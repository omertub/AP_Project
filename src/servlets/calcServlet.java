package servlets;

import java.io.IOException;
import java.io.OutputStream;

import server.RequestParser.RequestInfo;

public class calcServlet implements Servlet {

	@Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        // Extract parameters
        String param1 = ri.getParameters().get("param1");
        String param2 = ri.getParameters().get("param2");

        // Convert to double and perform calculation (addition)
        double num1 = Double.parseDouble(param1);
        double num2 = Double.parseDouble(param2);
        double result = num1 + num2;

        // Write the result back to the client
        toClient.write(("Result: " + result).getBytes());
    }

    @Override
    public void close() throws IOException {
        // No specific action needed on close
    }

}
