
# Taller diseño y estructuración de aplicaciones distribuidas en internet


En este taller se realizó el diseño y desarrollo de un servidor web básico utilizando Java, enfocado en manejar múltiples solicitudes de forma secuencial (no concurrente). El servidor fue programado para leer archivos del sistema local y responder con diversos tipos de recursos, incluyendo páginas HTML, hojas de estilo CSS, archivos JavaScript e imágenes.

Adicionalmente, se diseñó una aplicación web que integraba estos elementos para probar la funcionalidad del servidor. Esta aplicación incluyó comunicación asíncrona con servicios REST implementados en el backend, proporcionando una interacción dinámica entre el cliente y el servidor. Todo el desarrollo se realizó exclusivamente con librerías de red de Java, evitando el uso de frameworks web como Spark o Spring, con el objetivo de reforzar los fundamentos del desarrollo web y la interacción cliente-servidor.



## Contenidos del repositorio
* **Servidor Web:** Desarrollo de una implementación en Java para gestionar solicitudes HTTP de tipo GET y POST, asegurando un manejo eficiente y estructurado de las peticiones.

* **Archivos Estáticos:** Creación y uso de recursos estáticos esenciales, como un archivo HTML principal (index.html, app.html), una hoja de estilos (styles.css), un archivo de JavaScript (script.js) y una imagen de muestra.

* **Comunicación REST:** Implementación de un endpoint en el servidor para habilitar la comunicación asíncrona y el intercambio dinámico de datos entre el cliente y el servidor, garantizando una interacción fluida y eficiente entre ambos mediante servicios REST.
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
