package graph;

import java.util.Set;

import configs.Config;
import configs.GenericConfig;

public class GraphTest {

    public static void main(String[] args) {
        // Create and test multiple graphs
//        testGraph(new MathExampleConfig()); // Test math example configuration
        GenericConfig genericConfig = new GenericConfig();
        genericConfig.setConfFile("../config_files/simple.conf");
        testGraph(genericConfig);
        genericConfig.close();
        
//        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//        for (Thread t : threadSet) {
//        	 
//            // Printing the thread status using getState()
//            // method
//            System.out.println("Thread :" + t + ":"
//                               + "Thread status : "
//                               + t.getState());
//        }
    }

    // Method to create and test a graph from a configuration
    private static void testGraph(Config config) {
        // Create a graph from the configuration
        Graph graph = createGraph(config);

        // Print graph structure
        System.out.println("Graph Structure:");
        graph.forEach(node -> {
            System.out.print(node.getName() + " -> ");
            node.getEdges().forEach(edge -> System.out.print(edge.getName() + " "));
            System.out.println();
        });

        // Check if the graph has cycles
        System.out.println("Graph has cycles: " + graph.hasCycles());
        
        // publish all
        long start = System.currentTimeMillis();    
        TopicManagerSingleton.get().getTopic("A").publish(new Message("10"));
        System.out.println("Elapsed Time1:" + (System.currentTimeMillis() - start)/1000);
        TopicManagerSingleton.get().getTopic("B").publish(new Message("5"));
        System.out.println("Elapsed Time2:" + (System.currentTimeMillis() - start)/1000);

        // Print node names
        System.out.println("Node Names and Messages:");
        graph.forEach(node -> System.out.println(node.getName() + ": " + node.getMessage().asText));
  
        
        System.out.println();
    }

    // Method to create a graph from a configuration
    private static Graph createGraph(Config config) {
        Graph graph = new Graph();
        config.create();
        graph.createFromTopics();
        return graph;
    }
}
