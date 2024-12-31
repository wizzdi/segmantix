package com.wizzdi.segmantix.store.jpa.data;


import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.store.jpa.request.ClazzFilter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ClazzRepository implements SegmantixRepository {

    private final Map<String, Clazz> entities = new ConcurrentHashMap<>();

    public void add(Clazz clazz) {
        entities.put(clazz.name(), clazz);
    }


    public List<Clazz> listAllClazzs(ClazzFilter clazzFilter) {
        return streamClazz(clazzFilter).sorted(Comparator.comparing(f -> f.name())).collect(Collectors.toList());

    }

    private Stream<Clazz> streamClazz(ClazzFilter clazzFilter) {
        return entities.values().stream().filter(f -> filter(f, clazzFilter.getBasicPropertiesFilter()));
    }

    private boolean filter(Clazz clazz, BasicPropertiesFilter basicPropertiesFilter) {
        if (basicPropertiesFilter == null) {
            return true;
        }
        boolean pass = true;
        if (basicPropertiesFilter.getNameLike() != null) {
            String like = basicPropertiesFilter.getNameLike().replace("%", "");
            if (basicPropertiesFilter.isNameLikeCaseSensitive()) {
                pass = pass && clazz.name().contains(like);
            } else {

                pass = pass && clazz.name().toLowerCase().contains(like.toLowerCase());
            }
        }
        if (basicPropertiesFilter.getNames() != null && !basicPropertiesFilter.getNames().isEmpty()) {
            pass = pass && basicPropertiesFilter.getNames().contains(clazz.name());
        }

        if (basicPropertiesFilter.getOnlyIds() != null && !basicPropertiesFilter.getOnlyIds().isEmpty()) {
            pass = pass && basicPropertiesFilter.getOnlyIds().contains(clazz.name());
        }
        return pass;

    }


    public long countAllClazzs(ClazzFilter clazzFilter) {
        return streamClazz(clazzFilter).count();

    }
}
