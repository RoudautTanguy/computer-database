package com.excilys.cdb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import com.excilys.cdb.service.ServiceUser;

@Configuration
@EnableWebSecurity
@ComponentScan("com.excilys.cdb")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	private ServiceUser serviceUser;
	
	@Autowired
	public void setUserDetailsService(ServiceUser serviceUser) {
	    this.serviceUser = serviceUser;
	}

	@Override
    protected void configure(HttpSecurity http) throws Exception {
            http.addFilter(digestAuthenticationFilter())
            .exceptionHandling().authenticationEntryPoint(digestEntryPoint())
            .and()
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers( "/login").permitAll()
            .anyRequest().authenticated();
    }
	
    DigestAuthenticationFilter digestAuthenticationFilter() {
        DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
        digestAuthenticationFilter.setUserDetailsService(userDetailsService());
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());
        return digestAuthenticationFilter;
    }
    
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
      return serviceUser;
    }

    @Bean
    DigestAuthenticationEntryPoint digestEntryPoint() {
        DigestAuthenticationEntryPoint bauth = new DigestAuthenticationEntryPoint();
        bauth.setRealmName("Computer Database Realm");
        bauth.setKey("CDB-Key");
        return bauth;
    }
    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}
