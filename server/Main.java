package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import servlets.DoubleServlet;
import servlets.EchoServlet;
import servlets.ResourceServlet;
import servlets.SimpleServlet;

import java.io.*;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        MyHTTPServer server = new MyHTTPServer(port, 10);

        // Add a servlet to handle GET requests to /api/resource
        server.addServlet("GET", "/api/resource", new ResourceServlet());

        // Add another servlet to handle GET requests to /api/echo
        server.addServlet("GET", "/api/echo", new EchoServlet());

        // Add a third servlet to handle GET requests to /api/double
        server.addServlet("GET", "/api/double", new DoubleServlet());

        server.start();

        try {
            // Test the first servlet
            Socket clientSocket = new Socket("localhost", port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("GET /api/resource?id=123 HTTP/1.1");
            out.println("Host: localhost");
            out.println("");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String responseLine;
            System.out.println("Server response for /api/resource:");
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
            clientSocket.close();

            // Test the second servlet
            clientSocket = new Socket("localhost", port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("GET /api/echo?message=hello HTTP/1.1");
            out.println("Host: localhost");
            out.println("");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Server response for /api/echo:");
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
            clientSocket.close();

            // Test the third servlet
            clientSocket = new Socket("localhost", port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("GET /api/double?number=21 HTTP/1.1");
            out.println("Host: localhost");
            out.println("");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Server response for /api/double:");
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        server.close();
		// Check if all threads are terminated
		if (server.getExecutorService().isTerminated()) {
		    System.out.println("All threads are terminated.");
		} else {
		    System.out.println("Some threads are still running.");
		}
    }
}
