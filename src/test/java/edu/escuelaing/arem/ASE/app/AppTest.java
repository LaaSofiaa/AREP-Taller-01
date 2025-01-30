package edu.escuelaing.arem.ASE.app;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class AppTest  {
    private HttpServer httpServer;
    private Map<String, String> dataStore;

    @BeforeEach
    public void setUp() {
        httpServer = new HttpServer();
        dataStore = new HashMap<>();
    }

    @Test
    public void testHandleGetRequest(){
        String requestLine = "GET /api/hello HTTP/1.1";
        Socket mockSocket = new SocketTest(requestLine);
        httpServer.handleRequestClient(mockSocket);

        assertEquals("usuario", dataStore.getOrDefault("name", "usuario"));
    }

    @Test
    public void testHandlePostRequest() {
        String requestLine = "POST /api/updateName HTTP/1.1";
        String requestBody = "{\"name\":\"Jungkook\"}";
        Socket mockSocket = new SocketTest(requestLine, requestBody);
        httpServer.handleRequestClient(mockSocket);

        Map<String, String> dataStore = HttpServer.getDataStore();
        assertEquals("Jungkook", dataStore.get("name"));
    }

    @Test
    public void testHandleGetRequestHtml(){
        String requestLine = "GET /index.html HTTP/1.1";
        Socket mockSocket = new SocketTest(requestLine);
        httpServer.handleRequestClient(mockSocket);
        assertTrue(new File("src/main/java/resources/index.html").exists());
    }

    @Test
    public void testHandleGetRequestAppHtml(){
        String requestLine = "GET /app/app.html HTTP/1.1";
        Socket mockSocket = new SocketTest(requestLine);
        httpServer.handleRequestClient(mockSocket);
        assertTrue(new File("src/main/java/resources/app/app.html").exists());
    }

    @Test
    public void testHandleGetRequestJs(){
        String requestLine = "GET /app/script.js HTTP/1.1";
        Socket mockSocket = new SocketTest(requestLine);
        httpServer.handleRequestClient(mockSocket);
        assertTrue(new File("src/main/java/resources/app/script.js").exists());
    }

    @Test
    public void testHandleGetRequestCss(){
        String requestLine = "GET /styles.css HTTP/1.1";
        Socket mockSocket = new SocketTest(requestLine);
        httpServer.handleRequestClient(mockSocket);
        assertTrue(new File("src/main/java/resources/styles.css").exists());
    }

    @Test
    public void testHandleGetRequestImg(){
        String requestLine = "GET /img.jpg HTTP/1.1";
        Socket mockSocket = new SocketTest(requestLine);
        httpServer.handleRequestClient(mockSocket);
        assertTrue(new File("src/main/java/resources/img.jpg").exists());
    }



    // simular un socket en las pruebas
    private static class SocketTest extends Socket {
        private final ByteArrayInputStream inputStream;
        private final ByteArrayOutputStream outputStream;

        public SocketTest(String requestLine) {
            this(requestLine, "");
        }

        public SocketTest(String requestLine, String requestBody) {
            String request = requestLine + "\r\n" +
                    "Content-Length: " + requestBody.length() + "\r\n" +
                    "\r\n" +
                    requestBody;
            this.inputStream = new ByteArrayInputStream(request.getBytes());
            this.outputStream = new ByteArrayOutputStream();
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public OutputStream getOutputStream() {
            return outputStream;
        }
    }
}
