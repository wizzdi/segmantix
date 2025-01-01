package com.wizzdi.segmantix.spring;

import com.wizzdi.segmantix.store.jpa.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.store.jpa.service.BaseclassService;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
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
    @Autowired
    private BaseclassService baseclassService;


    public TestEntity createTestEntity(TestEntityCreate testEntityCreate, SecurityContext securityContext){
        TestEntity testEntity=createTestEntityNoMerge(testEntityCreate,securityContext);
        testEntityRepository.merge(testEntity);
        return testEntity;

    }

    public TestEntity createTestEntityNoMerge(TestEntityCreate testEntityCreate, SecurityContext securityContext) {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(UUID.randomUUID().toString());
        updateTestEntityNoMerge(testEntityCreate, testEntity);
        BaseclassService.createSecurityObjectNoMerge(testEntity,securityContext);

        return testEntity;
    }

    public boolean updateTestEntityNoMerge(TestEntityCreate testEntityCreate, TestEntity testEntity) {
        boolean update=baseclassService.updateBaseclassNoMerge(testEntityCreate,testEntity);
            testEntity.setDescription(testEntityCreate.getDescription());
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

    public TestEntity findByIdOrNull( String id) {
        return listAllTestEntities(new TestEntityFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Set.of(id))),null).stream().findFirst().orElse(null);
    }
}
