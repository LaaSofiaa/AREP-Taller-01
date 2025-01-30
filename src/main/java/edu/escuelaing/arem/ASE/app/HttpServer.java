package edu.escuelaing.arem.ASE.app;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Esta clase implementa un servidor HTTP básico que maneja solicitudes GET y POST.
 * Sirve archivos estáticos desde un directorio y proporciona una API sencilla para
 * manejar el nombre del usuario.
 */
public class HttpServer {
    private static final int port = 35000;
    private static final String directory = "src/main/java/resources";
    private static final Map<String, String> dataStore = new HashMap<>();

    /**
     * Método principal que inicia el servidor y espera solicitudes.
     * Escucha conexiones entrantes en el puerto definido y las maneja.
     */
    public static void main(String[] args) {

        /// Crea el servidor que escucha en el puerto
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor escuchando en el puerto: " + port);
            boolean running = true;
            while (running) {
                // Acepta una nueva conexión y maneja la solictud del cliente
                Socket clientSocket = serverSocket.accept();
                handleRequestClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("No se pudo escuchar en el puerto: " + port);
            System.exit(1);
        }
    }

    /**
     * Maneja la solicitud de un cliente y delega la acción según el tipo de solicitud.
     * @param clientSocket El socket de la conexión con el cliente.
     */
    public static void handleRequestClient(Socket clientSocket) {
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

    /**
     * Maneja una solicitud GET.
     * Si la solicitud es para la API /api/hello, responde con un JSON.
     * Si la solicitud es para un archivo estático, lo envía al cliente.
     * @param fileRequested El archivo solicitado.
     * @param dataOut El flujo de salida para enviar los datos.
     * @param out El flujo de salida para enviar las cabeceras HTTP.
     */
    private static void handleGetRequest(String fileRequested, BufferedOutputStream dataOut, PrintWriter out) {
        if (fileRequested.startsWith("/api/hello")) {
            String savedName = dataStore.getOrDefault("name", "usuario"); // Valor por defecto en el map
            // Verificar si se pasó un nombre en la URL como parámetro
            String queryParams = fileRequested.split("\\?").length > 1 ? fileRequested.split("\\?")[1] : "";
            String[] params = queryParams.split("&");

            for (String param : params) {
                if (param.startsWith("name=")) {
                    String name = param.split("=")[1];
                    dataStore.put("name", name); // Guardar el nombre en el dataStore
                    savedName = name;
                }
            }
            // Crea una respuesta JSON con el nombre
            String jsonResponse = "{\"name\": \"" + savedName + "\"}";
            System.out.println("Nombre obtenido: "+jsonResponse);

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: application/json");
            out.println();
            out.flush();
            System.out.println("GET /api/hello procesado exitosamente.");
            return;
        }

        // Manejar archivos estáticos
        File file = new File(directory, fileRequested);
        if (file.exists() && !file.isDirectory()) {
            try {
                String contentType = getType(fileRequested);
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: " + contentType);
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
                System.out.println("Archivo " + fileRequested + " enviado exitosamente.");
            } catch (IOException e) {
                out.println("HTTP/1.1 500 Internal Server Error");
                System.err.println("Error al enviar el archivo " + fileRequested + ": " + e.getMessage());
            }
        } else {
            out.println("HTTP/1.1 404 Not Found");
            System.err.println("Archivo no encontrado: " + fileRequested);
        }
    }

    /**
     * Maneja una solicitud POST.
     * Si la solicitud es para la API /api/updateName, actualiza el nombre en la memoria.
     * @param in El flujo de entrada para leer la solicitud.
     * @param fileRequested El archivo o endpoint solicitado.
     * @param out El flujo de salida para enviar las cabeceras HTTP.
     */
    private static void handlePostRequest(BufferedReader in, String fileRequested, PrintWriter out) {
        if(fileRequested.equals("/api/updateName")) {
            try {
                String line;
                int contentLength = 0;
                while (!(line = in.readLine()).isEmpty()) {
                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.split(":")[1].trim());
                    }
                }

                // Leer el cuerpo de la solicitud
                char[] body = new char[contentLength];
                in.read(body, 0, contentLength);
                String requestBody = new String(body);
                System.out.println("Nombre actualizado: " + requestBody);

                //Extrae el nuevo nombre y lo guarda
                String name = requestBody.replace("{\"name\":\"", "").replace("\"}", "");
                dataStore.put("name", name);


                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/plain");
                out.println();
                System.out.println("POST /api/updateName procesado exitosamente.");

            } catch (IOException e) {
                out.println("HTTP/1.1 500 Internal Server Error");
                System.err.println("Error procesando POST /api/updateName: " + e.getMessage());
            }
        }else{
            out.println("HTTP/1.1 404 Not Found");
            System.err.println("Endpoint no encontrado: " + fileRequested);
        }
    }

    /**
     * Devuelve el tipo MIME correspondiente a una extensión de archivo.
     * @param fileRequested El archivo solicitado.
     * @return El tipo MIME del archivo.
     */
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

    /**
     * Extrae la extensión de un archivo.
     * @param fileRequested El archivo solicitado.
     * @return La extensión del archivo.
     */
    private static String getFileExtension(String fileRequested) {
        int dotIndex = fileRequested.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return fileRequested.substring(dotIndex + 1).toLowerCase();
    }

    public static Map<String, String> getDataStore() {
        return dataStore;
    }
}
