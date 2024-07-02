package test;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    public final String name;
    public final List<Agent> subs; // List of subscribed agents
    public final List<Agent> pubs; // List of publishing agents

    // Constructor
    Topic(String name) {
        this.name = name;
        this.subs = new ArrayList<>();
        this.pubs = new ArrayList<>();
    }

    // subscribe an agent to the topic
    public void subscribe(Agent agent) {
        subs.add(agent);
    }

    // unsubscribe an agent from the topic
    public void unsubscribe(Agent agent) {
        subs.remove(agent);
    }

    // publish a message to all subscribed agents
    public void publish(Message message) {
        for (Agent agent : subs) {
            agent.callback(this.name, message);
        }
    }

    // add a publishing agent to the topic
    public void addPublisher(Agent agent) {
        pubs.add(agent);
    }

    // remove a publishing agent from the topic
    public void removePublisher(Agent agent) {
        pubs.remove(agent);
    }
}