package com.gmail.antoniomarkoski314.Chat.configurations;

import com.gmail.antoniomarkoski314.Chat.Properties;
import com.gmail.antoniomarkoski314.Chat.filters.BasicAuthFilter;
import com.gmail.antoniomarkoski314.Chat.filters.UsernamePasswordAuthFilter;
import com.gmail.antoniomarkoski314.Chat.services.userdetails.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsServiceImpl;

    public SecurityConfigurer(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider()).eraseCredentials(false);
        auth.userDetailsService(this.userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // For H2 database console
        http.headers().frameOptions().sameOrigin();

        // Configure cross origin
        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowCredentials(true);
            cors.setAllowedOrigins(List.of("http://localhost:4200"));
            cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(List.of("*"));
            cors.addExposedHeader(Properties.AUTHENTICATION_HEADER);
            cors.addExposedHeader(Properties.AUTHORIZATION_HEADER);
            return cors;
        });
        // remove csrf and state in session
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Add authentication filter
        http.addFilter(new UsernamePasswordAuthFilter(authenticationManager(), this.userDetailsServiceImpl ));
        // Add authorization filter
        http.addFilter(new BasicAuthFilter(authenticationManager(), this.userDetailsServiceImpl));

        http.authorizeRequests()
                .antMatchers(Properties.errorUrl).permitAll()
                .antMatchers(Properties.authenticateUrl).permitAll()
                .antMatchers(Properties.registerUrl).permitAll()
                .antMatchers(Properties.getUsersUrl).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers(Properties.socketUrl).permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(this.userDetailsServiceImpl);
        return provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
