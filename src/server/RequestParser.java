package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
	
	public static class RequestInfo {
		private String httpCommand;
		private String uri;
		private String[] uriParts;
		private Map<String, String> params;
		private byte[] content;
		
        // Constructor
        public RequestInfo(String httpCommand, String uri, String[] uriParts, Map<String, String> params, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriParts = uriParts;
            this.params = params != null ? params : new HashMap<>();
            this.content = content;
//            debug();
        }
        
//        public void debug() {
//	    	System.out.println("Http Command: " + this.getHttpCommand());
//	    	System.out.println("URI: " + this.getUri());
//	    	System.out.println("URI Parts: " + String.join(", ", this.getUriSegments()));
//	    	System.out.println("Parameters: " + this.getParameters());
//	    	System.out.println("Content: " + new String(this.getContent()));
//        }
        
        // Getters
        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriParts;
        }

        public Map<String, String> getParameters() {
            return params;
        }

        public byte[] getContent() {
            return content;
        }
	}
	
	public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
		String httpCommand = null;
        String uri = null;
        String[] uriParts = null;
        Map<String, String> params = new HashMap<>();
        byte[] content = null;

        // Read the request line (e.g., "GET /api/resource?id=123&name=test HTTP/1.1")
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Invalid HTTP request");
        }

        // Split the request line
        String[] requestParts = requestLine.split(" ");
        if (requestParts.length != 3) {
            throw new IOException("Invalid HTTP request line");
        }

        // Parse httpCommand and URI
        httpCommand = requestParts[0];
        uri = requestParts[1];

        // Parse URI parts and parameters
        String[] uriAndParams = uri.split("\\?");
        uriParts = uriAndParams[0].split("/");

        if (uriAndParams.length > 1) {
            String[] paramPairs = uriAndParams[1].split("&");
            for (String param : paramPairs) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }

        // Skip headers and get content length
        String line;
        int contentLength = 0;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }

        // Read content
        if (contentLength > 0) {
            char[] contentChars = new char[contentLength];
            int readCount = reader.read(contentChars, 0, contentLength);
            if (readCount != contentLength) {
                throw new IOException("Content length mismatch");
            }
            content = new String(contentChars).getBytes();
        }

        return new RequestInfo(httpCommand, uri, uriParts, params, content);
    }

}
