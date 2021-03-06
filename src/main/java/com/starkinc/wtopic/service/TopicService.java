package com.starkinc.wtopic.service;

import com.starkinc.wtopic.dto.SearchDTO;
import com.starkinc.wtopic.dto.TopicsDTO;
import com.starkinc.wtopic.entity.Topic;

/**
 * @author RajaSushanth
 *
 */
public interface TopicService {
	
	public Topic createTopic(Topic topic);
	public Topic updateTopic(String topicName, String firstMessage);
	public Topic getTopicByName(String topicname);
	public TopicsDTO getTopicsByAuthor(int skip);
	public SearchDTO getTopicsByAuthorAndTopicName(SearchDTO searchDTO, int skip);
}
