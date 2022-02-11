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
                .description("Fórum de perguntas e respostas. \n\n" +
                        "Informações importantes: \n\n" +
                        "• Para a autenticação é utilizado o token JWT, e a autenticação é do tipo Bearer. \n" +
                        "• Quase todos os métodos necessitam de autenticação, com excessão dos métodos de listar os tópicos e " +
                        "as respostas. \n• Para fazer o login e se autenticar, é preciso ter um cadastro antes. \n" +
                        "• Para testar métodos restritos, como o de listar os usuários cadastrados, é necessário ser um ADMIN. \n" +
                        "• Para cadastrar um usuário ADMIN, basta cadastrar um e-mail que termine com \"@admin.com\". " +
                        "Todos os outros recebem perfil de acesso USER. \n" +
                        "• Se quiser receber o e-mail de boas vindas, cadastre um e-mail válido. " +
                        "A aplicação também envia, todos os dias, e-mails de recomendações de tópicos baseados nos " +
                        "tópicos que você cadastrou.\n • Se não quiser mais receber os e-mails, basta deletar seu usuário.")
                .build();
    }
}
