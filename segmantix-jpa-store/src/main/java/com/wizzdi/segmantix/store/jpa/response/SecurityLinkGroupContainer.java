package com.wizzdi.segmantix.store.jpa.response;


import com.wizzdi.segmantix.store.jpa.model.SecurityLinkGroup;

import java.util.List;

public record SecurityLinkGroupContainer(SecurityLinkGroup securityLinkGroup, List<SecurityLinkContainer> links) {
}
