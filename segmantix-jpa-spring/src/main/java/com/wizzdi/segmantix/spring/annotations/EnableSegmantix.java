package com.wizzdi.segmantix.spring.annotations;

import com.wizzdi.segmantix.spring.config.SegmantixSpringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(SegmantixSpringConfig.class)
@Configuration
public @interface EnableSegmantix {
}
