
# SegmantiX

SegmantiX is a robust multi-tenancy and access control library for Java applications.

This library is a fork of the FlexiCore project, designed to isolate multi-tenancy features. Framework-agnostic and lightweight, it relies solely on JPA and `slf4j-api`, offering fine-grained security controls for managing permissions across users, roles, tenants, and instance groups.

## Features

- **Multi-Tenancy Support**: Secure, isolated environments for tenants.
- **Role-Based Access Control**: Manage permissions at a role level.
- **User-Level Permissions**: Precise access control for individual users.
- **Instance Group Management**: Secure logical resource groups.
- **Wildcard Access**: Broad permissions using wildcards.
- **Restricted Data Access**: Deny data access for unauthorized users or roles.
- **Operation-Specific Access**: Control permissions for operations like read, write, or delete.
- **Data Set Management**: Organize and secure logical collections of data.

## Dependencies

- Java 21+
- JPA
- `slf4j-api`

## Installation

It is recommended to use the `segmantix-dependencies` BOM (Bill of Materials) in your dependency management to simplify version alignment. Include it in your Maven project:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.wizzdi</groupId>
            <artifactId>segmantix-dependencies</artifactId>
            <version>2.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Then, add the desired module(s) to your dependencies:

```xml
<dependencies>
    <dependency>
        <groupId>com.wizzdi</groupId>
        <artifactId>segmantix-core</artifactId>
    </dependency>
    <dependency>
        <groupId>com.wizzdi</groupId>
        <artifactId>segmantix-jpa-store</artifactId>
    </dependency>
</dependencies>
```

## Modules

### SegmantiX Core

#### Description
The core API provides foundational functionality for multi-tenancy and access control. It is lightweight and framework-agnostic, making it suitable for various Java applications.

#### Configuration
To use SegmantiX Core, implement the following interfaces. These can be in-memory or persisted, depending on your needs:

- **Core Interfaces**:
    - `IInstanceGroup`: Manage permissions for instance groups.
    - `IOperation`: Define operations (e.g., read, write, delete).
    - `IOperationGroup`: Group related operations.
    - `IRole`, `IRoleSecurityLink`: Manage roles and their permissions.
    - `ISecurityLink`, `ISecurityContext`: Define and manage access controls.
    - `ITenant`, `ITenantSecurityLink`: Manage tenant-specific permissions.
    - `IUser`, `IUserSecurityLink`: Manage user-specific permissions.

- **Utility Components**:
    - `SegmantixCache`: Cache security link results to enhance performance.
    - `FieldPathProvider`: Extract paths for secured queries from JPA roots.
    - `OperationGroupLinkProvider`: Retrieve operations linked to groups.
    - `SecurityLinkProvider`: Fetch relevant security links for a context.

### SegmantiX JPA Store

#### Description
The `segmantix-jpa-store` module provides JPA-based implementations for many of the core interfaces, enabling the persistence of users, roles, tenants, and permission links in a database.

#### Configuration
1. Provide an `EntityManager`.
2. Provide a `SegmantixCache` instance to cache security results.
3. Optionally, define an `Operations` object containing a list of specific `SecurityOperation` objects and the `allOperations` operation. If not provided, the module will automatically create a default `Operations` instance with only the `allOperations` operation.

Example:

```java
SecurityOperation allOperations = new SecurityOperation(null, null, "all", "All Operations", null, Access.ALLOW, null);
Operations operations = new Operations(List.of(
    new SecurityOperation(null, null, "read", "Read Operation", null, Access.ALLOW, null),
    new SecurityOperation(null, null, "write", "Write Operation", null, Access.DENY, null)
), allOperations);

SecurityRepository securityRepository = SegmantixJPAStore.create(
    entityManager,
    segmantixCache,
    operations.allOperations()
);
```

#### Services
The `segmantix-jpa-store` module provides a set of services to manage the core entities. These services follow the naming convention `XService`, where `X` represents the entity being managed. Examples include:

- **`SecurityUserService`**: Manage `SecurityUser` entities.
- **`RoleService`**: Manage `Role` entities.
- **`SecurityTenantService`**: Manage `SecurityTenant` entities.
- **`TenantToUserService`**: Manage tenant-to-user associations.
- **`RoleToUserService`**: Manage role-to-user associations.
- **`PermissionGroupService`**: Manage `PermissionGroup` (instance group) entities.
- **`PermissionGroupToBaseclassService`**: Manage instance group-to-baseclass links.
- **`UserToBaseclassService`**: Manage user-to-baseclass associations.
- **`RoleToBaseclassService`**: Manage role-to-baseclass associations.
- **`TenantToBaseclassService`**: Manage tenant-to-baseclass associations.

Each service provides standard methods for creating, updating, listing, and retrieving entities. Below is an example using `SecurityUserService`:

Example usage of `SecurityUserService`:

```java
@Autowired
private SecurityUserService securityUserService;

public void createUser(SecurityContext securityContext) {
    SecurityUserCreate createRequest = new SecurityUserCreate();
    createRequest.setName("New User");
    SecurityUser newUser = securityUserService.createSecurityUser(createRequest, securityContext);
    System.out.println("Created user: " + newUser.getId());
}

public PaginationResponse<SecurityUser> listUsers(SecurityContext securityContext) {
    SecurityUserFilter filter = new SecurityUserFilter();
    return securityUserService.getAllSecurityUsers(filter, securityContext);
}
```

#### Interceptor and `SecurityContextProvider`

For most applications, an interceptor extends the `OncePerRequestFilter` class to extract the `userId` from a JWT token or identify impersonation details. The `SecurityContextProvider` is then used to create and manage security contexts. Typical usage involves:

1. Extracting `userId` and tenant information from the JWT token.
2. Determining the operation ID based on the intercepted method.
3. Using `SecurityContextProvider` to set up the security context.

Example:

```java
@Component
public class SecurityInterceptor extends OncePerRequestFilter {

    @Autowired
    private SecurityContextProvider securityContextProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = extractUserIdFromJwt(request);
        SecurityUser securityUser = findSecurityUser(userId);
        SecurityOperation operation = determineOperation(request);

        SecurityContext securityContext = securityContextProvider.getSecurityContext(securityUser, operation);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }

    private String extractUserIdFromJwt(HttpServletRequest request) {
        // Extract the user ID from the JWT token
        return "exampleUserId";
    }

    private SecurityUser findSecurityUser(String userId) {
        // Retrieve the SecurityUser using the SecurityUserService
        return securityUserService.findByIdOrNull(userId, SecurityUser.class);
    }

    private SecurityOperation determineOperation(HttpServletRequest request) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // Use SecurityOperation.ofMethod to create the operation
            return SecurityOperation.ofMethod(method, "operationId", method.getName(), null, Access.ALLOW, null);
        }
        throw new IllegalArgumentException("Handler is not a HandlerMethod");
    }
}
```

### SegmantiX Spring

#### Description
The `segmantix-spring` module builds on the JPA store, offering Spring Boot auto-configuration and integration.

#### Configuration
To use this module:

1. Annotate your application with `@EnableSegmantix`.
2. Optionally, provide a bean of type `Operations` to define custom operations. If not provided, a default `Operations` instance will be created containing only the `allOperations` operation.

Example:

```java
@Configuration
@EnableSegmantix
public class SegmantixConfig {

    @Bean
    public Operations operations() {
        SecurityOperation allOperations = new SecurityOperation(null, null, "all", "All Operations", null, Access.ALLOW, null);
        SecurityOperation readOperation = new SecurityOperation(null, null, "read", "Read Operation", null, Access.ALLOW, null);
        SecurityOperation writeOperation = new SecurityOperation(null, null, "write", "Write Operation", null, Access.DENY, null);
        return new Operations(List.of(readOperation, writeOperation), allOperations);
    }
}
```

## Usage

Integrate security predicates into your queries with the `SecurityRepository`. Example:

```java
public List<TestEntity> listAllTestEntities(
        TestEntityFilter filtering, ISecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<TestEntity> q = cb.createQuery(TestEntity.class);
    Root<TestEntity> r = q.from(TestEntity.class);
    List<Predicate> preds = new ArrayList<>();
    securityRepository.addSecurityPredicates(cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<TestEntity> query = em.createQuery(q);
    return query.getResultList();
}
```

## Testing

Unit tests cover scenarios such as:

- User access within their tenant.
- Role-based access control.
- Instance group-based permissions.
- Denial of access at various levels (user, role, tenant).

Run tests with your preferred build tool:

```bash
mvn test
```

## Future Enhancements

- Develop a permission management UI.
- Migrate FlexiCore features to create a `jpa-store` implementation.

## Contribution

Contributions are welcome! Submit feature requests before opening pull requests to align with project goals. Report bugs to help improve the library.

## License

SegmantiX is licensed under the Apache License 2.0. See `LICENSE` for details.

## Contact

For support or questions, contact us at [support@wizzdi.com](mailto:support@wizzdi.com).

## Projects Using SegmantiX

- [Wizzdi Cloud](https://wizzdi.com): A no-code platform for backend development.

We look forward to adding more projects to this list!
