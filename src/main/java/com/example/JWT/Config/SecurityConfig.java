package com.example.JWT.Config;

import com.example.JWT.Filter.AuthFilter;
import com.example.JWT.Service.UserInfoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@Order(2) //
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
     private AuthFilter authFilter;
    @Bean
    public UserDetailsService userDetailsService(){
//        UserDetails admin = User.withUsername("Boopathi")
//                .password(encoder.encode("Pwd1"))
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withUsername("John")
//                .password(encoder.encode("Pwd2"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
        return new UserInfoUserDetailsService();
    }

  @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return  http.csrf().disable()
                .authorizeHttpRequests()
              //("/product/welcome","/product/new","/product/authenticate","/product/refreshToken",)
                .requestMatchers("/product/welcome","/product/new","/product/authenticate","/product/refreshToken","/product/forgot_password","product/reset_password","product/otp_verification").permitAll()
//              .requestMatchers("/product/all").hasRole("hasAuthority('ROLE_ADMIN')")
                .and()
                .authorizeHttpRequests().requestMatchers("/product/**").authenticated()
                .and().sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
              .authenticationProvider(authenticationProvider())
              .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
              .build();
   }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
     public AuthenticationProvider authenticationProvider(){
         DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
         authenticationProvider.setUserDetailsService(userDetailsService());
         authenticationProvider.setPasswordEncoder(passwordEncoder());
         return authenticationProvider;
     }

     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
     }



}
