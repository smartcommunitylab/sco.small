/**
 * Copyright 2015 Smart Community Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.smartcommunitylab.small.profileservice.security;

import it.smartcommunitylab.small.profileservice.common.Utils;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

/**
 * 
 * @author nawazk
 *
 */
public class OAuthFilter extends GenericFilterBean {
	private static final transient Logger logger = LoggerFactory.getLogger(OAuthFilter.class);

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private RememberMeServices rememberMeServices;
	@Autowired
	private Environment env;

	private BasicProfileService profileService;

	@PostConstruct
	private void init() {
		profileService = new BasicProfileService(env.getProperty("ext.aacURL"));
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			String authToken = Utils.extractToken((HttpServletRequest) request);
			if (authToken != null) {
				if (((HttpServletRequest) request).getRequestURI().contains("/extprofile/me/")) {
					try {
						BasicProfile basicProfile = profileService.getBasicProfile(authToken);
						UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
								basicProfile.getUserId(), basicProfile.getUserId(),
								SmallUserDetails.SMALLPROFILE_AUTHORITIES);
						token.setDetails(new WebAuthenticationDetails((HttpServletRequest) request));
						SecurityContextHolder.getContext().setAuthentication(token);
					} catch (Exception e) {
						logger.error("doFilter error:" + e.getMessage());
						SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("", "", 
								SmallUserDetails.SMALLPROFILE_AUTHORITIES));
					}
				}
				if (((HttpServletRequest) request).getRequestURI().contains("/extprofile/app/")) {
					PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(
							authToken, authToken, SmallUserDetails.SMALLPROFILE_AUTHORITIES);
					token.setDetails(new WebAuthenticationDetails((HttpServletRequest) request));
					SecurityContextHolder.getContext().setAuthentication(token);
				}
			}
		}
		chain.doFilter(request, response);
	}

	// private boolean isAuthenticationRequired() {
	// // apparently filters have to check this themselves. So make sure they
	// // have a proper AuthenticatedAccount in their session.
	// Authentication existingAuth =
	// SecurityContextHolder.getContext().getAuthentication();
	// if ((existingAuth == null) || !existingAuth.isAuthenticated()) {
	// return true;
	// }
	//
	// return false;
	// }

}