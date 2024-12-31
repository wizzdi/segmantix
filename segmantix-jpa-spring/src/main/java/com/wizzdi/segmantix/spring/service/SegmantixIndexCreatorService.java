package com.wizzdi.segmantix.spring.service;

import com.wizzdi.segmantix.spring.data.SegmantixIndexCreatorRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SegmantixIndexCreatorService implements InitializingBean {
    private final SegmantixIndexCreatorRepository segmantixIndexCreatorRepository;
    private final boolean recreateIndexes;

    public SegmantixIndexCreatorService(SegmantixIndexCreatorRepository segmantixIndexCreatorRepository, @Value("${segmantix.indexes.recreate:false}") boolean recreateIndexes) {
        this.segmantixIndexCreatorRepository = segmantixIndexCreatorRepository;
        this.recreateIndexes=recreateIndexes;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
       segmantixIndexCreatorRepository.createIndexes(recreateIndexes);
    }
}
