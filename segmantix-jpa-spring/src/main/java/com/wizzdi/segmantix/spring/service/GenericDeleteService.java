package com.wizzdi.segmantix.spring.service;


import com.wizzdi.segmantix.spring.data.GenericDeleteRepository;
import com.wizzdi.segmantix.spring.request.DeleteObjectRequest;
import com.wizzdi.segmantix.spring.request.DeleteObjectsRequest;
import com.wizzdi.segmantix.spring.response.DeleteResponse;
import com.wizzdi.segmantix.store.jpa.model.Basic;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GenericDeleteService {

    @Autowired
    private GenericDeleteRepository genericDeleteRepository;

    public void validate(DeleteObjectsRequest deleteObjectsRequest, SecurityContext securityContext) {
        if(deleteObjectsRequest.getEntries()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"entries field cannot be null");
        }
        Map<String, Map<String, DeleteObjectRequest>> deleteMap=deleteObjectsRequest.getEntries().stream().collect(Collectors.groupingBy(f->f.getType(),Collectors.toMap(f->f.getId(), f->f,(a, b)->a)));
        for (Map.Entry<String, Map<String, DeleteObjectRequest>> entry : deleteMap.entrySet()) {
            String className = entry.getKey();
                Class<?> type =genericDeleteRepository.getType(className);
                if(type==null){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no class "+className);
                }
                if(!Basic.class.isAssignableFrom(type)){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"type "+type+" does not extend Basic");
                }
                Map<String, DeleteObjectRequest> value = entry.getValue();
                List<? extends Basic> objects = genericDeleteRepository.getObjects((Class<? extends Basic>) type, value.keySet(), securityContext);
                for (Basic object : objects) {
                    DeleteObjectRequest deleteObjectRequest = value.get(object.getId());
                    if(deleteObjectRequest!=null){
                        deleteObjectRequest.setBasic(object);
                    }
                }



        }
    }

    public DeleteResponse softDelete(DeleteObjectsRequest deleteObjectsRequest, SecurityContext securityContext) {
        List<Object> deleted=new ArrayList<>();
        List<DeleteObjectRequest> failed=new ArrayList<>();
        for (DeleteObjectRequest entry : deleteObjectsRequest.getEntries()) {
            Basic basic = entry.getBasic();
            if(basic !=null){
                basic.setSoftDelete(true);
                genericDeleteRepository.merge(basic);
                deleted.add(basic);
            }
            else{
                failed.add(entry);
            }
        }
        return new DeleteResponse().setDeleted(deleted).setFailed(failed);
    }
}
