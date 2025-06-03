package com.example.everyhealth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EveryHealth API")
                        .version("1.0.0")
                        .description("EveryHealth의 API 문서입니다.")
                )
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("개발서버")
                ));
    }
}
