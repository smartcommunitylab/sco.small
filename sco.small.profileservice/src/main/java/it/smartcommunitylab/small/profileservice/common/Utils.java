package it.smartcommunitylab.small.profileservice.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Utils {

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
		UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal.getUsername();
	}
	
	public static boolean isRequestScopePermitted(HttpServletRequest request, String profileId, String caller, String method) {
		String scope = (String)request.getAttribute("scope");
		if(!Utils.isNull(scope)) {
			List<String> permissions = Arrays.asList(scope.split(" "));
			String requestedScope = caller + ":" + profileId + ":" + method;
			if(permissions.contains(requestedScope)) {
				return true;
			}
		}
		return false;
	}
	
}
