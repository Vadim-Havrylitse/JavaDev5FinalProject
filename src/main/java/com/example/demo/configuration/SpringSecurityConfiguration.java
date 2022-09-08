package com.example.demo.configuration;


import com.example.demo.auth.UserDetailsServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

//    @SneakyThrows
//    @Autowired
//    public void configureGlobal(BCryptPasswordEncoder passwordEncoder,
//                                UserDetailsService userDetailsService, AuthenticationManagerBuilder auth) {
//
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register", "/login").permitAll()
                .antMatchers("/note/list", "/note/create", "/note/edit").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                  //.loginPage("/login")
                  .defaultSuccessUrl("/note/list")
                  .permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();

//        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/noteUser/listUsers").access("hasRole('ROLE_ADMIN')");
//
//
//        http.authorizeRequests()
//                .antMatchers("/user/register").permitAll()
//                .antMatchers("/login").permitAll();





    }

}
