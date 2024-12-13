package com.wizzdi.segmantix.jpa.store.spring.configuration;



import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration

public class AdminConfiguration  {
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean(name = "systemAdminId")
	@Qualifier("systemAdminId")
	public String systemAdminId(){
		return "UEKbB6XlQhKOtjziJoUQ8w";
	}

}
