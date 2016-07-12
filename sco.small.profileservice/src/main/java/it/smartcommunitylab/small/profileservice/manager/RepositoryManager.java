package it.smartcommunitylab.small.profileservice.manager;

import it.smartcommunitylab.small.profileservice.common.EntityNotFoundException;
import it.smartcommunitylab.small.profileservice.model.ExtendedProfile;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class RepositoryManager {
	private static final transient Logger logger = LoggerFactory.getLogger(RepositoryManager.class);
	
	private MongoTemplate mongoTemplate;
	
	public RepositoryManager(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	public ExtendedProfile saveExtendedProfile(String userId, String profileId, 
			ExtendedProfile profile) {
		Query query = getProfileQuery(userId, profileId);
		ExtendedProfile profileDB = mongoTemplate.findOne(query, ExtendedProfile.class);
		Date now = new Date();
		if(profileDB == null) {
			profile.setCreationDate(now);
			profile.setLastUpdate(now);
			profile.setUserId(userId);
			profile.setProfileId(profileId);
			mongoTemplate.save(profile);
			profileDB = profile;
		} else {
			Update update = new Update();
			update.set("lastUpdate", now);
			update.set("content", profile.getContent());
			update.set("name", profile.getName());
			update.set("description", profile.getDescription());
			update.set("tags", profile.getTags());
			mongoTemplate.updateFirst(query, update, ExtendedProfile.class);
			profileDB.setLastUpdate(now);
			profileDB.setContent(profile.getContent());
			profileDB.setName(profile.getName());
			profileDB.setDescription(profile.getDescription());
			profileDB.setTags(profile.getTags());
		}
		return profileDB;
	}
	
	public ExtendedProfile updateExtendedProfile(String userId, String profileId, 
			ExtendedProfile profile) throws EntityNotFoundException {
		Query query = getProfileQuery(userId, profileId);
		ExtendedProfile profileDB = mongoTemplate.findOne(query, ExtendedProfile.class);
		Date now = new Date();
		if(profileDB == null) {
			throw new EntityNotFoundException(String.format("Profile for user %s with id %s not found", userId, profileId));
		}
		Update update = new Update();
		update.set("lastUpdate", now);
		update.set("content", profile.getContent());
		update.set("name", profile.getName());
		update.set("description", profile.getDescription());
		update.set("tags", profile.getTags());
		mongoTemplate.updateFirst(query, update, ExtendedProfile.class);
		profileDB.setLastUpdate(now);
		profileDB.setContent(profile.getContent());
		profileDB.setName(profile.getName());
		profileDB.setDescription(profile.getDescription());
		profileDB.setTags(profile.getTags());
		return profileDB;
	}
	
	public ExtendedProfile deleteExtendedProfile(String userId, String profileId) throws EntityNotFoundException {
		Query query = getProfileQuery(userId, profileId);
		ExtendedProfile profileDB = mongoTemplate.findOne(query, ExtendedProfile.class);
		if(profileDB == null) {
			throw new EntityNotFoundException(String.format("Profile for user %s with id %s not found", userId, profileId));
		}
		mongoTemplate.findAndRemove(query, ExtendedProfile.class);
		return profileDB;
	}
	
	public ExtendedProfile getUserProfile(String userId, String profileId) throws EntityNotFoundException {
		Query query = getProfileQuery(userId, profileId);
		ExtendedProfile profileDB = mongoTemplate.findOne(query, ExtendedProfile.class);
		if(profileDB == null) {
			throw new EntityNotFoundException(String.format("Profile for user %s with id %s not found", userId, profileId));
		}
		return profileDB;
	}
	
	public List<ExtendedProfile> getUserProfiles(String userId) {
		Query query = new Query(new Criteria("userId").is(userId));
		List<ExtendedProfile> result = mongoTemplate.find(query, ExtendedProfile.class);
		return result;
	}
	
	public Query getProfileQuery(String userId, String profileId) {
		return new Query(new Criteria("userId").is(userId).and("profileId").is(profileId));
	}
}
