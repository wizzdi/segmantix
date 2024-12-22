package com.wizzdi.segmantix;


import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.app.DataSecurityConfig;
import com.wizzdi.segmantix.app.InstanceGroupLinkServiceImpl;
import com.wizzdi.segmantix.model.Access;
import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.model.IOperationGroup;
import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.IRoleSecurityLink;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.IUser;
import com.wizzdi.segmantix.api.model.ITenantSecurityLink;
import com.wizzdi.segmantix.api.model.IUserSecurityLink;
import com.wizzdi.segmantix.app.App;
import com.wizzdi.segmantix.app.OperationService;
import com.wizzdi.segmantix.app.SecurityLinkProviderImpl;
import com.wizzdi.segmantix.app.TestEntity;
import com.wizzdi.segmantix.app.TestEntityCreate;
import com.wizzdi.segmantix.app.TestEntityFilter;
import com.wizzdi.segmantix.app.TestEntityService;
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
    private InstanceGroupLinkServiceImpl instanceGroupLinkServiceImpl;


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private TestEntityService testEntityService;
    @Autowired
    private SecurityLinkProviderImpl securityService;
    @Autowired
    private OperationService operationService;

    private SecurityContext test1SecurityContext;
    private SecurityContext test2SecurityContext;
    private SecurityContext test3SecurityContext;

    private Set<String> othersInTenantIds=new HashSet<>();
    private Set<String> othersInOtherTenantIds =new HashSet<>();

record SecurityContext(User user,List<Tenant> tenants,Tenant tenantToCreateIn,List<Role> roles,Operation operation) implements ISecurityContext{

}
    record User(String id) implements IUser {

        @Override
        public String getId() {
            return id();
        }
    }
    record Role(String id,Tenant tenant) implements IRole{

        @Override
        public ITenant getTenant() {
            return tenant();
        }

        @Override
        public String getId() {
            return id();
        }
    }
    record Tenant(String id) implements ITenant {

        @Override
        public String getId() {
            return id();
        }
    }

    record UserSecurityLink(String id, String securedId, String securedType, IInstanceGroup instanceGroup,
                            Access access, IOperation operation, IOperationGroup operationGroup,
                            IUser user) implements IUserSecurityLink {
        @Override
        public IUser getUser() {
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
        public IInstanceGroup getInstanceGroup() {
            return instanceGroup;
        }

        @Override
        public Access getAccess() {
            return access;
        }

        @Override
        public IOperation getOperation() {
            return operation;
        }

        @Override
        public IOperationGroup getOperationGroup() {
            return operationGroup;
        }
    }

    record RoleSecurityLink(String id, String securedId, String securedType, IInstanceGroup instanceGroup,
                            Access access, IOperation operation, IOperationGroup operationGroup,
                            IRole role) implements IRoleSecurityLink {
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
        public IInstanceGroup getInstanceGroup() {
            return instanceGroup;
        }

        @Override
        public Access getAccess() {
            return access;
        }

        @Override
        public IOperation getOperation() {
            return operation;
        }

        @Override
        public IOperationGroup getOperationGroup() {
            return operationGroup;
        }
    }

    record TenantSecurityLink(String id, String securedId, String securedType, IInstanceGroup instanceGroup,
                              Access access, IOperation operation, IOperationGroup operationGroup,
                              ITenant tenant) implements ITenantSecurityLink {
        @Override
        public ITenant getTenant() {
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
        public IInstanceGroup getInstanceGroup() {
            return instanceGroup;
        }

        @Override
        public Access getAccess() {
            return access;
        }

        @Override
        public IOperation getOperation() {
            return operation;
        }

        @Override
        public IOperationGroup getOperationGroup() {
            return operationGroup;
        }
    }

    record Operation(String id) implements IOperation {

        @Override
        public String getId() {
            return id();
        }
    }
    record InstanceGroup(String id) implements IInstanceGroup {
        @Override
        public String getId() {
            return id;
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
        test1SecurityContext = new SecurityContext(testUser1, List.of(testTenant1, testTenant2),testTenant1, List.of(testRole), testOperation);
        test2SecurityContext = new SecurityContext(testUser2, List.of(testTenant2),testTenant2, List.of(), testOperation);
        test3SecurityContext = new SecurityContext(testUser3, List.of(testTenant3),testTenant3, List.of(), testOperation);
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
        UserSecurityLink security = new UserSecurityLink(UUID.randomUUID().toString(), forUser.getId(), TestEntity.class.getSimpleName(), null, Access.allow, operationService.getAllOps(), null, test1SecurityContext.user());
        securityService.add(security);
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

        RoleSecurityLink roleSecurity = new RoleSecurityLink(UUID.randomUUID().toString(),forRole.getId(),TestEntity.class.getSimpleName(),null,Access.allow, operationService.getAllOps(), null,test1SecurityContext.roles().getFirst());
        securityService.add(roleSecurity);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forRole.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(4)
    public void testForTenant() {
        TestEntity forTenant = testEntityService.createTestEntity(new TestEntityCreate().setName("forTenant"), test2SecurityContext);

        TenantSecurityLink tenantSecurity=new TenantSecurityLink(UUID.randomUUID().toString(),forTenant.getId(),forTenant.getClass().getSimpleName(),null,Access.allow, operationService.getAllOps(), null,test1SecurityContext.tenants().getLast());
        securityService.add(tenantSecurity);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forTenant.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }



    @Test
    @Order(5)
    public void testInstanceGroupForUser() {
        InstanceGroup forUserGroup = new InstanceGroup("forRoleGroup");
        TestEntity forUser = testEntityService.createTestEntity(new TestEntityCreate().setName("forUser").setPermissionGroupId(forUserGroup.id()), test2SecurityContext);

        UserSecurityLink userSecurity = new UserSecurityLink(UUID.randomUUID().toString(), null, null, forUserGroup, Access.allow, operationService.getAllOps(), null, test1SecurityContext.user());
        securityService.add(userSecurity);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forUser.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(6)
    public void testInstanceGroupForRole() {
        InstanceGroup forRoleGroup = new InstanceGroup("forRoleGroup");
        TestEntity forRole = testEntityService.createTestEntity(new TestEntityCreate().setName("forRole").setPermissionGroupId(forRoleGroup.id()), test2SecurityContext);
        RoleSecurityLink roleSecurity=new RoleSecurityLink(UUID.randomUUID().toString(),null,null,forRoleGroup,Access.allow, operationService.getAllOps(), null,test1SecurityContext.roles().getFirst());
        securityService.add(roleSecurity);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forRole.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));
    }

    @Test
    @Order(7)
    public void testInstanceGroupForTenant() {
        InstanceGroup forTenantGroup = new InstanceGroup("forTenantGroup");
        TestEntity forTenant = testEntityService.createTestEntity(new TestEntityCreate().setName("forTenant").setPermissionGroupId(forTenantGroup.id()), test2SecurityContext);
        TenantSecurityLink tenantSecurity=new TenantSecurityLink(UUID.randomUUID().toString(),null,null,forTenantGroup,Access.allow, operationService.getAllOps(), null,test1SecurityContext.tenants().getLast());
        securityService.add(tenantSecurity);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), test1SecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forTenant.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }
    @Test
    @Order(8)
    public void testUserToClazz() {
        securityService.clear();
        instanceGroupLinkServiceImpl.clear();
        othersInTenantIds=testEntityService.listAllTestEntities(new TestEntityFilter(),test2SecurityContext).stream().map(f->f.getId()).collect(Collectors.toSet());
        // create a fresh user and link it to a tenant
        User freshUser = new User("freshUser1");
        Tenant freshTenant = new Tenant("freshTenant1");
        SecurityContext freshUserSecurityContext=new SecurityContext(freshUser,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),freshTenant,List.of(),testOperation);

        // validate it has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), freshUserSecurityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant it access for clazz
        UserSecurityLink userSecurity=new UserSecurityLink(UUID.randomUUID().toString(),null,TestEntity.class.getSimpleName(),null,Access.allow, operationService.getAllOps(), null,freshUser);
        securityService.add(userSecurity);

        // validate it has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), freshUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));

    }

    @Test
    @Order(9)
    public void testRoleToClazz() {
        securityService.clear();
        instanceGroupLinkServiceImpl.clear();

        // create a fresh role, user and link them to a tenant
        Tenant freshTenant = new Tenant("freshTenant2");

        Role freshRole = new Role("freshRole2",freshTenant);

        User freshUserWithRole = new User("freshUserWithRole2");
        SecurityContext securityContext=new SecurityContext(freshUserWithRole,List.of(freshTenant,test2SecurityContext.tenants().getFirst(),test3SecurityContext.tenants().getFirst()),freshTenant,List.of(freshRole),testOperation);

        // validate the user with role has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the role access for clazz
        RoleSecurityLink roleSecurity=new RoleSecurityLink(UUID.randomUUID().toString(),null,TestEntity.class.getSimpleName(),null,Access.allow, operationService.getAllOps(), null,freshRole);
        securityService.add(roleSecurity);

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

        securityService.clear();
        instanceGroupLinkServiceImpl.clear();
        // create a fresh tenant and a user linked to it
        Tenant freshTenant = new Tenant("freshTenant3");
        User freshUserInTenant = new User("freshUserInTenant3");

        SecurityContext securityContext=new SecurityContext(freshUserInTenant,List.of(freshTenant,test2SecurityContext.tenants().getFirst(),test3SecurityContext.tenants().getFirst()),freshTenant,List.of(),testOperation);
        // validate the user in the tenant has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the tenant access for clazz
        TenantSecurityLink tenantSecurity=new TenantSecurityLink(UUID.randomUUID().toString(),null,TestEntity.class.getSimpleName(),null,Access.allow, operationService.getAllOps(), null,securityContext.tenants().get(1));
        securityService.add(tenantSecurity);

        // validate the user in the tenant has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));
    }

    @Test
    @Order(8)
    public void testUserToAll() {

        securityService.clear();
        instanceGroupLinkServiceImpl.clear();
        // create a fresh user and link it to a tenant
        User freshUser = new User("freshUser4");
        Tenant freshTenant = new Tenant("freshTenant4");
        SecurityContext securityContext=new SecurityContext(freshUser,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),freshTenant,List.of(),testOperation);

        // validate it has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant it access for clazz
        UserSecurityLink userSecurity= new UserSecurityLink(UUID.randomUUID().toString(),null, DataSecurityConfig.SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,freshUser);
        securityService.add(userSecurity);

        // validate it has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(),securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));

    }

    @Test
    @Order(9)
    public void testRoleToAll() {

        securityService.clear();
        instanceGroupLinkServiceImpl.clear();
        // create a fresh role, user and link them to a tenant
        Tenant freshTenant = new Tenant("freshTenant5");

        Role freshRole = new Role("freshRole5",freshTenant);

        User freshUserWithRole = new User("freshUserWithRole5");
        SecurityContext securityContext=new SecurityContext(freshUserWithRole,List.of(freshTenant,test2SecurityContext.tenants().getFirst(),test3SecurityContext.tenants().getFirst()),freshTenant,List.of(freshRole),testOperation);

        // validate the user with role has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the role access for clazz
        RoleSecurityLink roleSecurity=new RoleSecurityLink(UUID.randomUUID().toString(),null, DataSecurityConfig.SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,freshRole);
        securityService.add(roleSecurity);

        // validate the user with role has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));


    }

    @Test
    @Order(10)
    public void testTenantToAll() {


        securityService.clear();
        instanceGroupLinkServiceImpl.clear();
        // create a fresh tenant and a user linked to it
        Tenant freshTenant = new Tenant("freshTenant6");
        User freshUserInTenant = new User("freshUserInTenant6");
        SecurityContext securityContext=new SecurityContext(freshUserInTenant,List.of(freshTenant,test2SecurityContext.tenants().getFirst(),test3SecurityContext.tenants().getFirst()),freshTenant,List.of(),testOperation);

        // validate the user in the tenant has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the tenant access for clazz
        TenantSecurityLink tenantSecurity=new TenantSecurityLink(UUID.randomUUID().toString(),null, DataSecurityConfig.SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,test2SecurityContext.tenants().getFirst());
        securityService.add(tenantSecurity);
        //validate role does not have access to Test Entities in other tenant which he belongs to
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));

        // validate the user in the tenant has access to all TestEntities in the tenant
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
    }

    @Test
    @Order(11)
    public void testAccessDenyForUser() {

        securityService.clear();
        instanceGroupLinkServiceImpl.clear();
        TenantSecurityLink tenantSecurity=new TenantSecurityLink(UUID.randomUUID().toString(),null, DataSecurityConfig.SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,test2SecurityContext.tenants().getFirst());
        securityService.add(tenantSecurity);
        User freshUser = new User("freshUser7");
        Tenant freshTenant = new Tenant("freshTenant7");

        // validate it has access to nothing
        SecurityContext securityContext = new SecurityContext(freshUser,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),freshTenant,List.of(),testOperation);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

        // grant it access for clazz
        String toDeny = othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found"));
        UserSecurityLink userSecurity=new UserSecurityLink(UUID.randomUUID().toString(),toDeny,TestEntity.class.getSimpleName(),null,Access.deny, operationService.getAllOps(), null,freshUser);
        securityService.add(userSecurity);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny)));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(12)
    public void testAccessDenyForRole() {

        securityService.clear();
        instanceGroupLinkServiceImpl.clear();
        TenantSecurityLink tenantSecurity=new TenantSecurityLink(UUID.randomUUID().toString(),null, DataSecurityConfig.SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,test2SecurityContext.tenants().getFirst());
        securityService.add(tenantSecurity);
        Tenant freshTenant = new Tenant("freshTenant8");
        Role freshRole = new Role("freshRole8",freshTenant);

        User freshUserWithRole = new User("freshUserWithRole8");

        // validate it has access to nothing
        SecurityContext securityContext = new SecurityContext(freshUserWithRole,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),freshTenant,List.of(freshRole),testOperation);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

        // grant it access for clazz
        String toDeny =  othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found"));
        RoleSecurityLink roleSecurity=new RoleSecurityLink(UUID.randomUUID().toString(),toDeny,TestEntity.class.getSimpleName(),null,Access.deny, operationService.getAllOps(), null,freshRole);
        securityService.add(roleSecurity);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny)));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
    }

  /**  invalid test ? tenant A allows access to all stuff in it , tenant b specifically denies access to something that belongs to tenant A
   @Test
    @Order(13)
    public void testAccessDenyForTenant() {

        securityService.clear();
        instanceGroupLinkServiceImpl.clear();
        TenantSecurity allowAll=new TenantSecurity(UUID.randomUUID().toString(),null,SECURITY_WILDCARD_PLACEHOLDER,null,Access.allow, operationService.getAllOps(), null,test2SecurityContext.tenants().getFirst());
        securityService.add(allowAll);
        User freshUser = new User("freshUser9");
        Tenant freshTenant = new Tenant("freshTenant9");

        // validate it has access to nothing
        SecurityContext securityContext = new SecurityContext(freshUser,List.of(freshTenant,test2SecurityContext.tenants().getFirst()),List.of(),testOperation);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

        // grant it access for clazz
        String toDeny =  othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found"));
        TenantSecurity tenantSecurity=new TenantSecurity(UUID.randomUUID().toString(),toDeny,TestEntity.class.getSimpleName(),null,Access.deny, operationService.getAllOps(), null,freshTenant);
        securityService.add(tenantSecurity);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny)));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
    }
    **/
   /**
    @Test
    @Order(14)
    public void testAccessDenyForTenantWithCache() {
        User freshUser = userService.createUser(new UserCreate().setName("freshUser"), null);
        Tenant freshTenant = tenantService.createTenant(new TenantCreate().setName("freshTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUser).setDefaultTenant(true).setTenant(freshTenant),null);
        tenantSecurityService.createTenantSecurity(new TenantSecurityCreate().setTenant(freshTenant).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(SecurityWildcard.class.getSimpleName())),null);

        // validate it has access to nothing
        SecurityContext securityContext = securityContextProvider.getSecurityContext(freshUser);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        for (int i = 0; i < 1000; i++) {
            TestEntity entity = testEntityService.createTestEntity(new TestEntityCreate().setName("i:"+i), adminSecurityContext);
            othersInTenantIds.add(entity.getId());
        }
        // grant it access for clazz
        TestEntity toDeny = testEntityService.findByIdOrNull(TestEntity.class, othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found")));
        tenantSecurityService.createTenantSecurity(new TenantSecurityCreate().setTenant(freshTenant).setAccess(IOperation.Access.deny).setBaseclass(toDeny.getSecurity()),null);
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
