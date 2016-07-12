package it.smartcommunitylab.small.profileservice.controller.rest;

import io.swagger.annotations.ApiOperation;
import it.smartcommunitylab.small.profileservice.common.EntityNotFoundException;
import it.smartcommunitylab.small.profileservice.common.UnauthorizedException;
import it.smartcommunitylab.small.profileservice.common.Utils;
import it.smartcommunitylab.small.profileservice.manager.RepositoryManager;
import it.smartcommunitylab.small.profileservice.model.ExtendedProfile;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ExtendedProfileController {
	private static final transient Logger logger = LoggerFactory.getLogger(ExtendedProfileController.class);
	
	@Autowired
	private RepositoryManager repository;
	
//	@RequestMapping(method = RequestMethod.GET, value = "/extprofile/me")
//	@ApiOperation(value = "get logged user's profiles", response=ExtendedProfile.class, responseContainer="List")
//	public @ResponseBody List<ExtendedProfile> getMyProfiles(HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		String userId = Utils.getUserId();
//		if(logger.isInfoEnabled()) {
//			logger.info(String.format("getMyProfiles - %s", userId));
//		}
//		List<ExtendedProfile> result = repository.getUserProfiles(userId);
//		if(logger.isInfoEnabled()) {
//			logger.info(String.format("getMyProfiles result %d", result.size()));
//		}		
//		return result;
//	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/extprofile/me/{profileId}")
	@ApiOperation(value = "get a spacific logged user's profile")
	public @ResponseBody ExtendedProfile getMyProfileById(@PathVariable String profileId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
//		String userId = Utils.getUserId();
		String userId = "test";
		if(logger.isInfoEnabled()) {
			logger.info(String.format("getMyProfileById - %s - %s", userId, profileId));
		}
		ExtendedProfile result = repository.getUserProfile(userId, profileId);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/extprofile/me/{profileId}")
	@ApiOperation(value = "add a new profile to logged user")
	public @ResponseBody ExtendedProfile addMyProfile(@PathVariable String profileId,	@RequestBody ExtendedProfile profile, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String userId = Utils.getUserId();
		String userId = "test";
		if(logger.isInfoEnabled()) {
			logger.info(String.format("addMyProfile - %s - %s", userId, profileId));
		}
//		if(!Utils.isRequestScopePermitted(request, profileId, "personal", "write")) {
//			throw new UnauthorizedException("requested scope non present");
//		}
		ExtendedProfile result = repository.saveExtendedProfile(userId, profileId, profile);
		return result;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/extprofile/me/{profileId}")
	@ApiOperation(value = "update a spacific logged user's profile")
	public @ResponseBody ExtendedProfile updateMyProfile(@PathVariable String profileId, @RequestBody ExtendedProfile profile, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String userId = Utils.getUserId();
		String userId = "test";
		if(logger.isInfoEnabled()) {
			logger.info(String.format("updateMyProfile - %s - %s", userId, profileId));
		}
//		if(!Utils.isRequestScopePermitted(request, profileId, "personal", "write")) {
//			throw new UnauthorizedException("requested scope non present");
//		}
		ExtendedProfile result = repository.updateExtendedProfile(userId, profileId, profile);
		return result;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/extprofile/me/{profileId}")
	@ApiOperation(value = "delete a spacific logged user's profile")
	public @ResponseBody ExtendedProfile deleteMyProfile(@PathVariable String profileId, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String userId = Utils.getUserId();
		String userId = "test";
		if(logger.isInfoEnabled()) {
			logger.info(String.format("deleteMyProfile - %s - %s", userId, profileId));
		}
//		if(!Utils.isRequestScopePermitted(request, profileId, "personal", "write")) {
//			throw new UnauthorizedException("requested scope non present");
//		}
		ExtendedProfile profile = repository.deleteExtendedProfile(userId, profileId);
		return profile;
	}

//	@RequestMapping(method = RequestMethod.GET, value = "/extprofile/app/{userId}")
//	@ApiOperation(value = "get all user profiles", response=ExtendedProfile.class, responseContainer="List")
//	public @ResponseBody List<ExtendedProfile> getUserProfiles(@PathVariable String userId, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		if(logger.isInfoEnabled()) {
//			logger.info(String.format("getUserProfiles - %s", userId));
//		}
//		List<ExtendedProfile> result = repository.getUserProfiles(userId);
//		if(logger.isInfoEnabled()) {
//			logger.info(String.format("getUserProfiles result %d", result.size()));
//		}
//		return result;
//	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/extprofile/app/{userId}/{profileId}")
	@ApiOperation(value = "get a spacific user profile")
	public @ResponseBody ExtendedProfile getUserProfileById(@PathVariable String userId, @PathVariable String profileId, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(logger.isInfoEnabled()) {
			logger.info(String.format("getUserProfileById - %s - %s", userId, profileId));
		}
		ExtendedProfile result = repository.getUserProfile(userId, profileId);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/extprofile/app/{userId}/{profileId}")
	@ApiOperation(value = "add a new user profile")
	public @ResponseBody ExtendedProfile addUserProfile(@PathVariable String userId, @PathVariable String profileId, 
			@RequestBody ExtendedProfile profile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(logger.isInfoEnabled()) {
			logger.info(String.format("addUserProfile - %s - %s", userId, profileId));
		}
		ExtendedProfile result = repository.saveExtendedProfile(userId, profileId, profile);
		return result;
	}
		
	@RequestMapping(method = RequestMethod.PUT, value = "/extprofile/app/{userId}/{profileId}")
	@ApiOperation(value = "update a spacific user profile")
	public @ResponseBody ExtendedProfile updateUserProfile(@PathVariable String userId, @PathVariable String profileId, 
			@RequestBody ExtendedProfile profile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(logger.isInfoEnabled()) {
			logger.info(String.format("updateUserProfile - %s - %s", userId, profileId));
		}
		ExtendedProfile result = repository.updateExtendedProfile(userId, profileId, profile);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/extprofile/app/{userId}/{profileId}")
	@ApiOperation(value = "delete a spacific user profile")
	public @ResponseBody ExtendedProfile deleteUserProfile(@PathVariable String userId, @PathVariable String profileId, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(logger.isInfoEnabled()) {
			logger.info(String.format("deleteUserProfile - %s - %s", userId, profileId));
		}
		ExtendedProfile profile = repository.deleteExtendedProfile(userId, profileId);
		return profile;
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String,String> handleEntityNotFoundError(HttpServletRequest request, Exception exception) {
		return Utils.handleError(exception);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(value=HttpStatus.FORBIDDEN)
	@ResponseBody
	public Map<String,String> handleUnauthorizedError(HttpServletRequest request, Exception exception) {
		return Utils.handleError(exception);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Map<String,String> handleGenericError(HttpServletRequest request, Exception exception) {
		return Utils.handleError(exception);
	}	
}
