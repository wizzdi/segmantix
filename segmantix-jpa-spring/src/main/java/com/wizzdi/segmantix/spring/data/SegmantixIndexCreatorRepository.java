package com.wizzdi.segmantix.spring.data;

import com.wizzdi.segmantix.store.jpa.data.BaseclassRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SegmantixIndexCreatorRepository {
    private final BaseclassRepository baseclassRepository;

    public SegmantixIndexCreatorRepository(BaseclassRepository baseclassRepository) {
        this.baseclassRepository = baseclassRepository;
    }

    @Transactional
    public void createIndexes(boolean recreateIndexes) {
       baseclassRepository.createIndexes(recreateIndexes);
    }
}
