package com.sunlife.ph.workflowmanagementsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${APP_BASIC_USER}")
  private String basicUser;

  @Value("${APP_BASIC_PASS}")
  private String basicPass;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/actuator/health", "/actuator/health/**", "/actuator/info").permitAll()
            .antMatchers("/edit/**").hasRole(UserRole.ADMIN.name())
            .antMatchers("/delete/**").hasRole(UserRole.ADMIN.name())
            .antMatchers("/actuator/**").hasRole(UserRole.ADMIN.name())
            .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .formLogin();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser(basicUser)
        .password("{noop}" + basicPass)
        .roles(UserRole.ADMIN.name());
  }
}
