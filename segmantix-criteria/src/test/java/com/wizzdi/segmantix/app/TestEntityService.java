package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.impl.model.SecurityTenant;
import com.wizzdi.segmantix.impl.model.SecurityUser;
import com.wizzdi.segmantix.service.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class TestEntityService {

    @Autowired
    private TestEntityRepository testEntityRepository;


    public TestEntity createTestEntity(TestEntityCreate testEntityCreate, SecurityContext securityContextBase){
        TestEntity testEntity=createTestEntityNoMerge(testEntityCreate,securityContextBase);
        testEntityRepository.merge(testEntity);
        return testEntity;

    }

    public TestEntity createTestEntityNoMerge(TestEntityCreate testEntityCreate, SecurityContext securityContextBase) {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(UUID.randomUUID().toString());
        updateTestEntityNoMerge(testEntityCreate, testEntity);
        testEntity.setCreatorId(securityContextBase.securityUser().getId());
        testEntity.setTenantId( securityContextBase.tenants().getFirst().getId());

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
        return update;
    }

    public List<TestEntity> listAllTestEntities(TestEntityFilter filtering, SecurityContext securityContext) {
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
