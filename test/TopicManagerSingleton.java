package test;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class TopicManagerSingleton {

	public static TopicManager get() {
		return TopicManager.instance;
	}
	
	public static class TopicManager {
		private static final TopicManager instance = new TopicManager(); // Not generated unless called, since its an inner class
	    private final ConcurrentHashMap<String, Topic> topics;
	
	    private TopicManager() {
	        topics = new ConcurrentHashMap<>();
	    }
		
	    public Topic getTopic(String name) {
	        return topics.computeIfAbsent(name, Topic::new);
	    }
	
	    public Collection<Topic> getTopics() {
	        return topics.values();
	    }
	
	    public void clear() {
	        topics.clear();
	    }
	}
}
