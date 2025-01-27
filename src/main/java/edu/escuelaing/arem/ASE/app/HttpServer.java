package edu.escuelaing.arem.ASE.app;

import java.io.*;
import java.net.*;


public class HttpServer {
    private static final int port = 35000;
    private static final String directory = "src/main/java/resources";

    public static void main(String[] args) {

        // Escucha en un puerto y maneja las conexiones entrantes de los clientes.
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Escuchando en el puerto: " + port);
            boolean running = true;
            while (running) {
                Socket clientSocket = serverSocket.accept();
                handleRequestClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("No se pudo escuchar en el puerto: " + port);
            System.exit(1);
        }
    }

    private static void handleRequestClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine != null) {
                System.out.println("Solicitud recibida: " + requestLine);
                // Divide la línea de la solicitud en partes: método y recurso.
                String[] tokens = requestLine.split(" ");
                String method = tokens[0];
                String fileRequested = tokens[1];

                if (fileRequested.equals("/")) {
                    fileRequested = "/index.html";
                }

                if (method.equals("GET")) {
                    handleGetRequest(fileRequested, dataOut, out);
                } else if (method.equals("POST")) {
                    handlePostRequest(in, fileRequested, out);
                } else {
                    out.println("HTTP/1.1 501 Not Implemented");
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo procesar la solicitud.");
            System.exit(1);
        }
    }

    private static void handleGetRequest(String fileRequested, BufferedOutputStream dataOut, PrintWriter out) {
        if (fileRequested.startsWith("/api/hello")) {
            // Respuesta estática desde el servidor
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/plain");
            out.println();
            out.flush();
            return;
        }

        // Manejar archivos estáticos
        File file = new File(directory, fileRequested);
        if (file.exists() && !file.isDirectory()) {
            try {
                String contentType = getType(fileRequested);
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: " + contentType);
                out.println("Content-Length: " + file.length());
                out.println();
                out.flush();

                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    dataOut.write(buffer, 0, bytesRead);
                }
                dataOut.flush();
                fileInputStream.close();
            } catch (IOException e) {
                out.println("HTTP/1.1 500 Internal Server Error");
            }
        } else {
            out.println("HTTP/1.1 404 Not Found");
        }
    }



    private static void handlePostRequest(BufferedReader in, String fileRequested, PrintWriter out) {
        try {
            // Leer las cabeceras de la solicitud
            String line;
            int contentLength = 0;
            while (!(line = in.readLine()).isEmpty()) {
                System.out.println("Cabecera: " + line);
                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            // Leer el cuerpo de la solicitud
            char[] body = new char[contentLength];
            in.read(body, 0, contentLength);
            String requestBody = new String(body);
            System.out.println("Cuerpo de la solicitud: " + requestBody);

            // Responder al cliente
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/plain");
            out.println("Content-Length: 2");
            out.println();
            out.println("OK");
        } catch (IOException e) {
            out.println("HTTP/1.1 500 Internal Server Error");
        }
    }

    private static String getType(String fileRequested) {
        String extension = getFileExtension(fileRequested);
        switch (extension) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            default:
                return "application/octet-stream";
        }
    }

    private static String getFileExtension(String fileRequested) {
        int dotIndex = fileRequested.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return fileRequested.substring(dotIndex + 1).toLowerCase();
    }
}
