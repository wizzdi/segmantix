package com.wizzdi.segmantix.jpa.store.spring.response;



import com.wizzdi.segmantix.impl.model.Security;
import com.wizzdi.segmantix.impl.model.SecurityGroup;

import java.util.List;

public record SecurityGroupContainer(SecurityGroup securityGroup, List<Security> links) {
}
