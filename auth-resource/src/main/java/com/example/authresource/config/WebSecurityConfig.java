package com.example.authresource.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/*/**")
                .permitAll();


//                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage( "/login" ).failureUrl( "/login-error" )
//                .and()
//                .exceptionHandling().accessDeniedPage( "/401" );
//        http.logout().logoutSuccessUrl( "/" );
    }


}
