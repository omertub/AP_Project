package servlets;

import java.io.IOException;
import java.io.OutputStream;

import server.RequestParser.RequestInfo;

public class SimpleServlet implements Servlet {
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n\r\n";
        response += "Parameters: " + ri.getParameters().toString();
        toClient.write(response.getBytes());
    }

    @Override
    public void close() throws IOException {
        // Cleanup resources if needed
    }
}
