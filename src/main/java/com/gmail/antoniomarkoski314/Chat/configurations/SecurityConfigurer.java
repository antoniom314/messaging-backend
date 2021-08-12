package com.gmail.antoniomarkoski314.Chat.security;

import com.gmail.antoniomarkoski314.Chat.Properties;
import com.gmail.antoniomarkoski314.Chat.database.UserRepository;
import com.gmail.antoniomarkoski314.Chat.security.filters.BasicAuthFilter;
import com.gmail.antoniomarkoski314.Chat.security.filters.UsernamePasswordAuthFilter;
import com.gmail.antoniomarkoski314.Chat.security.userdetails.UserDetailsServiceImpl;
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
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsServiceImpl;
    private UserRepository userRepository;

    public SecurityConfigurer(UserDetailsServiceImpl userDetailsServiceImpl, UserRepository userRepository) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.userRepository = userRepository;
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

        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowCredentials(true);
            cors.setAllowedOrigins(List.of("http://localhost:4200"));
            cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(List.of("*"));
            cors.addExposedHeader(Properties.HEADER_STRING);
            return cors;
        });
        // remove csrf and state in session
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilter(new UsernamePasswordAuthFilter(authenticationManager(), this.userDetailsServiceImpl ));
        http.addFilter(new BasicAuthFilter(authenticationManager(), this.userDetailsServiceImpl));

//        http.requestMatchers().antMatchers("ws://localhost:8080/socket");
        http.authorizeRequests()
                .antMatchers(Properties.errorUrl).permitAll()

//                .antMatchers("/socket").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//                .antMatchers("/socket/**").permitAll()
//                .antMatchers("/app").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//                .antMatchers("/user").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//                .antMatchers("/queue").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers(Properties.authenticateUrl).permitAll()
                .antMatchers(Properties.registerUrl).permitAll()
                .antMatchers(Properties.getUsersUrl).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers(Properties.getRoleUserUrl).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers(Properties.getRoleAdminUrl).hasAuthority("ROLE_ADMIN")
                .antMatchers("/socket").permitAll()
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
