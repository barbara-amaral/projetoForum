package com.projetoforum.forum.config.swagger;

import com.projetoforum.forum.model.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

@Configuration
public class SwaggerConfigurations {
    @Bean
    public Docket forumApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.projetoforum.forum"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .ignoredParameterTypes(Usuario.class)
                .globalOperationParameters(Arrays.asList(
                        new ParameterBuilder()
                                .name("Authorization")
                                .description("Header para token JWT: Cole seu token aqui.")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false).defaultValue("Bearer ")
                                .build()));
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Fórum API")
                .description("Fórum de perguntas e respostas.")
                .build();
    }
}
