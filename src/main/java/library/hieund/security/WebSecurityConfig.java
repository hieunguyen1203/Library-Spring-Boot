package library.hieund.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

	http.csrf().disable();

	// No session will be created or used by spring security
	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	// Entry points
	http.authorizeRequests()//
		.antMatchers("/users/login").permitAll()//
		.antMatchers("/users/register").permitAll()//
		.antMatchers("/h2-console/**/**").permitAll()
		// Disallow everything else..
		.anyRequest().authenticated();

	// If a user try to access a resource without having enough permissions
//	http.exceptionHandling().accessDeniedPage("/login");
//	http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
	http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());

	// Apply JWT
	http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

	// Optional, if you want to test the API from a browser
	// http.httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
	// Allow swagger to be accessed without authentication
	web.ignoring().antMatchers("/v2/api-docs")//
		.antMatchers("/swagger-resources/**")//
		.antMatchers("/swagger-ui.html")//
		.antMatchers("/configuration/**")//
		.antMatchers("/webjars/**")//
		.antMatchers("/public")

		// Un-secure H2 Database (for testing purposes, H2 console shouldn't be
		// unprotected in production)
		.and().ignoring().antMatchers("/h2-console/**/**");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder(12);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
	return super.authenticationManagerBean();
    }

    /////

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
	return new CustomAuthenticationEntryPoint();
    }

}
