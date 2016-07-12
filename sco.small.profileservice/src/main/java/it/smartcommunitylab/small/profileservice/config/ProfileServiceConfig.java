/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package it.smartcommunitylab.small.profileservice.config;

import it.smartcommunitylab.small.profileservice.manager.RepositoryManager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

@Configuration
@ComponentScan("it.smartcommunitylab.small.profileservice")
@PropertySource("classpath:profileservice.properties")
@EnableWebMvc
@EnableSwagger2
public class ProfileServiceConfig extends WebMvcConfigurerAdapter {

	@Autowired
	@Value("${mongo.db}")
	private String dbName;

	@Autowired
	@Value("${mongo.host}")
	private String dbHost;

	@Autowired
	@Value("${mongo.port}")
	private String dbPort;

	@Autowired
	@Value("${swagger.title}")
	private String swaggerTitle;
	
	@Autowired
	@Value("${swagger.desc}")
	private String swaggerDesc;

	@Autowired
	@Value("${swagger.version}")
	private String swaggerVersion;
	
	@Autowired
	@Value("${swagger.tos.url}")
	private String swaggerTosUrl;
	
	@Autowired
	@Value("${swagger.contact}")
	private String swaggerContact;

	@Autowired
	@Value("${swagger.license}")
	private String swaggerLicense;

	@Autowired
	@Value("${swagger.license.url}")
	private String swaggerLicenseUrl;
	
	@Autowired
	public ProfileServiceConfig() {
	}

	@Bean
	public MongoTemplate getMongoTemplate() throws UnknownHostException, MongoException {
		return new MongoTemplate(new MongoClient(dbHost, Integer.parseInt(dbPort)), dbName);
	}
	
	@Bean
	public RepositoryManager getRepositoryManager() throws UnknownHostException, MongoException {
		return new RepositoryManager(getMongoTemplate());
	}

	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/resources/");
		resolver.setSuffix(".html");
		return resolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/*").addResourceLocations(
				"/resources/");
		registry.addResourceHandler("/resources/*").addResourceLocations(
				"/resources/");
		registry.addResourceHandler("/css/**").addResourceLocations(
				"/resources/css/");
		registry.addResourceHandler("/fonts/**").addResourceLocations(
				"/resources/fonts/");
		registry.addResourceHandler("/js/**").addResourceLocations(
				"/resources/js/");
		registry.addResourceHandler("/lib/**").addResourceLocations(
				"/resources/lib/");
		registry.addResourceHandler("/templates/**").addResourceLocations(
				"/resources/templates/");
		registry.addResourceHandler("/html/**").addResourceLocations(
				"/resources/html/");
		registry.addResourceHandler("/file/**").addResourceLocations(
				"/resources/file/");
	}

	@Bean
	public MultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}
	
	@Bean 
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
  public Docket swaggerSpringMvcPlugin() {
		ApiInfo apiInfo = new ApiInfo(swaggerTitle, swaggerDesc, swaggerVersion, swaggerTosUrl, swaggerContact, 
				swaggerLicense, swaggerLicenseUrl);
     return new Docket(DocumentationType.SWAGGER_2)
     	.groupName("extprofile")
     	.select()
     		.paths(PathSelectors.regex("/extprofile/.*"))
     		.build()
        .apiInfo(apiInfo)
        .produces(getContentTypes())
        .securitySchemes(getSecuritySchemes());
  }
	
	private Set<String> getContentTypes() {
		Set<String> result = new HashSet<String>();
		result.add("application/json");
    return result;
  }
	
	private List<SecurityScheme> getSecuritySchemes() {
		List<SecurityScheme> result = new ArrayList<SecurityScheme>();
		List<AuthorizationScope> scopes = new ArrayList<AuthorizationScope>();
		scopes.add(new AuthorizationScope("personal:profileId:read", "read a specific user profile"));
		scopes.add(new AuthorizationScope("personal:profileId:write", "write a specific user profile"));
		scopes.add(new AuthorizationScope("personal:profileId:delete", "delete a specific user profile"));
		scopes.add(new AuthorizationScope("app:profileId:read", "read a specific user profile"));
		scopes.add(new AuthorizationScope("app:profileId:write", "write a specific user profile"));
		scopes.add(new AuthorizationScope("app:profileId:delete", "delete a specific user profile"));
		List<GrantType> grants = new ArrayList<GrantType>();
		grants.add(new AuthorizationCodeGrant(
				new TokenRequestEndpoint("/authorize", "client_id", "client_secret"), 
				new TokenEndpoint("/token", "token")));
		OAuth oAuth = new OAuth("profile_auth", scopes, grants);
		result.add(oAuth);
		return result;
	}
}
