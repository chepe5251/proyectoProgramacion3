# Informe Técnico

## Arquitectura y decisiones

El sistema se organizó por capas para separar responsabilidades:

- `entidades`: modelo de dominio (`Persona`, `Direccion`)
- `dto`: contratos de entrada y salida (`SolicitudPadron`, `RespuestaPadron`, `FormatoSalida`)
- `datos`: carga inicial y consulta en memoria de `PADRON.txt` y `distelec.txt`
- `logica`: validación, normalización de cédula y orquestación de repositorios
- `presentacion`: servidores TCP y HTTP con sockets Java puros
- `util`: serialización manual JSON/XML con escape básico

Los archivos del padrón se cargan una sola vez al iniciar la aplicación y se
mantienen en `HashMap` para privilegiar el tiempo de consulta. Esa decisión mueve
el costo al arranque, pero evita lecturas repetidas de disco en cada solicitud.

## Protocolo y transporte

El servidor TCP usa el formato `GET|cedula|formato` y atiende clientes concurrentes
con un `ExecutorService` de tamaño fijo.

El servidor HTTP implementa un subconjunto simple de HTTP/1.1 sobre `ServerSocket`
y soporta:

- `GET /padron?cedula=...&formato=json|xml`
- `GET /padron/{cedula}?formato=json|xml`
- alias `format` además de `formato`
- respuestas `200`, `400`, `404` y `405`

## Validaciones y manejo de errores

La lógica de negocio normaliza la cédula antes de validarla, removiendo espacios
y separadores no numéricos. Después exige exactamente 9 dígitos.

Las respuestas de error se serializan igual que las exitosas para mantener un
contrato uniforme en TCP y HTTP. La clase `Serializador` aplica escape básico de
caracteres especiales en JSON y XML para evitar respuestas inválidas.

## Pruebas y ajustes realizados

Se agregaron pruebas JUnit para cubrir:

- normalización de cédula
- serialización con apellidos y escape básico
- soporte de `/padron/{cedula}` y respuesta `405` en HTTP

Los principales ajustes hechos a partir de la rúbrica fueron:

- incluir apellidos y nombre completo en la respuesta
- adaptar TCP al formato `GET|cedula|formato`
- agregar concurrencia con `ExecutorService` en TCP y HTTP
- soportar `/padron/{cedula}` y `405 Method Not Allowed`
