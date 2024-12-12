package com.wizzdi.segmantix;


import com.wizzdi.segmantix.api.Access;
import com.wizzdi.segmantix.api.IOperationGroup;
import com.wizzdi.segmantix.api.IPermissionGroup;
import com.wizzdi.segmantix.api.IPermissionGroupToBaseclass;
import com.wizzdi.segmantix.api.IRole;
import com.wizzdi.segmantix.api.IRoleToBaseclass;
import com.wizzdi.segmantix.api.ISecurityOperation;
import com.wizzdi.segmantix.api.ISecurityTenant;
import com.wizzdi.segmantix.api.ISecurityUser;
import com.wizzdi.segmantix.api.ITenantToBaseclass;
import com.wizzdi.segmantix.api.IUserToBaseclass;
import com.wizzdi.segmantix.app.App;
import com.wizzdi.segmantix.app.OperationService;
import com.wizzdi.segmantix.app.PermissionGroupToBaseclassServiceImpl;
import com.wizzdi.segmantix.app.SecurityLinkServiceImpl;
import com.wizzdi.segmantix.app.TestEntity;
import com.wizzdi.segmantix.app.TestEntityCreate;
import com.wizzdi.segmantix.app.TestEntityFilter;
import com.wizzdi.segmantix.app.TestEntityService;
import com.wizzdi.segmantix.service.SecurityContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.wizzdi.segmantix.service.SecurityPermissionEntry.SECURITY_WILDCARD_PLACEHOLDER;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class SecurityQueryTest {
	    private final static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15")

			.withDatabaseName("flexicore-test")
			.withUsername("flexicore")
			.withPassword("flexicore");
	
	static{
		postgresqlContainer.start();
	}

    @Autowired
    private PermissionGroupToBaseclassServiceImpl permissionGroupToBaseclassServiceImpl;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private TestEntityService testEntityService;
    @Autowired
    private SecurityLinkServiceImpl securityLinkService;
    @Autowired
    private OperationService operationService;
    @Autowired
    private PermissionGroupToBaseclassServiceImpl permissionGroupToBaseclassService;


    private SecurityContext test1SecurityContext;
    private SecurityContext test2SecurityContext;
    private SecurityContext test3SecurityContext;

    private Set<String> othersInTenantIds=new HashSet<>();
    private Set<String> othersInOtherTenantIds =new HashSet<>();


    record User(String id) implements ISecurityUser{

        @Override
        public String getId() {
            return id();
        }
    }
    record Role(String id,Tenant tenant) implements IRole{

        @Override
        public ISecurityTenant getTenant() {
            return tenant();
        }

        @Override
        public String getId() {
            return id();
        }
    }
    record Tenant(String id) implements ISecurityTenant{

        @Override
        public String getId() {
            return id();
        }
    }

    record UserToBaseclass(String id, String securedId, String securedType, IPermissionGroup permissionGroup,
                           Access access, ISecurityOperation operation, IOperationGroup operationGroup,
                           ISecurityUser user) implements IUserToBaseclass {
        @Override
        public ISecurityUser getUser() {
            return user;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getSecuredId() {
            return securedId;
        }

        @Override
        public String getSecuredType() {
            return securedType;
        }

        @Override
        public IPermissionGroup getPermissionGroup() {
            return permissionGroup;
        }

        @Override
        public Access getAccess() {
            return access;
        }

        @Override
        public ISecurityOperation getOperation() {
            return operation;
        }

        @Override
        public IOperationGroup getOperationGroup() {
            return operationGroup;
        }
    }

    record RoleToBaseclass(String id, String securedId, String securedType, IPermissionGroup permissionGroup,
                           Access access, ISecurityOperation operation, IOperationGroup operationGroup,
                           IRole role) implements IRoleToBaseclass {
        @Override
        public IRole getRole() {
            return role;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getSecuredId() {
            return securedId;
        }

        @Override
        public String getSecuredType() {
            return securedType;
        }

        @Override
        public IPermissionGroup getPermissionGroup() {
            return permissionGroup;
        }

        @Override
        public Access getAccess() {
            return access;
        }

        @Override
        public ISecurityOperation getOperation() {
            return operation;
        }

        @Override
        public IOperationGroup getOperationGroup() {
            return operationGroup;
        }
    }

    record TenantToBaseclass(String id, String securedId, String securedType, IPermissionGroup permissionGroup,
                             Access access, ISecurityOperation operation, IOperationGroup operationGroup,
                             ISecurityTenant tenant) implements ITenantToBaseclass {
        @Override
        public ISecurityTenant getTenant() {
            return tenant;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getSecuredId() {
            return securedId;
        }

        @Override
        public String getSecuredType() {
            return securedType;
        }

        @Override
        public IPermissionGroup getPermissionGroup() {
            return permissionGroup;
        }

        @Override
        public Access getAccess() {
            return access;
        }

        @Override
        public ISecurityOperation getOperation() {
            return operation;
        }

        @Override
        public IOperationGroup getOperationGroup() {
            return operationGroup;
        }
    }

    record Operation(String id) implements ISecurityOperation{

        @Override
        public String getId() {
            return id();
        }
    }
    record PermissionGroup(String id) implements IPermissionGroup{
        @Override
        public String getId() {
            return id;
        }
    }
    record PermissionGroupToBaseclass(IPermissionGroup permissionGroup,String securedId,String securedType) implements IPermissionGroupToBaseclass{
        @Override
        public String getSecuredId() {
            return securedId;
        }

        @Override
        public String getSecuredType() {
            return securedType;
        }

        @Override
        public IPermissionGroup getPermissionGroup() {
            return permissionGroup;
        }
    }

    private static final Operation testOperation = new Operation("test");
    @BeforeAll
    public void init() {
        User testUser1 = new User("testUser1");
        User testUser2 =new User("testUser2");
        User testUser3 =new User("testUser3");
        Tenant testTenant1 = new Tenant("testTenant1");
        Tenant testTenant2=new Tenant("testTenant2");
        Tenant testTenant3=new Tenant("testTenant3");


        Role testRole = new Role("role1", testTenant1);
        test1SecurityContext = new SecurityContext(testUser1, List.of(testTenant1, testTenant2), List.of(testRole), testOperation);
        test2SecurityContext = new SecurityContext(testUser2, List.of(testTenant2), List.of(), testOperation);
        test3SecurityContext = new SecurityContext(testUser3, List.of(testTenant3), List.of(), testOperation);
        othersInOtherTenantIds =IntStream.range(0, 10).mapToObj(f->testEntityService.createTestEntity(new TestEntityCreate().setName("othersInOtherTenant"+f), test3SecurityContext)).map(f->f.getId()).collect(Collectors.toSet());
        List<TestEntity> list = IntStream.range(0, 10).mapToObj(f -> testEntityService.createTestEntity(new TestEntityCreate().setName("othersInTenant" + f), test2SecurityContext)).toList();

        othersInTenantIds= list.stream().map(f->f.getId()).collect(Collectors.toSet());

    }



    @Test
    @Order(1)
    public void testForCreator() {
        TestEntity forCreator = testEntityService.createTestEntity(new TestEntityCreate().setName("forCreator"), test1SecurityContext);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forCreator.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }
    @Test
    @Order(2)
    public void testForUser() {
        TestEntity forUser = testEntityService.createTestEntityNoMerge(new TestEntityCreate().setName("forUser"), test2SecurityContext);
        testEntityService.merge(forUser);
        UserToBaseclass securityLink = new UserToBaseclass(UUID.randomUUID().toString(), forUser.getId(), TestEntity.class.getCanonicalName(), null, Access.allow, operationService.getAllOps(), null, test1SecurityContext.securityUser());
        securityLinkService.add(securityLink);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forUser.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(3)
    public void testForRole() {
        TestEntity forRole = testEntityService.createTestEntity(new TestEntityCreate().setName("forRole"), test2SecurityContext);
        testEntityService.merge(forRole);

        RoleToBaseclass roleToBaseclass = new RoleToBaseclass(UUID.randomUUID().toString(),forRole.getId(),TestEntity.class.getCanonicalName(),null,Access.allow, operationService.getAllOps(), null,test1SecurityContext.roles().getFirst());
        securityLinkService.add(roleToBaseclass);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forRole.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(4)
    public void testForTenant() {
        TestEntity forTenant = testEntityService.createTestEntity(new TestEntityCreate().setName("forTenant"), test2SecurityContext);

        TenantToBaseclass tenantToBaseclass=new TenantToBaseclass(UUID.randomUUID().toString(),forTenant.getId(),forTenant.getClass().getCanonicalName(),null,Access.allow, operationService.getAllOps(), null,test1SecurityContext.tenants().getLast());
        securityLinkService.add(tenantToBaseclass);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forTenant.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }



    @Test
    @Order(5)
    public void testPermissionGroupForUser() {
        PermissionGroup forUserGroup = new PermissionGroup("forRoleGroup");
        TestEntity forUser = testEntityService.createTestEntity(new TestEntityCreate().setName("forUser"), test2SecurityContext);

        PermissionGroupToBaseclass permissionGroupToBaseclass = new PermissionGroupToBaseclass(forUserGroup,forUser.getId(),forUser.getClass().getCanonicalName());
        permissionGroupToBaseclassServiceImpl.add(permissionGroupToBaseclass);
        UserToBaseclass userToBaseclass = new UserToBaseclass(UUID.randomUUID().toString(), null, null, forUserGroup, Access.allow, operationService.getAllOps(), null, test1SecurityContext.securityUser());
        securityLinkService.add(userToBaseclass);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forUser.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(6)
    public void testPermissionGroupForRole() {
        PermissionGroup forRoleGroup = new PermissionGroup("forRoleGroup");
        TestEntity forRole = testEntityService.createTestEntity(new TestEntityCreate().setName("forRole"), test2SecurityContext);
        PermissionGroupToBaseclass permissionGroupToBaseclass = new PermissionGroupToBaseclass(forRoleGroup,forRole.getId(),forRole.getClass().getCanonicalName());
        permissionGroupToBaseclassServiceImpl.add(permissionGroupToBaseclass);
        RoleToBaseclass roleToBaseclass=new RoleToBaseclass(UUID.randomUUID().toString(),null,null,forRoleGroup,Access.allow, operationService.getAllOps(), null,test1SecurityContext.roles().getFirst());
        securityLinkService.add(roleToBaseclass);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forRole.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));
    }

    @Test
    @Order(7)
    public void testPermissionGroupForTenant() {
        PermissionGroup forTenantGroup = new PermissionGroup("forTenantGroup");
        TestEntity forTenant = testEntityService.createTestEntity(new TestEntityCreate().setName("forTenant"), test2SecurityContext);
        PermissionGroupToBaseclass permissionGroupToBaseclass = new PermissionGroupToBaseclass(forTenantGroup,forTenant.getId(),forTenant.getClass().getCanonicalName());
        permissionGroupToBaseclassServiceImpl.add(permissionGroupToBaseclass);
        TenantToBaseclass tenantToBaseclass=new TenantToBaseclass(UUID.randomUUID().toString(),null,null,forTenantGroup,Access.allow, operationService.getAllOps(), null,test1SecurityContext.tenants().getLast());
        securityLinkService.add(tenantToBaseclass);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forTenant.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }
    @Test
    @Order(8)
    public void testUserToClazz() {
        securityLinkService.clear();
        permissionGroupToBaseclassServiceImpl.clear();
        othersInTenantIds=testEntityService.listAllTestEntities(new TestEntityFilter(),test2SecurityContext).stream().map(f->f.getId()).collect(Collectors.toSet());
        // create a fresh user and link it to a tenant
        User freshUser = new User("freshUser1");
        Tenant freshTenant = new Tenant("freshTenant1");
        SecurityContext freshUserSecurityContext=new SecurityContext(freshUser,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),List.of(),testOperation);

        // validate it has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), freshUserSecurityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant it access for clazz
        UserToBaseclass userToBaseclass=new UserToBaseclass(UUID.randomUUID().toString(),null,TestEntity.class.getCanonicalName(),null,Access.allow, operationService.getAllOps(), null,freshUser);
        securityLinkService.add(userToBaseclass);

        // validate it has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), freshUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));

    }

    @Test
    @Order(9)
    public void testRoleToClazz() {
        securityLinkService.clear();
        permissionGroupToBaseclassServiceImpl.clear();

        // create a fresh role, user and link them to a tenant
        Tenant freshTenant = new Tenant("freshTenant2");

        Role freshRole = new Role("freshRole2",freshTenant);

        User freshUserWithRole = new User("freshUserWithRole2");
        SecurityContext securityContext=new SecurityContext(freshUserWithRole,List.of(freshTenant,test2SecurityContext.tenants().getFirst(),test3SecurityContext.tenants().getFirst()),List.of(freshRole),testOperation);

        // validate the user with role has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the role access for clazz
        RoleToBaseclass roleToBaseclass=new RoleToBaseclass(UUID.randomUUID().toString(),null,TestEntity.class.getCanonicalName(),null,Access.allow, operationService.getAllOps(), null,freshRole);
        securityLinkService.add(roleToBaseclass);

        // validate the user with role has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));
        //validate role does not have access to Test Entities in other tenant which he belongs to
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));


    }

    @Test
    @Order(10)
    public void testTenantToClazz() {

        securityLinkService.clear();
        permissionGroupToBaseclassServiceImpl.clear();
        // create a fresh tenant and a user linked to it
        Tenant freshTenant = new Tenant("freshTenant3");
        User freshUserInTenant = new User("freshUserInTenant3");

        SecurityContext securityContext=new SecurityContext(freshUserInTenant,List.of(freshTenant,test2SecurityContext.tenants().getFirst(),test3SecurityContext.tenants().getFirst()),List.of(),testOperation);
        // validate the user in the tenant has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the tenant access for clazz
        TenantToBaseclass tenantToBaseclass=new TenantToBaseclass(UUID.randomUUID().toString(),null,TestEntity.class.getCanonicalName(),null,Access.allow, operationService.getAllOps(), null,securityContext.tenants().get(1));
        securityLinkService.add(tenantToBaseclass);

        // validate the user in the tenant has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));
    }

    @Test
    @Order(8)
    public void testUserToAll() {

        securityLinkService.clear();
        permissionGroupToBaseclassServiceImpl.clear();
        // create a fresh user and link it to a tenant
        User freshUser = new User("freshUser4");
        Tenant freshTenant = new Tenant("freshTenant4");
        SecurityContext securityContext=new SecurityContext(freshUser,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),List.of(),testOperation);

        // validate it has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant it access for clazz
        UserToBaseclass userToBaseclass= new UserToBaseclass(UUID.randomUUID().toString(),null,SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,freshUser);
        securityLinkService.add(userToBaseclass);

        // validate it has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(),securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));

    }

    @Test
    @Order(9)
    public void testRoleToAll() {

        securityLinkService.clear();
        permissionGroupToBaseclassServiceImpl.clear();
        // create a fresh role, user and link them to a tenant
        Tenant freshTenant = new Tenant("freshTenant5");

        Role freshRole = new Role("freshRole5",freshTenant);

        User freshUserWithRole = new User("freshUserWithRole5");
        SecurityContext securityContext=new SecurityContext(freshUserWithRole,List.of(freshTenant,test2SecurityContext.tenants().getFirst(),test3SecurityContext.tenants().getFirst()),List.of(freshRole),testOperation);

        // validate the user with role has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the role access for clazz
        RoleToBaseclass roleToBaseclass=new RoleToBaseclass(UUID.randomUUID().toString(),null,SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,freshRole);
        securityLinkService.add(roleToBaseclass);

        // validate the user with role has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));


    }

    @Test
    @Order(10)
    public void testTenantToAll() {


        securityLinkService.clear();
        permissionGroupToBaseclassServiceImpl.clear();
        // create a fresh tenant and a user linked to it
        Tenant freshTenant = new Tenant("freshTenant6");
        User freshUserInTenant = new User("freshUserInTenant6");
        SecurityContext securityContext=new SecurityContext(freshUserInTenant,List.of(freshTenant,test2SecurityContext.tenants().getFirst(),test3SecurityContext.tenants().getFirst()),List.of(),testOperation);

        // validate the user in the tenant has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the tenant access for clazz
        TenantToBaseclass tenantToBaseclass=new TenantToBaseclass(UUID.randomUUID().toString(),null,SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,test2SecurityContext.tenants().getFirst());
        securityLinkService.add(tenantToBaseclass);
        //validate role does not have access to Test Entities in other tenant which he belongs to
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));

        // validate the user in the tenant has access to all TestEntities in the tenant
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
    }

    @Test
    @Order(11)
    public void testAccessDenyForUser() {

        securityLinkService.clear();
        permissionGroupToBaseclassServiceImpl.clear();
        TenantToBaseclass tenantToBaseclass=new TenantToBaseclass(UUID.randomUUID().toString(),null,SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,test2SecurityContext.tenants().getFirst());
        securityLinkService.add(tenantToBaseclass);
        User freshUser = new User("freshUser7");
        Tenant freshTenant = new Tenant("freshTenant7");

        // validate it has access to nothing
        SecurityContext securityContext = new SecurityContext(freshUser,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),List.of(),testOperation);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

        // grant it access for clazz
        String toDeny = othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found"));
        UserToBaseclass userToBaseclass=new UserToBaseclass(UUID.randomUUID().toString(),toDeny,TestEntity.class.getCanonicalName(),null,Access.deny, operationService.getAllOps(), null,freshUser);
        securityLinkService.add(userToBaseclass);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny)));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(12)
    public void testAccessDenyForRole() {

        securityLinkService.clear();
        permissionGroupToBaseclassServiceImpl.clear();
        TenantToBaseclass tenantToBaseclass=new TenantToBaseclass(UUID.randomUUID().toString(),null,SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,test2SecurityContext.tenants().getFirst());
        securityLinkService.add(tenantToBaseclass);
        Tenant freshTenant = new Tenant("freshTenant8");
        Role freshRole = new Role("freshRole8",freshTenant);

        User freshUserWithRole = new User("freshUserWithRole8");

        // validate it has access to nothing
        SecurityContext securityContext = new SecurityContext(freshUserWithRole,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),List.of(freshRole),testOperation);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

        // grant it access for clazz
        String toDeny =  othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found"));
        RoleToBaseclass roleToBaseclass=new RoleToBaseclass(UUID.randomUUID().toString(),toDeny,TestEntity.class.getCanonicalName(),null,Access.deny, operationService.getAllOps(), null,freshRole);
        securityLinkService.add(roleToBaseclass);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny)));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
    }

  /**  invalid test ? tenant A allows access to all stuff in it , tenant b specifically denies access to something that belongs to tenant A
   @Test
    @Order(13)
    public void testAccessDenyForTenant() {

        securityLinkService.clear();
        permissionGroupToBaseclassServiceImpl.clear();
        TenantToBaseclass allowAll=new TenantToBaseclass(UUID.randomUUID().toString(),null,SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,test2SecurityContext.tenants().getFirst());
        securityLinkService.add(allowAll);
        User freshUser = new User("freshUser9");
        Tenant freshTenant = new Tenant("freshTenant9");

        // validate it has access to nothing
        SecurityContext securityContext = new SecurityContext(freshUser,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),List.of(),testOperation);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

        // grant it access for clazz
        String toDeny =  othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found"));
        TenantToBaseclass tenantToBaseclass=new TenantToBaseclass(UUID.randomUUID().toString(),toDeny,TestEntity.class.getCanonicalName(),null,Access.deny, operationService.getAllOps(), null,freshTenant);
        securityLinkService.add(tenantToBaseclass);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny)));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
    }
    **/
   /**
    @Test
    @Order(14)
    public void testAccessDenyForTenantWithCache() {
        SecurityUser freshUser = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUser"), null);
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUser).setDefaultTenant(true).setTenant(freshTenant),null);
        tenantToBaseclassService.createTenantToBaseclass(new TenantToBaseclassCreate().setTenant(freshTenant).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(SecurityWildcard.class.getCanonicalName())),null);

        // validate it has access to nothing
        SecurityContextBase securityContext = securityContextProvider.getSecurityContext(freshUser);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        for (int i = 0; i < 1000; i++) {
            TestEntity entity = testEntityService.createTestEntity(new TestEntityCreate().setName("i:"+i), adminSecurityContext);
            othersInTenantIds.add(entity.getId());
        }
        // grant it access for clazz
        TestEntity toDeny = testEntityService.findByIdOrNull(TestEntity.class, othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found")));
        tenantToBaseclassService.createTenantToBaseclass(new TenantToBaseclassCreate().setTenant(freshTenant).setAccess(IOperation.Access.deny).setBaseclass(toDeny.getSecurity()),null);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny.getId())));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
            Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny.getId())));
            Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        }
        System.out.println("took: "+(System.currentTimeMillis()-start)/100.0+" ms");
    }

**/

}
