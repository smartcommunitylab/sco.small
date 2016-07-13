package it.smartcommunitylab.small.profileservice.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.aac.AACService;

public class Utils {
	private static final transient Logger logger = LoggerFactory.getLogger(Utils.class);
			
	public static boolean isNull(String value) {
		if((value == null) || (value.isEmpty())) {
			return true;
		} else {
			return false;
		}
	}

	public static String getString(Map<String, String> data, String lang, String defaultLang) {
		String result = null;
		if(data.containsKey(lang)) {
			result = data.get(lang);
		} else {
			result = data.get(defaultLang);
		}
		return result;
	}
	
	public static Map<String,String> handleError(Exception exception) {
		Map<String,String> errorMap = new HashMap<String,String>();
		errorMap.put(Const.ERRORTYPE, exception.getClass().toString());
		errorMap.put(Const.ERRORMSG, exception.getMessage());
		return errorMap;
	}
	
	public static String getUserId() {
		String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal;
	}
	
	public static boolean isRequestScopePermitted(HttpServletRequest request, AACService aacService, 
			String profileId, String caller, String method) {
		String token = Utils.extractToken(request);
		if(!isNull(token)) {
			String requestedScope = caller + "." + profileId + "." + method;
			try {
				return aacService.isTokenApplicable(token, requestedScope);
			} catch (AACException e) {
				logger.error("isRequestScopePermitted error:" + e.getMessage());
			}
		}
		return false;
	}
	
	public static String extractToken(HttpServletRequest request) {
		String completeToken = request.getHeader(Const.AUTHORIZATION);
		if (completeToken == null)
			return null;
		if (completeToken.startsWith(Const.BEARER_TYPE)) {
			completeToken = completeToken.substring(Const.BEARER_TYPE.length());
		}
		return completeToken;
	}
	
}
