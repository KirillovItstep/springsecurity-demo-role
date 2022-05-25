package lab01.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Можно задать разрешения в контроллере, а можно здесь
        http.authorizeRequests()
                    .antMatchers("/test/home").permitAll()
                    .antMatchers("/test/admin").hasRole("ADMIN")
                    .antMatchers("/test/user").access("hasRole('USER')")

                    .anyRequest().authenticated()
                .and()

                .formLogin()
//                    .loginPage("/login")
                    .permitAll()
                .and()

                .logout()
//                    .logoutUrl("/logout")
                    .permitAll();
    }

    @Bean
    public UserDetailsService userDetailsService()
    {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        User.UserBuilder userBuilder = User.builder().passwordEncoder(encoder::encode);
        UserDetails user = userBuilder.username("user").password("user")
                .roles("USER").build();
        UserDetails admin = userBuilder.username("admin").password("admin")
                .roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

}
