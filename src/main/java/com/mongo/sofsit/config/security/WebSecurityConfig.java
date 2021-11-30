package com.mongo.sofsit.config.security;
import com.mongo.sofsit.config.security.jwt.JwtTokenRequestFilter;
import com.mongo.sofsit.config.security.jwt.JwtUtil;
import com.mongo.sofsit.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtUtil jwtUtil;
    private final JwtTokenRequestFilter jwtTokenRequestFilter;
    private final UserServiceImpl userDetails;
    @Autowired
    public WebSecurityConfig(JwtUtil jwtUtil, JwtTokenRequestFilter jwtTokenRequestFilter, UserServiceImpl userDetails) {
        this.jwtUtil = jwtUtil;
        this.jwtTokenRequestFilter = jwtTokenRequestFilter;
        this.userDetails = userDetails;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
            .antMatchers("/api/authorization/login").permitAll()
            .antMatchers("/api/authorization/register").permitAll()
            .anyRequest().authenticated();
        http.addFilterBefore(jwtTokenRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
