package com.wizzdi.segmantix.jpa.store.spring.init;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.wizzdi.segmantix.jpa.store.spring")
@EnableTransactionManagement(proxyTargetClass = true)
public class SegmantixModule {

}
