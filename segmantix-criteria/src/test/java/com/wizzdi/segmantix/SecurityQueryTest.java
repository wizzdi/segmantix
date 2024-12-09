package com.wizzdi.segmantix;


import com.wizzdi.segmantix.api.IRole;
import com.wizzdi.segmantix.api.ISecurityOperation;
import com.wizzdi.segmantix.api.ISecurityTenant;
import com.wizzdi.segmantix.api.ISecurityUser;
import com.wizzdi.segmantix.api.ITenantToUser;
import com.wizzdi.segmantix.app.App;
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

@DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private TestEntityService testEntityService;


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
    record TenantToUser(String id,Tenant tenant,User user) implements ITenantToUser{

        @Override
        public ISecurityTenant getTenant() {
            return tenant;
        }

        @Override
        public ISecurityUser getUser() {
            return user;
        }
    }
    record Operation(String id) implements ISecurityOperation{

        @Override
        public String getId() {
            return id();
        }
    }

    @BeforeAll
    public void init() {
        User testUser1 = new User("testUser1");
        User testUser2 =new User("testUser2");
        User testUser3 =new User("testUser3");
        Tenant testTenant1 = new Tenant("testTenant1");
        Tenant testTenant2=new Tenant("testTenant2");
        Tenant testTenant3=new Tenant("testTenant3");


        Role testRole = new Role("role1", testTenant1);
        test1SecurityContext =new SecurityContext(testUser1,List.of(testTenant1,testTenant2),List.of(testRole),null);
        test2SecurityContext =new SecurityContext(testUser2,List.of(testTenant2),List.of(),null);
        test3SecurityContext =new SecurityContext(testUser3,List.of(testTenant3),List.of(),null);
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
/**
    @Test
    @Order(2)
    public void testForUser() {
        TestEntity forUser = testEntityService.createTestEntityNoMerge(new TestEntityCreate().setName("forUser"), adminSecurityContext);
        forUser.getSecurity().setTenant(testTenant);
        testEntityService.merge(forUser);
        userToBaseclassService.createUserToBaseclass(new UserToBaseclassCreate().setUser(testUser).setAccess(IOperation.Access.allow).setBaseclass(forUser.getSecurity()),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forUser.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(3)
    public void testForRole() {
        TestEntity forRole = testEntityService.createTestEntity(new TestEntityCreate().setName("forRole"), adminSecurityContext);
        forRole.getSecurity().setTenant(testTenant);
        testEntityService.merge(forRole);
        roleToBaseclassService.createRoleToBaseclass(new RoleToBaseclassCreate().setRole(testRole).setAccess(IOperation.Access.allow).setBaseclass(forRole.getSecurity()),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forRole.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(4)
    public void testForTenant() {
        TestEntity forTenant = testEntityService.createTestEntity(new TestEntityCreate().setName("forTenant"), adminSecurityContext);

        tenantToBaseclassService.createTenantToBaseclass(new TenantToBaseclassCreate().setTenant(testTenant).setAccess(IOperation.Access.allow).setBaseclass(forTenant.getSecurity()),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forTenant.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }




    @Test
    @Order(5)
    public void testPermissionGroupForUser() {
        PermissionGroup forUserGroup = permissionGroupService.createPermissionGroup(new PermissionGroupCreate().setName("forUserGroup"), null);
        TestEntity forUser = testEntityService.createTestEntity(new TestEntityCreate().setName("forUser"), adminSecurityContext);
        PermissionGroupToBaseclass permissionGroupToBaseclass = permissionGroupToBaseclassService.createPermissionGroupToBaseclass(new PermissionGroupToBaseclassCreate().setPermissionGroup(forUserGroup).setBaseclass(forUser.getSecurity()), null);
        userToBaseclassService.createUserToBaseclass(new UserToBaseclassCreate().setUser(testUser).setAccess(IOperation.Access.allow).setPermissionGroup(forUserGroup),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forUser.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(6)
    public void testPermissionGroupForRole() {
        PermissionGroup forRoleGroup = permissionGroupService.createPermissionGroup(new PermissionGroupCreate().setName("forRoleGroup"), null);
        TestEntity forRole = testEntityService.createTestEntity(new TestEntityCreate().setName("forRole"), adminSecurityContext);
        PermissionGroupToBaseclass permissionGroupToBaseclass = permissionGroupToBaseclassService.createPermissionGroupToBaseclass(new PermissionGroupToBaseclassCreate().setPermissionGroup(forRoleGroup).setBaseclass(forRole.getSecurity()), null);
        roleToBaseclassService.createRoleToBaseclass(new RoleToBaseclassCreate().setRole(testRole).setAccess(IOperation.Access.allow).setPermissionGroup(forRoleGroup),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forRole.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));
    }

    @Test
    @Order(7)
    public void testPermissionGroupForTenant() {
        PermissionGroup forTenantGroup = permissionGroupService.createPermissionGroup(new PermissionGroupCreate().setName("forTenantGroup"), null);
        TestEntity forTenant = testEntityService.createTestEntity(new TestEntityCreate().setName("forTenant"), adminSecurityContext);
        PermissionGroupToBaseclass permissionGroupToBaseclass = permissionGroupToBaseclassService.createPermissionGroupToBaseclass(new PermissionGroupToBaseclassCreate().setPermissionGroup(forTenantGroup).setBaseclass(forTenant.getSecurity()), null);
        tenantToBaseclassService.createTenantToBaseclass(new TenantToBaseclassCreate().setTenant(testTenant).setAccess(IOperation.Access.allow).setPermissionGroup(forTenantGroup),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forTenant.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(8)
    public void testUserToClazz() {
        // create a fresh user and link it to a tenant
        SecurityUser freshUser = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUser"), null);
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUser).setDefaultTenant(true).setTenant(freshTenant),null);

        // validate it has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUser));
        Assertions.assertTrue(testEntities.isEmpty());

        // grant it access for clazz
        userToBaseclassService.createUserToBaseclass(new UserToBaseclassCreate().setUser(freshUser).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(TestEntity.class.getCanonicalName())),null);

        // validate it has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUser));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));

    }

    @Test
    @Order(9)
    public void testGrantAccessForRole() {
        // create a fresh role, user and link them to a tenant
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant2"), null);

        Role freshRole = roleService.createRole(new RoleCreate().setTenant(freshTenant).setName("freshRole"), null);

        SecurityUser freshUserWithRole = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUserWithRole"), null);
        roleToUserService.createRoleToUser(new RoleToUserCreate().setRole(freshRole).setSecurityUser(freshUserWithRole), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUserWithRole).setDefaultTenant(true).setTenant(freshTenant),null);

        // validate the user with role has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserWithRole));
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the role access for clazz
        roleToBaseclassService.createRoleToBaseclass(new RoleToBaseclassCreate().setRole(freshRole).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(TestEntity.class.getCanonicalName())),null);

        // validate the user with role has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserWithRole));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));
        //validate role does not have access to Test Entities in other tenant which he belongs to
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUserWithRole).setTenant(adminSecurityContext.getTenantToCreateIn()),null);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserWithRole));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));


    }

    @Test
    @Order(10)
    public void testGrantAccessForTenant() {
        // create a fresh tenant and a user linked to it
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant"), null);
        SecurityUser freshUserInTenant = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUserInTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUserInTenant).setDefaultTenant(true).setTenant(freshTenant),null);

        // validate the user in the tenant has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserInTenant));
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the tenant access for clazz
        tenantToBaseclassService.createTenantToBaseclass(new TenantToBaseclassCreate().setTenant(freshTenant).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(TestEntity.class.getCanonicalName())),null);

        // validate the user in the tenant has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserInTenant));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));
    }

    @Test
    @Order(8)
    public void testUserToAll() {
        // create a fresh user and link it to a tenant
        SecurityUser freshUser = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUser"), null);
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUser).setDefaultTenant(true).setTenant(freshTenant),null);

        // validate it has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUser));
        Assertions.assertTrue(testEntities.isEmpty());

        // grant it access for clazz
        userToBaseclassService.createUserToBaseclass(new UserToBaseclassCreate().setUser(freshUser).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(SecurityWildcard.class.getCanonicalName())),null);

        // validate it has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUser));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));

    }

    @Test
    @Order(9)
    public void testRoleToAll() {
        // create a fresh role, user and link them to a tenant
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant2"), null);

        Role freshRole = roleService.createRole(new RoleCreate().setTenant(freshTenant).setName("freshRole"), null);

        SecurityUser freshUserWithRole = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUserWithRole"), null);
        roleToUserService.createRoleToUser(new RoleToUserCreate().setRole(freshRole).setSecurityUser(freshUserWithRole), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUserWithRole).setDefaultTenant(true).setTenant(freshTenant),null);

        // validate the user with role has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserWithRole));
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the role access for clazz
        roleToBaseclassService.createRoleToBaseclass(new RoleToBaseclassCreate().setRole(freshRole).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(SecurityWildcard.class.getCanonicalName())),null);

        // validate the user with role has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserWithRole));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));
        //validate role does not have access to Test Entities in other tenant which he belongs to
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUserWithRole).setTenant(adminSecurityContext.getTenantToCreateIn()),null);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserWithRole));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));


    }

    @Test
    @Order(10)
    public void testTenantToAll() {
        // create a fresh tenant and a user linked to it
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant"), null);
        SecurityUser freshUserInTenant = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUserInTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUserInTenant).setDefaultTenant(true).setTenant(freshTenant),null);

        // validate the user in the tenant has access to nothing
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserInTenant));
        Assertions.assertTrue(testEntities.isEmpty());

        // grant the tenant access for clazz
        tenantToBaseclassService.createTenantToBaseclass(new TenantToBaseclassCreate().setTenant(freshTenant).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(SecurityWildcard.class.getCanonicalName())),null);

        // validate the user in the tenant has access to all TestEntities in the tenant
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContextProvider.getSecurityContext(freshUserInTenant));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInOtherTenantIds.contains(f.getId())));
    }

    @Test
    @Order(11)
    public void testAccessDenyForUser() {
        SecurityUser freshUser = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUser"), null);
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUser).setDefaultTenant(true).setTenant(freshTenant),null);
        userToBaseclassService.createUserToBaseclass(new UserToBaseclassCreate().setUser(freshUser).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(SecurityWildcard.class.getCanonicalName())),null);

        // validate it has access to nothing
        SecurityContextBase securityContext = securityContextProvider.getSecurityContext(freshUser);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

        // grant it access for clazz
        TestEntity toDeny = testEntityService.findByIdOrNull(TestEntity.class, othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found")));
        userToBaseclassService.createUserToBaseclass(new UserToBaseclassCreate().setUser(testUser).setAccess(IOperation.Access.deny).setBaseclass(toDeny.getSecurity()),null);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny.getId())));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(12)
    public void testAccessDenyForRole() {
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant"), null);
        Role freshRole = roleService.createRole(new RoleCreate().setTenant(freshTenant).setName("freshRole"), null);

        SecurityUser freshUserWithRole = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUserWithRole"), null);
        roleToUserService.createRoleToUser(new RoleToUserCreate().setRole(freshRole).setSecurityUser(freshUserWithRole), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUserWithRole).setDefaultTenant(true).setTenant(freshTenant),null);
        roleToBaseclassService.createRoleToBaseclass(new RoleToBaseclassCreate().setRole(freshRole).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(SecurityWildcard.class.getCanonicalName())),null);

        // validate it has access to nothing
        SecurityContextBase securityContext = securityContextProvider.getSecurityContext(freshUserWithRole);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

        // grant it access for clazz
        TestEntity toDeny = testEntityService.findByIdOrNull(TestEntity.class, othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found")));
        roleToBaseclassService.createRoleToBaseclass(new RoleToBaseclassCreate().setRole(freshRole).setAccess(IOperation.Access.deny).setBaseclass(toDeny.getSecurity()),null);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny.getId())));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
    }

    @Test
    @Order(13)
    public void testAccessDenyForTenant() {
        SecurityUser freshUser = securityUserService.createSecurityUser(new SecurityUserCreate().setName("freshUser"), null);
        SecurityTenant freshTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("freshTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(freshUser).setDefaultTenant(true).setTenant(freshTenant),null);
        tenantToBaseclassService.createTenantToBaseclass(new TenantToBaseclassCreate().setTenant(freshTenant).setAccess(IOperation.Access.allow).setClazz(Baseclass.getClazzByName(SecurityWildcard.class.getCanonicalName())),null);

        // validate it has access to nothing
        SecurityContextBase securityContext = securityContextProvider.getSecurityContext(freshUser);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));

        // grant it access for clazz
        TestEntity toDeny = testEntityService.findByIdOrNull(TestEntity.class, othersInTenantIds.stream().findFirst().orElseThrow(() -> new RuntimeException("no test entity found")));
        tenantToBaseclassService.createTenantToBaseclass(new TenantToBaseclassCreate().setTenant(freshTenant).setAccess(IOperation.Access.deny).setBaseclass(toDeny.getSecurity()),null);
        testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), securityContext);
        Assertions.assertTrue(testEntities.stream().noneMatch(f->f.getId().equals(toDeny.getId())));
        Assertions.assertTrue(testEntities.stream().allMatch(f->othersInTenantIds.contains(f.getId())));
    }
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
