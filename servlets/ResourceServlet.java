package servlets;

import java.io.IOException;
import java.io.OutputStream;

import server.RequestParser.RequestInfo;

public class ResourceServlet implements Servlet {
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        String paramValue = ri.getParameters().get("id");
        String response = "Received id: " + paramValue;
        toClient.write(("HTTP/1.1 200 OK\r\nContent-Length: " + response.length() + "\r\n\r\n" + response).getBytes());
    }

    @Override
    public void close() throws IOException {
        // No resources to close in this simple example
    }
}
