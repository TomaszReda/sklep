package pl.tomek.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/loginform")
                .permitAll()
                .loginProcessingUrl("/processlogin")
                .permitAll()
                .usernameParameter("user")
                .passwordParameter("pass")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");

        ;

    }
}
