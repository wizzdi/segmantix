package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.Basic;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;




public class MergingRepository  implements SegmantixRepository {
    private static final Logger logger = LoggerFactory.getLogger(MergingRepository.class);
    private final EntityManager em;

    public MergingRepository(EntityManager em) {
        this.em = em;
    }



    public <T> MergeResult<T> merge(T base, boolean updateDate) {
        Basic base1;
        boolean created;
        if (base instanceof Basic) {
            OffsetDateTime now = OffsetDateTime.now();
            base1 = (Basic) base;
            created = base1.getUpdateDate() == null;
            if (updateDate) {
                base1.setUpdateDate(now);
            }
            if (created) {
                base1.setCreationDate(now);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("merging " + base1.getId() + " updateDate flag is " + updateDate + " update date " + base1.getUpdateDate());
            }


        }

        T merged = em.merge(base);

        return new MergeResult<>(merged);

    }


    public MassMergeResult massMerge(List<?> toMerge, boolean updatedate) {
        List<Object> merged=new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();
        for (Object o : toMerge) {
            if (o instanceof Basic) {
                Basic baseclass = (Basic) o;
                boolean created = baseclass.getUpdateDate() == null;
                if (updatedate) {
                    baseclass.setUpdateDate(now);
                }
                if (created) {
                    baseclass.setCreationDate(now);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("merging " + baseclass.getId() + " updateDate flag is " + updatedate + " update date is " + baseclass.getUpdateDate());
                }

            }

            merged.add(em.merge(o));
        }

        return new MassMergeResult(merged);

    }
    public record MassMergeResult(List<?> merged){}

    public record MergeResult<T>(T merged){}
}
