package web.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.handler.LoginSuccessHandler;

@Configuration
@ComponentScan("web")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(@Qualifier("userServiceImpl") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/admin").hasAnyAuthority("admin")
                    .antMatchers("/rest/**").hasAnyAuthority("admin")
                    .antMatchers("/user").hasAnyAuthority("user")
                    .antMatchers("/login").permitAll()
                    .antMatchers("/").authenticated()
                    .antMatchers("/*.css").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
                    .loginPage("/login").permitAll()
                    .usernameParameter("name")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())
                .and()
                .exceptionHandling()
                .accessDeniedPage("/");
    }


}
