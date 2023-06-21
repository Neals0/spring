package com.sofianev2.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class ConfigurationSecurite extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MonUserDetailsService monUserDetailsService;

    @Autowired
    JwtFilter filtre;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(monUserDetailsService);

        //        auth.jdbcAuthentication()
        //                .dataSource(dataSource)
        //                .usersByUsernameQuery("SELECT email, mot_de_passe, 1 FROM utilisateur WHERE email = ?")
        //                .authoritiesByUsernameQuery(
        //                        " SELECT email, IF (admin, 'ROLE_ADMINISTRATEUR', 'ROLE_UTILISATEUR') " +
        //                        " FROM utilisateur " +
        //                        " WHERE email = ? "
        //                );

        //      .inMemoryAuthentication()
        //      .withUser("fianso")
        //      .password("root")
        //      .roles("UTILISATEUR")
        //      .and()
        //      .withUser("admin")
        //      .password("azerty")
        //      .roles("ADMINISTRATEUR");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().configurationSource(httpServletRequest -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("*")); //ajout avec Rob
                    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
                    corsConfiguration.setAllowedHeaders(
                            Arrays.asList("X-Requested-With", "Origin", "Content-Type",
                                    "Accept", "Authorization", "Access-Control-Allow-Origin"));
                    corsConfiguration.applyPermitDefaultValues();
                    return corsConfiguration;
                }).and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasAnyRole("ADMINISTRATEUR", "SUPER_ADMINISTRATEUR")
                .antMatchers("/connexion", "/inscription", "/").permitAll()
                .antMatchers("/**").hasAnyRole("ADMINISTRATEUR", "UTILISATEUR", "SUPER_ADMINISTRATEUR")

                .anyRequest().authenticated()
                .and().exceptionHandling()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(filtre, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder creationPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
