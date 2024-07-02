package graph;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParallelAgent implements Agent {
    private final Agent agent;
    private final BlockingQueue<MessageWrapper> queue;
    private volatile boolean running = true;
    Thread t;

    // Inner static class MessageWrapper to wrap together the topic and msg parameters
    private static class MessageWrapper {
        private final String topic;
        private final Message msg;

        public MessageWrapper(String topic, Message msg) {
            this.topic = topic;
            this.msg = msg;
        }

        public String getTopic() {
            return topic;
        }

        public Message getMessage() {
            return msg;
        }
    }

    // Constructor for ParallelAgent which takes an agent and the queue capacity as parameters
    public ParallelAgent(Agent agent, int capacity) {
        this.agent = agent;
        this.queue = new ArrayBlockingQueue<>(capacity);
        
        // Start a new thread to process messages from the queue
        Thread processingThread = new Thread(() -> {
            while (running || !queue.isEmpty()) {
                try {
                    // Take a message from the queue and call the callback method of the wrapped agent
                    MessageWrapper messageWrapper = queue.take();
//                	System.out.println("Calling: " + messageWrapper.getTopic() + " msg: " + messageWrapper.getMessage().asText);
                    agent.callback(messageWrapper.getTopic(), messageWrapper.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t = processingThread;
        processingThread.start();
    }

    // Implement getName method by delegating to the wrapped agent
    @Override
    public String getName() {
        return agent.getName();
    }

    // Implement reset method by delegating to the wrapped agent
    @Override
    public void reset() {
        agent.reset();
    }

    // Implement callback method by putting a new message in the queue
    @Override
    public void callback(String topic, Message msg) {
        try {
//        	System.out.println("Pushing to Q: " + topic + " msg: " + msg.asText);
            queue.put(new MessageWrapper(topic, msg));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // Method to close the ParallelAgent and stop the processing thread
    public void close() {
        running = false;
        // Interrupt the processing thread to ensure it exits if it's waiting on the queue
        t.interrupt();
    }
}
