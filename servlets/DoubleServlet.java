package servlets;

import java.io.IOException;
import java.io.OutputStream;

import server.RequestParser.RequestInfo;

public class DoubleServlet implements Servlet {
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        String paramValue = ri.getParameters().get("number");
        int number = Integer.parseInt(paramValue);
        int doubled = number * 2;
        String response = "Doubled value: " + doubled;
        toClient.write(("HTTP/1.1 200 OK\r\nContent-Length: " + response.length() + "\r\n\r\n" + response).getBytes());
    }

    @Override
    public void close() throws IOException {
        // No resources to close in this simple example
    }
}
