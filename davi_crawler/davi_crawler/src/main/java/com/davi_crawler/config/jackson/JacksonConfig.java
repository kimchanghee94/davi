package com.davi_crawler.config.jackson;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    //Service절에서 objectMapper따로 strategy case 처리하는 방법이 더 동적이여서 해당 설정 주석처리
//    @Bean
//    public ObjectMapper jacksonBuilder(){
//        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
//        jackson2ObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//        return jackson2ObjectMapperBuilder.build();
//    }
}
