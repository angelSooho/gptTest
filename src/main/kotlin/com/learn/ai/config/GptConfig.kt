package com.learn.ai.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableConfigurationProperties(value = [GptProperties::class])
class GptConfig(
    val gptProperties: GptProperties,
) {

        @Bean
        fun restTemplate(): RestTemplate {
            return RestTemplate()
        }
}