package it.smartcommunitylab.small.profileservice.common;

public class Const {
	public static final String ERRORTYPE = "errorType";
	public static final String ERRORMSG = "errorMsg";
	
	public static final String BEARER_TYPE = "Bearer ";
	public static final String AUTHORIZATION = "Authorization";
	
	public static final String appSchemaOAuth2 = "appoauth2schema";
	public static final String personalSchemaOAuth2 = "personaloauth2schema";
	
	public static final String personalReadScope = "personal.mobility.read"; 
	public static final String personalReadScopeDesc = "read a specific user profile";
	
	public static final String personalWriteScope = "personal.mobility.write"; 
	public static final String personalWriteScopeDesc = "write a specific user profile";
	
	public static final String appReadScope = "app.mobility.read"; 
	public static final String appReadScopeDesc = "read a specific user profile";
	
	public static final String appWriteScope = "app.mobility.write"; 
	public static final String appWriteScopeDesc = "write a specific user profile";
	
	public static final String basicProfileScope = "profile.basicprofile.me"; 
	public static final String basicProfileScopeDesc = "read the aac basic profile";
}
