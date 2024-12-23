# SegmantiX

SegmantiX is a powerful multi-tenancy and access control library for any Java applications.

This library is a fork of our existing project, FlexiCore. The purpose of this fork is to separate the multi-tenancy features from other FlexiCore features. It is framework-agnostic and relies only on JPA and slf4j-api, making it flexible for various use cases. It provides fine-grained security controls, allowing developers to manage access permissions for users, roles, tenants, and instance groups at a granular level.


## Features

- **Multi-Tenancy Support**: SegmantiX enables secure, isolated environments for tenants.

- **Role-Based Access Control**: Assign permissions to roles and manage access at a broader scale.

- **User-Level Permissions**: Grant or deny access to specific users for targeted control.

- **Instance Group Management**: Define and secure logical groups of resources.

- **Wildcards for Global Access**: Utilize wildcard permissions for broad access.

- **Deny Access to Data**: Restrict access to data for unauthorized users or roles.

- **Grant Access Under Specific Operations**: Fine-tune access control by restricting permissions to specific operations like read, write, or delete.

- **Manage Access to Data Sets**: Organize and control permissions for logical collections of data effectively.

## Dependencies

- Java 21+
- JPA
- slf4j-api

## Installation

Add the following dependency to your Maven project:

```xml
<dependency>
    <groupId>com.wizzdi</groupId>
    <artifactId>segmantix-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Configuration

To use SegmantiX, you need to implement the following interfaces. These can be implemented in-memory or persisted, depending on your application's requirements:

- `IInstanceGroup`: A group of instances to manage permissions for.
- `IOperation`: An operation, such as read/write/delete/admin, or custom operations like `deleteAccount`.
- `IOperationGroup`: A group of operations.
- `IOperationGroupLink`: A link between an operation and a group.
- `IRole`: A role.
- `IRoleSecurityLink`: Security granted to a role.
- `ISecurityLink`: Security Link grants access to an instance, instance group, or type under specific or all operations.
- `ISecurityContext`: Holds the user, their roles/tenants, and the current operation.
- `ISecurityGroup`: A group of security links that logically belong together.
- `ITenant`: A tenant.
- `ITenantSecurityLink`: Security Link for a tenant.
- `IUser`: A user.
- `IUserSecurityLink`: Security Link for a user.

Additionally, you need to implement the following components:

- `Cache`: Caches the results of `SecurityLinkProvider` to avoid fetching repeatedly.
- `FieldPathProvider`: Extracts the required paths for executing a secured query from a JPA root.
- `OperationGroupLinkProvider`: Provides operations for given operation groups.
- `SecurityLinkProvider`: Provides a list of relevant links based on a security context.



## Usage

The library provides a `SecurityRepository` for integrating security predicates into your queries. Below is an example of using it with the Criteria API to list secured entities:

```java
public List<TestEntity> listAllTestEntities(
        TestEntityFilter filtering, ISecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<TestEntity> q = cb.createQuery(TestEntity.class);
    Root<TestEntity> r = q.from(TestEntity.class);
    List<Predicate> preds = new ArrayList<>();
    securedBasicRepository.addSecurityPredicates(cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<TestEntity> query = em.createQuery(q);
    return query.getResultList();
}
```

## Testing

 The provided unit tests cover various scenarios:

- User access to resources in their own tenant.
- Role-based access control to specific entities.
- Instance group-based access for users, roles, and tenants.
- Denial of access at the user, role, and tenant levels.

### Running Tests

Run the tests using your preferred build tool:

```bash
mvn test
```

## Future Work

- Add concrete implementations for required interfaces (`jpa-store` and `in-memory-store`).
- Introduce a permission management UI for enhanced usability.
- Migrate features from FlexiCore to SegmantiX to create the 'jpa-store' implementation

## Contribution

We welcome contributions to SegmantiX! Before creating a pull request, please submit a feature request to ensure alignment with the project's goals. Additionally, report any bugs you encounter to help improve the library.

## License

SegmantiX is licensed under the Apache License 2.0. See `LICENSE` for more information.

## Contact

For questions or support, reach out to the development team at [support@wizzdi.com](mailto\:support@wizzdi.com).

