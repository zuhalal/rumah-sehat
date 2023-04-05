package com.tugaskelompok.rumahsehat.config.security;

import com.tugaskelompok.rumahsehat.config.auth.JwtAuthenticationEntryPoint;
import com.tugaskelompok.rumahsehat.config.auth.JwtRequestFilter;
import com.tugaskelompok.rumahsehat.user.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.http.SessionCreationPolicy;

// taken from https://stackoverflow.com/questions/35761181/securing-only-rest-controller-with-spring-security-custom-token-stateless-while
@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Configuration
    @Order(2)
    public class UILoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/login-sso", "/validate-ticket").permitAll()
                .antMatchers("/admin/**").hasAuthority(UserRole.ADMIN.toString())
                .antMatchers("/dokter/**", "/apoteker/**").hasAuthority(UserRole.ADMIN.toString())
                .antMatchers("/obat/viewall").hasAnyAuthority(UserRole.ADMIN.toString(), UserRole.APOTEKER.toString())
                .antMatchers("/resep/add/{kode}").hasAuthority(UserRole.DOKTER.toString())
                .antMatchers( "/resep/detail/**").hasAnyAuthority(UserRole.APOTEKER.toString(), UserRole.ADMIN.toString(), UserRole.DOKTER.toString())
                .antMatchers("/resep/confirm/**").hasAuthority(UserRole.APOTEKER.toString())
                .antMatchers("/resep/viewall").hasAnyAuthority(UserRole.APOTEKER.toString(), UserRole.ADMIN.toString())
                .antMatchers( "/appointment/**").hasAnyAuthority( UserRole.ADMIN.toString(), UserRole.DOKTER.toString())
                .antMatchers("/obat/chart", "/obat/chart/data").hasAuthority(UserRole.ADMIN.toString())
                .antMatchers("/obat/barchart-obat", "/obat/chart/barchart").hasAuthority(UserRole.ADMIN.toString())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll().and()
                .sessionManagement().sessionFixation().newSession().maximumSessions(1);
        }
    }

    // taken from https://www.javainuse.com/spring/boot-jwt-mysql
    @Configuration
    @Order(1)
    public  class RestApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            // We don't need CSRF for this example
            httpSecurity.csrf().disable()
                    .antMatcher("/api/v1/**").cors().and()
                    // dont authenticate this particular request
                    .authorizeRequests()
                    .antMatchers("/api/v1/auth/login").permitAll()
                    .antMatchers("/api/v1/auth/regis").permitAll()
                    .antMatchers("/api/v1/**").hasAuthority("PASIEN")
                    .and()
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // Add a filter to validate the tokens with every request
            httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }

    public BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }
}
