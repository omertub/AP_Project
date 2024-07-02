package configs;

import java.util.function.BinaryOperator;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;

public class BinOpAgent implements Agent {
    private final String name;
    private final String inputTopic1;
    private final String inputTopic2;
    private final String outputTopic;
    private final BinaryOperator<Double> operation;
    private Double input1 = Double.NaN;
    private Double input2 = Double.NaN;

    // Constructor
    public BinOpAgent(String name, String inputTopic1, String inputTopic2, String outputTopic, BinaryOperator<Double> operation) {
        this.name = name;
        this.inputTopic1 = inputTopic1;
        this.inputTopic2 = inputTopic2;
        this.outputTopic = outputTopic;
        this.operation = operation;
        
        // Subscribe to input topics
        TopicManagerSingleton.get().getTopic(inputTopic1).subscribe(this);
        TopicManagerSingleton.get().getTopic(inputTopic2).subscribe(this);
        
        // Add publisher to output topic
        TopicManagerSingleton.get().getTopic(outputTopic).addPublisher(this);
    }

    // Implementation of the callback method
    @Override
    public void callback(String topic, Message msg) {
//    	System.out.println("Agent " + this.name + " callback: topic: " + topic + ", msg: " + msg.asText);
		
        if (topic.equals(inputTopic1)) {
            input1 = msg.asDouble;
        } else if (topic.equals(inputTopic2)) {
            input2 = msg.asDouble;
        }
        
        if (!input1.isNaN() && !input2.isNaN()) {
        	double result = operation.apply(input1, input2);
//        	System.out.println("Input 1: " + input1 + ", Input 2: " + input2 + ", result: " + result);
        	Message resultMsg = new Message(result);
        	TopicManagerSingleton.get().getTopic(outputTopic).publish(resultMsg);        	
        }
    }

    // Implementation of the reset method
    @Override
    public void reset() {
        input1 = (double) 0;
        input2 = (double) 0;
    }

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}