package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {
    private String name;
    private List<Node> edges;
    private Message message;

    // Constructor
    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.message = new Message("");
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for edges
    public List<Node> getEdges() {
        return edges;
    }

    public void setEdges(List<Node> edges) {
        this.edges = edges;
    }

    // Getter and setter for message
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    // Method to add an edge
    public void addEdge(Node node) {
        edges.add(node);
    }

    // Method to check for cycles
    public boolean hasCycles() {
        return hasCycles(this, new ArrayList<>());
    }

    private boolean hasCycles(Node current, List<Node> visited) {
        // If the current node has already been visited, then there's a cycle
        if (visited.contains(current)) {
            return true;
        }

        // Mark the current node as visited
        visited.add(current);

        // Recursively check each adjacent node for cycles
        for (Node neighbor : current.getEdges()) {
            if (hasCycles(neighbor, new ArrayList<>(visited))) {
                return true;
            }
        }

        // If no cycles found, return false
        return false;
    }
    
    @Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return Objects.equals(name, other.name);
	}
}
