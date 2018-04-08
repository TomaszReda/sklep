package pl.tomek.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import pl.tomek.service.UserDetailImplement;

@Configuration
public class security extends WebSecurityConfigurerAdapter {


    @Bean
    public UserDetailsService userDetailsService()
    {
        return new UserDetailImplement();
    }




    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/search").permitAll()
                .antMatchers("/searchh").permitAll()
                .antMatchers("/detailsSearch").permitAll()
                .antMatchers("/terms").permitAll()
                .antMatchers("/privacy").permitAll()
                .antMatchers("/api").permitAll()
                .antMatchers("/api/products").permitAll()
                .antMatchers("/api/users").permitAll()
                .antMatchers("/api/userRoles").permitAll()
                .antMatchers("/api/products/{id}").permitAll()
                .antMatchers("/api/users/{id}").permitAll()
                .antMatchers("/api/userRoles/{id}").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/loginform")
                .permitAll()
                .loginProcessingUrl("/processinglogin")
                .permitAll()
                .usernameParameter("user")
                .passwordParameter("pass")
                .failureUrl("/formError")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logmeout")
                .logoutSuccessUrl("/")
                .permitAll();

        ;

    }
}
