package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.model.ISecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


public class TestEntityService {

    @Autowired
    private TestEntityRepository testEntityRepository;


    public TestEntity createTestEntity(TestEntityCreate testEntityCreate, ISecurityContext securityContext){
        TestEntity testEntity=createTestEntityNoMerge(testEntityCreate,securityContext);
        testEntityRepository.merge(testEntity);
        return testEntity;

    }

    public TestEntity createTestEntityNoMerge(TestEntityCreate testEntityCreate, ISecurityContext securityContext) {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(UUID.randomUUID().toString());
        updateTestEntityNoMerge(testEntityCreate, testEntity);
        testEntity.setCreatorId(securityContext.user().getId());
        testEntity.setTenantId( securityContext.tenants().getFirst().getId());

        return testEntity;
    }

    public boolean updateTestEntityNoMerge(TestEntityCreate testEntityCreate, TestEntity testEntity) {
        boolean update=false;
        if(testEntityCreate.getDescription()!=null&&!testEntityCreate.getDescription().equals(testEntity.getDescription())){
            testEntity.setDescription(testEntityCreate.getDescription());
            update=true;
        }

        if(testEntityCreate.getName()!=null&&!testEntityCreate.getName().equals(testEntity.getName())){
            testEntity.setName(testEntityCreate.getName());
            update=true;
        }
        if(testEntityCreate.getPermissionGroupId()!=null&&!testEntityCreate.getPermissionGroupId().equals(testEntity.getPermissionGroupId())){
            testEntity.setPermissionGroupId(testEntityCreate.getPermissionGroupId());
            update=true;
        }
        return update;
    }

    public List<TestEntity> listAllTestEntities(TestEntityFilter filtering, ISecurityContext securityContext) {
        return testEntityRepository.listAllTestEntities(filtering, securityContext);
    }





    @Transactional
    public void merge(Object base) {
        testEntityRepository.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        testEntityRepository.massMerge(toMerge);
    }
}
