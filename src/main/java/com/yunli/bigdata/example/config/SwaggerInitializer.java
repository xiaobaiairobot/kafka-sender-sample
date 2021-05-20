package com.yunli.bigdata.example.config;

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author : yunli
 */
@Component
@EnableSwagger2
public class SwaggerInitializer implements BeanFactoryAware {
  @Value("${swagger.ui.enabled:false}")
  public Boolean enabled;

  @Value("${swagger.ui.title:未知}")
  public String title;

  @Value("${swagger.ui.version:未知}")
  public String version;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
    if (enabled) {
      configurableBeanFactory.registerSingleton("docket", new Docket(DocumentationType.SWAGGER_2)
          .apiInfo(new ApiInfoBuilder().title(title).version(version).build()).select()
          .apis(RequestHandlerSelectors.any())
          .paths(Predicates.not(PathSelectors.regex("/error.*")))
          .paths(PathSelectors.any())
          .build()
          .globalOperationParameters(Arrays.asList(new ParameterBuilder().name("x-token").description("login token")
              .modelRef(new ModelRef("string")).parameterType("header").required(false).build())));
    }
  }
}
