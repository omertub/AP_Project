package test;

import java.util.Date;

public class Message {
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;

    // Constructor for initializing with String
    public Message(String text) {
        this.data = text.getBytes();
        this.asText = text;
        double doubleVal;
        try {
        	doubleVal = Double.parseDouble(text);
        } catch (NumberFormatException e) {
        	doubleVal = Double.NaN;
        }
        this.asDouble = doubleVal;
        this.date = new Date(); // Initialize with current time
    }
    
    // Constructor for initializing with double
    public Message(double number) {
        this(Double.toString(number));
    }
    
    // Constructor for initializing with bytes
    public Message(byte[] bytes) {
        this(new String(bytes));
    }
}