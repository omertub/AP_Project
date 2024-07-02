package configs;

import graph.Graph;

public class Main {
    public static void main(String[] args) {
        // Create an instance of GenericConfig
        GenericConfig genericConfig = new GenericConfig();
        genericConfig.setConfFile("../config_files/simple.conf");

        // Create the configuration from the file
        genericConfig.create();

        // Create an instance of TopicManager and Graph
        Graph graph = new Graph();
        graph.createFromTopics();

        // Print the graph structure
        System.out.println("Graph Structure:");
        for (Node node : graph) {
            System.out.println("Node: " + node.getName());
            System.out.println("Message: " + (node.getMessage() != null ? node.getMessage().asText : "null"));
            System.out.print("Edges: ");
            for (Node edge : node.getEdges()) {
                System.out.print(edge.getName() + " ");
            }
            System.out.println();
        }

        // Check if the graph has cycles
        boolean hasCycles = graph.hasCycles();
        System.out.println("Does the graph have cycles? " + hasCycles);

        // Close all agents
        genericConfig.close();
    }
}