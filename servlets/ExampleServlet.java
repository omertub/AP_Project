package servlets;

import java.io.IOException;
import java.io.OutputStream;

import server.RequestParser.RequestInfo;

public class ExampleServlet implements Servlet {
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n\r\nHello, world!";
        toClient.write(response.getBytes());
    }

    @Override
    public void close() throws IOException {
        // Clean up resources
    }
}