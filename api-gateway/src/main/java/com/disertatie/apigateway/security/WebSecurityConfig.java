package com.disertatie.apigateway.security;

import com.disertatie.apigateway.config.SecurityProperties;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;


@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true, securedEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Autowired
   @Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

	@Override
	protected AuthenticationManager authenticationManager() throws Exception
	{
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
				.cors()
				.configurationSource(corsConfigurationSource())
				.and()
				.headers().frameOptions().disable()
				.and()
				.csrf().disable()
				.authorizeRequests()


				.antMatchers("/auth/**").permitAll()
				.antMatchers("/account-service/accounts/**").permitAll()
				.antMatchers("/client-service/clients/**").permitAll()
				.antMatchers("/client-service/**").permitAll()
				.antMatchers("/subscription-service/**").permitAll()
				.antMatchers("/review-service/**").permitAll()
				.antMatchers("/payment-service/**").permitAll()
				.antMatchers("/eureka/**").permitAll()
            .antMatchers(String.valueOf(HttpMethod.OPTIONS), "/**").permitAll()
				.and()
				.exceptionHandling().authenticationEntryPoint(((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
				.and()
				.oauth2Login()

				.and()
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource()
	{
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		if (null != securityProperties.getCorsConfiguration())
		{
			source.registerCorsConfiguration("/**", securityProperties.getCorsConfiguration());
		}
		return source;
	}
}
