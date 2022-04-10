package com.udemy.openapidemo.config

import org.springdoc.core.SpringDocConfigProperties
import org.springdoc.core.SpringDocConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun springDocConfiguration(): SpringDocConfiguration = SpringDocConfiguration()

    @Bean
    fun springDocConfigProperties(): SpringDocConfigProperties = SpringDocConfigProperties()
}