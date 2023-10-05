package com.lamp.lantern.plugins.springmvc.autoconfigure;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.lamp.lantern.plugins.core.environment.SpringEnvironmentContext;
import com.lamp.lantern.plugins.core.login.HandlerService;

@Configuration
@EnableConfigurationProperties(LanternProperties.class)
public class AutoconfiguredLantern  implements ApplicationContextAware{

	private static final Logger logger = LoggerFactory.getLogger(AutoconfiguredLantern.class);

	private LanternProperties lanternProperties;

	private ApplicationContext applicationContext;
	
	@Bean
	public HandlerService createHandlerService() {
		HandlerService handlerService = new HandlerService();
		handlerService.setEnvironmentContext(new SpringEnvironmentContext(applicationContext));
		handlerService.createConnection(lanternProperties.getConfigMap());
		return handlerService;
	}

	public static class AutoConfiguredHandlerServiceegistrar implements ApplicationContextAware ,ImportBeanDefinitionRegistrar{
		
		private ApplicationContext applicationContext;

		@Autowired
		private LanternProperties lanternProperties;
		
		@Autowired
		private HandlerService handlerService;
		
		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
		}

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
				BeanDefinitionRegistry registry) {
			
		}
	}
	
	@org.springframework.context.annotation.Configuration
	@Import({ AutoConfiguredHandlerServiceegistrar.class })
	public static class MapperScannerRegistrarNotFoundConfiguration {

		@PostConstruct
		public void afterPropertiesSet() {
			logger.debug("Import {} found.", AutoConfiguredHandlerServiceegistrar.class.getName());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
