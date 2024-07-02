package test;

import java.util.ArrayList;
import java.util.List;

public class Graph extends ArrayList<Node> {
	
    // Method to check for cycles in the graph
    public boolean hasCycles() {
        for (Node node : this) {
            if (node.hasCycles()) {
                return true;
            }
        }
        return false;
    }
    
    // Method to get agent node if exists, or create new node
    private Node getAgentNode(String name) {
    	Node agentNode = new Node("A" + name);
    	if (this.contains(agentNode)) {
    		return this.stream().filter(node -> node.getName().equals("A" + name)).findFirst().get();    		
    	}
        // If the agent node is not already in the graph, add it
        this.add(agentNode);
        return agentNode;
    }

    // Method to create the graph from topics and agents in TopicManager
    public void createFromTopics() {
        // Clear the existing graph
        this.clear();

        // Get all topics from TopicManager
        for (Topic topic : TopicManagerSingleton.get().getTopics()) {
            // Add topic node to Graph
            Node topicNode = new Node("T" + topic.name);
            this.add(topicNode);
            
            // Add edges from topic to agent
            for (Agent agent : topic.subs) {
            	Node agentNode = getAgentNode(agent.getName());
                topicNode.addEdge(agentNode);
            }
            // Add edges from agent to topic
            for (Agent agent : topic.pubs) {
            	Node agentNode = getAgentNode(agent.getName());
            	agentNode.addEdge(topicNode);
            }
        }

    }
}