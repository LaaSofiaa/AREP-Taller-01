
# Taller diseño y estructuración de aplicaciones distribuidas en internet


En este taller se realizó el diseño y desarrollo de un servidor web básico utilizando Java, enfocado en manejar múltiples solicitudes de forma secuencial (no concurrente). El servidor fue programado para leer archivos del sistema local y responder con diversos tipos de recursos, incluyendo páginas HTML, hojas de estilo CSS, archivos JavaScript e imágenes.

Adicionalmente, se diseñó una aplicación web que integraba estos elementos para probar la funcionalidad del servidor. Esta aplicación incluyó comunicación asíncrona con servicios REST implementados en el backend, proporcionando una interacción dinámica entre el cliente y el servidor. Todo el desarrollo se realizó exclusivamente con librerías de red de Java, evitando el uso de frameworks web como Spark o Spring, con el objetivo de reforzar los fundamentos del desarrollo web y la interacción cliente-servidor.



## Contenidos del repositorio
* **Servidor Web:** Desarrollo de una implementación en Java para gestionar solicitudes HTTP de tipo GET y POST, asegurando un manejo eficiente y estructurado de las peticiones.

* **Archivos Estáticos:** Creación y uso de recursos estáticos esenciales, como archivos HTML (index.html, app.html), una hoja de estilos (styles.css), un archivo de JavaScript (script.js) y una imagen de muestra.

* **Comunicación REST:** Implementación de unos endpoints en el servidor para habilitar la comunicación asíncrona y el intercambio dinámico de datos entre el cliente y el servidor, garantizando una interacción fluida y eficiente entre ambos mediante servicios REST.
  
## Instalación

**1.**  Clonar el repositorio

```bash
  git clone https://github.com/tu_usuario/AREP-Taller-01.git

  cd AREP-Taller-01
```
**2.**  Construir el proyecto mediante maven, donde debes tener previamente instalado este https://maven.apache.org .
```bash
  mvn clean install
```  
Esto compilará el código Java y lo empaquetará en un archivo JAR.

## Ejecutar el programa
**1.**  Iniciar el servidor desde la carpeta donde se guardo el programa, por medio de la consola.
```bash
  mvn exec:java -Dexec.mainClass="edu.escuelaing.arem.ASE.app.HttpServer"
```
Otra alternativa sería abrir su IDE de preferencia y seleccionar el icono de run.

**2.** Acceder a la aplicación web

Abrir su navegador web y navegue por el siguiente link http://localhost:35000 para mirar la aplicacón en uso. 

## Arquitectura del Proyecto 
**Estructura del directorio**
El directorio del proyecto esta organizado de la siguiente manera:

![image](https://github.com/user-attachments/assets/7ec2dd45-9b0a-4c93-b020-fbaad55c483a)

**Componente Principal**
- *HttpServer* : El HttpServer que se presenta en el código es un servidor HTTP básico implementado en Java. Este servidor escucha solicitudes de los clientes en un puerto específico y responde a esas solicitudes, manejando principalmente dos tipos de solicitudes: GET y POST. 

### Flujo de Datos

**Servidor HTTP:** Este servidor HTTP simple escucha solicitudes en el puerto `35000` y maneja tanto archivos estáticos como solicitudes de API REST.

**Recepción de Solicitudes**

El servidor escucha en el puerto `35000` utilizando `ServerSocket`. Cuando se recibe una solicitud, se crea un `Socket` para gestionar la conexión. El método `handleRequestClient(Socket clientSocket)` se encarga de procesar la solicitud.

**Procesamiento de la Solicitud**

*Solicitudes GET:*

- **Archivos Estáticos**: Si la solicitud es para un archivo estático (por ejemplo, `index.html`, `img.jpg`,`styles.css `,`script.js`), el servidor busca el archivo en el directorio `src/main/java/resources`. Si el archivo existe, se envía con el tipo de contenido adecuado (por ejemplo, `text/html` para un archivo HTML). Si no se encuentra el archivo, el servidor responde con un **404 Not Found**.
  
- **API REST**: Si la solicitud es para un endpoint de la API (por ejemplo, `/api/hello`), el servidor responde con un JSON. Si no se pasa un nombre como parámetro, el servidor utiliza "usuario" como valor por defecto en el JSON.

*Solicitudes POST:*

- **Actualización de Nombre**: El servidor maneja solicitudes POST para actualizar un nombre. Se espera que el cuerpo de la solicitud contenga un JSON. El nombre enviado en la solicitud se guarda en un `dataStore` y el servidor responde con un mensaje de éxito.

**Envío de Respuestas**

- **Archivos Estáticos**: El servidor responde con el archivo solicitado, utilizando el código de estado **200 OK** y el tipo de contenido adecuado (por ejemplo, `text/html` o `application/javascript`).

- **API REST (JSON)**: Si la solicitud es para un endpoint de la API, el servidor responde con el código de estado **200 OK** y el contenido JSON adecuado.


## Dependencias

- **Biblioteca estándar de Java**: Utilizada para la gestión de redes, manejo de archivos y procesamiento concurrente.
  
- **Maven**: Utilizado para la gestión de dependencias y la automatización de la compilación. La configuración se especifica en el archivo `pom.xml`.

## EndPoints

- **GET /api/hello**: Este endpoint devuelve un JSON con un saludo. Si no se pasa un nombre como parámetro, el valor por defecto es "usuario".
  
- **POST /api/updateName**: Este endpoint permite actualizar el nombre del usuario. Se espera que el cuerpo de la solicitud contenga un JSON con el nuevo nombre.

## Archivos Estáticos

**1. `app.html` :**  Demuestra las solicitudes asincrónicas REST. Contiene formularios para realizar solicitudes **GET** (obtener datos) y **POST** (actualizar datos). Los resultados se muestran dinámicamente en la misma página.

**2. `script.js` :** Maneja las solicitudes **GET** y **POST** de manera asincrónica, procesando las respuestas del servidor y actualizando la interfaz con el mensaje correspondiente.

**3. `img.jpg` :** Imagen utilizada en la interfaz, probablemente como diagrama o ilustración.

**4. `index.html`:** Explica el modelo **Cliente-Servidor** y ofrece un enlace a la página `app.html` para probar los servicios asincrónicos.

**5. `styles.css` :** Define el estilo de la interfaz, con colores y diseños para los formularios, botones y respuestas, mejorando la experiencia del usuario.


## Pruebas

Para verificar el funcionamiento del servidor web y de la aplicación, realice las siguientes pruebas:

- Servir archivos estáticos - Navegue a http://localhost:35000 y se aseguro de que la página se carga correctamente `index.html`  .
![image](https://github.com/user-attachments/assets/f4c136f7-2867-4d41-878a-abb9e8436764)

- Mirar si el archivo `styles.css` es aplicado correctamente.
![image](https://github.com/user-attachments/assets/922dcbb1-9a9e-4ed2-8744-07b00570f6a5)

- Mirar si la imagen se cargo correctamente `img.jpg`.
![image](https://github.com/user-attachments/assets/6beeffb7-8650-4054-8e45-79286bf3b0c0)

- Ahora para acceder a la desmostración de los servicios asincrónicos REST y la carga de la página `app.html`.
![image](https://github.com/user-attachments/assets/1158aaac-e746-4655-9699-cfc922ab9279)

- Mirar si el archivo `script.js` se cargo correctamente.
![image](https://github.com/user-attachments/assets/1e0f7d81-7689-4f00-aac7-d1f0b4ad9201)

- Solicitud GET de prueba: Se hace click en el botón «Enviar GET» de la página web para asegurarse de que los datos obtenidos del se muestran correctamente, saludando al usuario como confirmación.
![image](https://github.com/user-attachments/assets/8a9d88e2-0282-4bb4-acc0-fc145d892597)

- Solicitud POST de prueba: Se hace click en el botón «Enviar POST» de la página web para actualizar el nombre del usuario, mostrando un mensaje de confirmación. 
![image](https://github.com/user-attachments/assets/1fc6afbf-36f0-4d8f-b9c3-2837d3fbb188)

## Autor

**Laura Gil** - Desarrolladora y autora del proyecto. 
