package com.curso.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig { //Ya no es necesario extender de WebSecurityConfigurerAdapter

    @Autowired
    private UserDetailsService userDetailService;

    //Ahora puedes especificar todas las rutas que quieras que tengan cierto nivel de seguridad en un mismo lugar.
    public static final String[] rutasPublicas = {
            "/usuario/registro/**",
            "/vendor/bootstrap/css/bootstrap.min.css",
            "/css/heroic-features.css",
    };
    public static final String[] rutasAdmin = {
    		"/administrador/**",
            "/productos/**",
    };
    /*
    public static final String[] rutasUsuarioComun = {
            "/api/v1/productos/**",
            "/api/v1/carrito/**",
            "/api/v1/pagos/**",
    };*/

    //Aquí defines tu @Bean de tipo UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userDetailService.loadUserByUsername(username);
    }

    //Aquí defines tu @Bean de tipo PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Aquí defines tu @Bean de tipo AuthenticationProvider, pasando como argumento el UserDetailsService y el PasswordEncoder que definiste arriba
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService()); // <-- Aquí pasas el UserDetailsService
        provider.setPasswordEncoder(passwordEncoder()); // <-- Aquí pasas el PasswordEncoder
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /* Como ya no es necesario extender de WebSecurityConfigurerAdapter, no se deben sobreescribir los métodos

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(getEnecoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/administrador/**").hasRole("ADMIN")
                .antMatchers("/productos/**").hasRole("ADMIN")
                .and().formLogin().loginPage("/usuario/login")
                .permitAll().defaultSuccessUrl("/usuario/acceder");
    }

    */

    /*
    Ahora Tienes que crear tu propio Bean de Security filter chain donde le indicas a Spring Boot
    que no utilice el filtro de seguridad por defecto, sino el que tu creaste.
    Además de otras configuraciones que quieras agregarle a tu filtro de seguridad.
    Por ejemplo:
    */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.csrf().disable().authorizeRequests()
		.requestMatchers("/administrador/**").hasRole("ADMIN")
		.requestMatchers("/productos/**").hasRole("ADMIN")
		.and().formLogin().loginPage("/usuario/login")
		.permitAll().defaultSuccessUrl("/usuario/acceder");

        //Finalmente construyes el filtro de seguridad http y lo devuelves
        return http.build();
    }
    
    
  

    
    
    
    /* El password encoder se definió arriba
    @Bean
    public BCryptPasswordEncoder getEnecoder() {
        return new BCryptPasswordEncoder();
    }
     */
}