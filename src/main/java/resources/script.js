// Función para realizar solicitud GET asincrónica
async function performGet(event) {
    event.preventDefault();
    const name = document.getElementById('getName').value; // nombre actual
    try {
        // Solicitud GET al servidor
        const response = await fetch('/api/hello');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.text();

        // Genera la respuesta personalizada en el cliente
        const message = `¡Hola, ${name}! ${data}`;
        document.getElementById('getResponse').innerHTML = message;
    } catch (error) {
        console.error('Error en la solicitud GET:', error);
        document.getElementById('getResponse').innerHTML = `¡Hola, ${name}! Hubo un problema al procesar tu solicitud.`;
    }
}

// Función para realizar solicitud POST asincrónica
async function performPost(event) {
    event.preventDefault();
    const newName = document.getElementById('postName').value;
    try {
        const response = await fetch('/api/updateName', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: newName }) // nombre actualizado
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.text();

        // Muestra el nombre actualizado en la respuesta
        document.getElementById('postResponse').innerHTML = `Tu nombre ha sido actualizado a: "${newName}". ${data}`;

        // Actualiza el valor del campo 'getName' con el nuevo nombre
        const message = `¡Hola, ${newName}!`;
        document.getElementById('getResponse').innerHTML = message;

    } catch (error) {
        console.error('Error en la solicitud POST:', error);
        // Si hay un error, muestra un mensaje de error
        document.getElementById('postResponse').innerHTML = `¡Error! No se pudo actualizar el nombre: ${error.message}`;
    }
}

// Event listeners para los formularios
document.getElementById('getForm').addEventListener('submit', performGet);
document.getElementById('postForm').addEventListener('submit', performPost);

// Función para simular la carga del diagrama
function loadDiagram() {
    const img = document.getElementById('clientServerDiagram');
    img.src = 'img.jpg'; // Reemplaza con una URL real
}

// Cargar el diagrama cuando la página esté lista
document.addEventListener('DOMContentLoaded', loadDiagram);
