package com.excilys.cdb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@ComponentScan("com.excilys.cdb")
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration  extends GlobalMethodSecurityConfiguration{

}
