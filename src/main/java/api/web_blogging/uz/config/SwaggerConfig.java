package api.web_blogging.uz.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // general info
        Info info = new Info()
                .title("Title for Swagger Docs")
                .version("1.0.0")
                .description("Description for Swagger Docs.")
                .contact(new Contact()
                        .name("Author")
                        .email("author@gmail.com")
                        .url("https://t.me/author")
                )
                .license(new License()
                        .name("author.uz")
                        .url("https://author.uz/")
                )
                .termsOfService("Savol javob guruhi: https://t.me/author_group");

        // servers (ishlatiladigan serverlar)
        Server server1 = new Server()
                .description("Local")
                .url("http://localhost:9999");

        Server server2 = new Server()
                .description("DEV")
                .url("http://api.domain.uz");

        Server server3 = new Server()
                .description("PROD")
                .url("http://api.domain.uz");

        // security type (bizning holatda JWT)
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("bearerAuth");

        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setName("bearerAuth");
        securityScheme.setType(SecurityScheme.Type.HTTP);
        securityScheme.bearerFormat("JWT");
        securityScheme.setIn(SecurityScheme.In.HEADER);
        securityScheme.setScheme("bearer");

        Components components = new Components();
        components.addSecuritySchemes("bearerAuth", securityScheme);

        // collect all together
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);
        openAPI.setServers(List.of(server1, server2, server3));
        openAPI.setSecurity(List.of(securityRequirement));
        openAPI.components(components);

        // return-xe
        return openAPI;
    }
}
