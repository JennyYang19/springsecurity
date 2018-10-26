package com.example.securitycore;

import com.example.securitycore.propertites.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)  //让配置的读取器生效
public class SecurityCoreConfig {
}
