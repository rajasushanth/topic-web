package com.starkinc.wtopic.restClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.starkinc.wtopic.constants.Constants;
import com.starkinc.wtopic.entity.Message;
import com.starkinc.wtopic.entity.Topic;
import com.starkinc.wtopic.entity.UserSession;
import com.starkinc.wtopic.exception.ClientResponseException;
import com.starkinc.wtopic.exception.TopicException;
import com.starkinc.wtopic.util.TopicWebUtils;

@Service
public class TopicClient {
	
	private RestTemplate restTemplate;
	private String baseURL;
	private String topicResourcePath;
	
	
	public ResponseEntity<Topic> createTopic(Topic topic){
		ResponseEntity<Topic> responseEntity = null;
		UserSession userSession = TopicWebUtils.getCurrentUserSession();
		String token = userSession.getToken();
		topic.setAuthor(userSession.getUsername());
		HttpEntity<Object> entity = TopicWebUtils.buildEntityWithToken(topic, token);
		try{
			responseEntity = restTemplate.postForEntity(baseURL + topicResourcePath, entity, Topic.class);
		}catch (ClientResponseException e) {
			throw new TopicException(e);
		}
		return responseEntity;
	}
	
	public ResponseEntity<Topic> updateTopic(String topicName, String firstMessage){
		UserSession userSession = TopicWebUtils.getCurrentUserSession();
		String token = userSession.getToken();
		Message message = new Message();
		message.setMessage(firstMessage);
		message.setCommentator(userSession.getUsername());
		HttpEntity<Object> entity = TopicWebUtils.buildEntityWithToken(message, token);
		ResponseEntity<Topic> topic = null;
		try {
			topic = restTemplate.exchange(TopicWebUtils.appendPath(baseURL + topicResourcePath, topicName), HttpMethod.PUT, entity, Topic.class);
		} catch (ClientResponseException e) {
			e.printStackTrace();
		}
		return topic;
	}
	
	public ResponseEntity<Topic> getTopicByName(String topicName){
		UserSession userSession = TopicWebUtils.getCurrentUserSession();
		String token = userSession.getToken();
		HttpEntity<Object> entity = TopicWebUtils.buildEntityWithToken(null, token);
		ResponseEntity<Topic> responseEntity = restTemplate.exchange(TopicWebUtils.appendPath(baseURL + topicResourcePath, topicName),
				HttpMethod.GET, entity,Topic.class);
		return responseEntity;
	}
	
	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Value(Constants.BASE_URL)
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	
	@Value(Constants.TOPIC_RESOURCE)
	public void setPath(String topicResourcePath) {
		this.topicResourcePath = topicResourcePath;
	}

}
