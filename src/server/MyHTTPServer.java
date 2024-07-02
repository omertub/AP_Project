package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import server.RequestParser.RequestInfo;
import servlets.Servlet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MyHTTPServer implements HTTPServer {
    private final int port;
    private final ExecutorService executorService;
    private final ConcurrentMap<String, Servlet> getServlets;
    private ServerSocket serverSocket;
    private volatile boolean running;

    public MyHTTPServer(int port, int maxThreads) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(maxThreads);
        this.getServlets = new ConcurrentHashMap<>();
    }

    @Override
    public void addServlet(String httpCommand, String uri, Servlet s) {
        if (httpCommand.equalsIgnoreCase("GET")) {
            getServlets.put(uri, s);
        }
        // Add more commands like POST, DELETE if needed
    }

    @Override
    public void removeServlet(String httpCommand, String uri) {
        if (httpCommand.equalsIgnoreCase("GET")) {
            getServlets.remove(uri);
        }
        // Remove more commands like POST, DELETE if needed
    }

    @Override
    public void start() {
        this.running = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executorService.submit(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    if (!running) {
                        break; // Server was stopped
                    }
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            RequestInfo requestInfo = RequestParser.parseRequest(reader);
            String uri = requestInfo.getUri();
            
            // Strip query parameters for matching
            String uriWithoutParams = uri.split("\\?")[0];
            
            Servlet servlet = getServlets.get(uriWithoutParams);
            if (servlet != null) {
                servlet.handle(requestInfo, clientSocket.getOutputStream());
            } else {
                // Handle 404 Not Found
                clientSocket.getOutputStream().write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        running = false;
        if (serverSocket != null) {
            try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
    
    public ExecutorService getExecutorService() {
    	return this.executorService;
    }
}
