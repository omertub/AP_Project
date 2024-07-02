package test;

public class IncAgent implements Agent{
    private String name = "IncAgent";
    private final String inputTopic;
    private final String outputTopic;
    
    public IncAgent(String[] subs, String[] pubs) {
        this.inputTopic = subs[0];
        this.outputTopic = pubs[0];
        
        // Subscribe to the first two topics in subs, add publisher to first topic in pubs
        TopicManagerSingleton.get().getTopic(inputTopic).subscribe(this);
    	TopicManagerSingleton.get().getTopic(outputTopic).addPublisher(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void reset() {
    }

    @Override
    public void callback(String topic, Message msg) {
//    	System.out.println(this.name + " callback. topic: " + topic + ", msg: " + msg.asText);
//        try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        if (topic.equals(inputTopic)) {
            double x = msg.asDouble;
            double result = x + 1;
//            System.out.println(this.name + " x: " + x + ", result: " + result);
            TopicManagerSingleton.get().getTopic(outputTopic).publish(new Message(result));
        }

    }

    @Override
    public void close() {
    }
}
