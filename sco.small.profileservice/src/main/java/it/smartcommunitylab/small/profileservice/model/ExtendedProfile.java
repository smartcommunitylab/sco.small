package it.smartcommunitylab.small.profileservice.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendedProfile extends BaseObject {
	private String profileId;
	private Map<String, Object> content;
	private String userId;
	private Map<String, String> name = new HashMap<String, String>();
	private Map<String, String> description = new HashMap<String, String>();
	private List<String> tags = new ArrayList<String>();
	
	@ApiModelProperty(position=4, required=true, value="profile id")
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	@ApiModelProperty(position=9, required=true, value="payload Map&lt;String, Object&gt;")
	public Map<String, Object> getContent() {
		return content;
	}
	public void setContent(Map<String, Object> content) {
		this.content = content;
	}
	@ApiModelProperty(position=5, required=true, value="user id")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@ApiModelProperty(position=6, required=false, value="name as Map&lt;String, String&gt; where the key is a ISO 639-2 Alpha-2 code")
	public Map<String, String> getName() {
		return name;
	}
	public void setName(Map<String, String> name) {
		this.name = name;
	}
	@ApiModelProperty(position=7, required=false, value="name as Map&lt;String, String&gt; where the key is a ISO 639-2 Alpha-2 code")
	public Map<String, String> getDescription() {
		return description;
	}
	public void setDescription(Map<String, String> description) {
		this.description = description;
	}
	@ApiModelProperty(position=8, required=false, value="domain tags")
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
}
