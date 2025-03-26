package gogo.gogobetting.global.config

import gogo.gogobetting.global.security.SecurityProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(
    basePackageClasses = [
        SecurityProperties::class
    ]
)
class PropertiesScanConfig
