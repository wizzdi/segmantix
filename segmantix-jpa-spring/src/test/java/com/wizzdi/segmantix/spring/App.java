package com.wizzdi.segmantix.spring;


import com.wizzdi.segmantix.spring.annotations.EnableSegmantix;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@SpringBootApplication
@EnableSegmantix
@EntityScan(basePackageClasses=TestEntity.class)
public class App {


	private static final Logger logger= LoggerFactory.getLogger(App.class);
	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(App.class);
		app.addListeners(new ApplicationPidFileWriter());
		ConfigurableApplicationContext context=app.run(args);

	}





	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
			System.out.println("total of "+beanNames.length +" beans");



		};
	}
}
