package mz.org.fgh.hl7SearchApp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Value("${app.hl7SearchApp.username}") 
    private String userName;
    
    @Value("${app.hl7SearchApp.password}") 
    private String passWord;
	
	@Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username(userName)
                .password(passwordEncoder().encode(passWord))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
					.anyRequest().authenticated()
					.and()
					.formLogin(form -> form
							.loginPage("/index")
							.defaultSuccessUrl("/search", true)
							.permitAll()
					)
							.logout(logout -> logout.logoutUrl("/logout"));
	}
}
