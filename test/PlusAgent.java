package test;

public class PlusAgent implements Agent{
    private String name = "PlusAgent";
    private final String inputTopic1;
    private final String inputTopic2;
    private final String outputTopic;
    private double x = 0;
    private double y = 0;
    
    public PlusAgent(String[] subs, String[] pubs) {
        this.inputTopic1 = subs[0];
        this.inputTopic2 = subs[1];
        this.outputTopic = pubs[0];
        this.x = 0;
        this.y = 0;
        
        // Subscribe to the first two topics in subs, add publisher to first topic in pubs
        TopicManagerSingleton.get().getTopic(inputTopic1).subscribe(this);
        TopicManagerSingleton.get().getTopic(inputTopic2).subscribe(this);
    	TopicManagerSingleton.get().getTopic(outputTopic).addPublisher(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
    }

    @Override
    public void callback(String topic, Message msg) {
//    	System.out.println(this.name + " callback. topic: " + topic + ", msg: " + msg.asText);
        if (topic.equals(inputTopic1)) {
            x = msg.asDouble;
        } else if (topic.equals(inputTopic2)) {
            y = msg.asDouble;
        }
//        try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

        double result = x + y;
//        System.out.println(this.name + " x: " + x + ", y: " + y + ", result: " + result);
        TopicManagerSingleton.get().getTopic(outputTopic).publish(new Message(result));
    }

    @Override
    public void close() {
    }
}