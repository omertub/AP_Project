package configs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import graph.Agent;
import graph.ParallelAgent;
import graph.Topic;
import graph.TopicManagerSingleton;

public class GenericConfig implements Config {
	private String confFile;
	private List<ParallelAgent> agents = new ArrayList<>();
	 
	public void setConfFile(String confFile) {
	    this.confFile = confFile;
	}

	@Override
	public void create() {
	    try (BufferedReader reader = new BufferedReader(new FileReader(confFile))) {
	        List<String> lines = new ArrayList<>();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            lines.add(line);
	        }
	        if (lines.size() % 3 != 0) {
	            throw new IllegalArgumentException("Invalid configuration file format");
	        }
	
	        for (int i = 0; i < lines.size(); i += 3) {
	            String className = lines.get(i);
	            String[] subs = lines.get(i + 1).split(",");
	            String[] pubs = lines.get(i + 2).split(",");
	
	            try {
	            	// create Agent
	            	Class<?> clazz = Class.forName(className);
		            Constructor<?> constructor = clazz.getConstructor(String[].class, String[].class);
		            Agent agent = (Agent) constructor.newInstance(subs, pubs);
		            
		            // Create ParallelAgent
		            ParallelAgent parallelAgent = new ParallelAgent(agent, 10);
		            agents.add(parallelAgent);
		            
		            // Replace Agent with ParallelAgent for all topics
		            for (Topic topic : TopicManagerSingleton.get().getTopics()) {
		            	for (Agent ag : topic.subs) {
		            		if (ag.getName().equals(agent.getName()) && !(agent instanceof ParallelAgent)) {
				            	topic.unsubscribe(agent);
				            	topic.subscribe(parallelAgent);
		            		}
		            	}
		            	for (Agent ag : topic.pubs) {
		            		if (ag.getName().equals(agent.getName()) && !(agent instanceof ParallelAgent)) {
		            			topic.removePublisher(agent);
		            			topic.addPublisher(parallelAgent);
		            		}
		            	}
		            } 
	             } catch (ClassNotFoundException e) {
	                    e.printStackTrace();
	            }
	            
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@Override
    public String getName() {
        return "GenericConfig";
    }

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public void close() {
	    for (ParallelAgent agent : agents) {
	        agent.close();
	    }
	}

}
