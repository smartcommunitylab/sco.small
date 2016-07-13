package it.smartcommunitylab.small.profileservice.config;

import it.smartcommunitylab.small.profileservice.security.OAuthFilter;
import it.smartcommunitylab.small.profileservice.security.SmallUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Autowired
	private UserDetailsService userDetailsServiceImpl;

	@Autowired
	private AuthenticationProvider consoleAuthenticationProvider;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(consoleAuthenticationProvider).authenticationProvider(
				rememberMeAuthenticationProvider());
	}

	@Autowired
	@Value("${rememberme.key}")
	private String rememberMeKey;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http
		// .headers()
		// .frameOptions().disable();

//		http.rememberMe();

		http
		.authorizeRequests()
			.antMatchers("/extprofile/**")
				.hasAnyAuthority(SmallUserDetails.SMALLPROFILE).and()
				.addFilterBefore(oauthAuthenticationFilter(), BasicAuthenticationFilter.class)
//				.addFilterBefore(rememberMeAuthenticationFilter(), BasicAuthenticationFilter.class)
		.authorizeRequests()
			.antMatchers("/", "/console/**").authenticated()
		.and().formLogin().loginPage("/login").permitAll()
		.and().logout().permitAll().deleteCookies("rememberme", "JSESSIONID")
		.and().csrf().disable();
	}

	@Bean
	public OAuthFilter oauthAuthenticationFilter() throws Exception {
		return new OAuthFilter();
	}

	@Bean
	public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
		return new RememberMeAuthenticationFilter(authenticationManager(),
				tokenBasedRememberMeService());
	}

	// @Bean
	public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
		return new RememberMeAuthenticationProvider(tokenBasedRememberMeService().getKey());
	}

	@Bean
	public TokenBasedRememberMeServices tokenBasedRememberMeService() {
		TokenBasedRememberMeServices service = new TokenBasedRememberMeServices(
				env.getProperty("rememberme.key"), userDetailsServiceImpl);
		service.setAlwaysRemember(true);
		service.setCookieName("rememberme");
		service.setTokenValiditySeconds(3600 * 24 * 365 * 100);
		return service;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
