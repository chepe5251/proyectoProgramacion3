# Sistema de Consulta de Padrón Electoral

Sistema que permite consultar el padrón electoral costarricense por número de cédula,
disponible a través de interfaces TCP y HTTP con respuesta en JSON o XML.

---

## Integrantes

| Nombre                          | GitHub     |
|---------------------------------|------------|
| Alejandro Rodríguez Sanabria    | Chepe5251  |
| Cristian Meléndez García        | akatosh92  |
| Pablo Enrique Delgado Miranda   | Pabloedm88 |
| Santiago Vallejos Arias         |            |
--

## Requisitos

- JDK 17 o superior (probado con JDK 24)
- Apache Maven 3.8+
- Archivos `PADRON.txt` y `distelec.txt` en `src/main/resources/data/` (no van en el repo)

### Obtener los archivos de datos

1. Ir a [https://www.tse.go.cr](https://www.tse.go.cr)
2. Navegar a **Servicios → Padrón Electoral → Descarga del Padrón**
3. Descargar `PADRON_COMPLETO.zip` y `distelec.zip`, descomprimir y colocar en:

```
src/main/resources/data/PADRON.txt
src/main/resources/data/distelec.txt
```

| Archivo        | Separador | Campos en orden                                                       |
|----------------|-----------|-----------------------------------------------------------------------|
| `PADRON.txt`   | coma `,`  | cedula, codelec, fechacaduc, nombre, primerApellido, segundoApellido  |
| `distelec.txt` | coma `,`  | codele, provincia, canton, distrito                                   |

---

## Cómo compilar y ejecutar

```bash
# 1. Compilar
mvn compile

# 2. Ejecutar pruebas
mvn test

# 3. Generar JAR ejecutable
mvn package

# 4a. Ejecutar con rutas por defecto (src/main/resources/data/)
java -jar target/padron-electoral-1.0-SNAPSHOT.jar

# 4b. Ejecutar con rutas personalizadas
java -jar target/padron-electoral-1.0-SNAPSHOT.jar /ruta/PADRON.txt /ruta/distelec.txt

# Limpiar artefactos
mvn clean
```

Al iniciar correctamente verás:

```
=== Sistema de Consulta de Padron Electoral ===
Ruta PADRON.txt  : src/main/resources/data/PADRON.txt
Ruta distelec.txt: src/main/resources/data/distelec.txt
Padron cargado: 3740286 personas.
Distelec cargado: 2179 distritos.
Servidor TCP escuchando en puerto 5000
Servidor HTTP escuchando en puerto 8080
Presiona Ctrl+C para detener.
```

El sistema queda escuchando en:
- **TCP:** puerto `5000`
- **HTTP:** puerto `8080`

Detener con `Ctrl+C` (cierre limpio de ambos servidores).

---

## Protocolo TCP

El servidor TCP escucha en el **puerto 5000** usando sockets Java puros (sin frameworks).
El protocolo es de texto plano, una línea por solicitud.
Cada conexión entrante se procesa con un `ExecutorService` de tamaño fijo.

### Formato de solicitud

```
GET|<CEDULA>|<FORMATO>\n
```

| Posición | Requerido | Valores               |
|----------|-----------|-----------------------|
| `GET`    | Sí        | Literal `GET`         |
| `cedula` | Sí        | Exactamente 9 dígitos |
| `formato`| Sí        | `json` o `xml`        |

- Los campos se separan con `|`.
- La línea debe terminar en `\n` (o `\r\n`).
- La conexión se cierra automáticamente tras la respuesta.

### Formato de respuesta

La respuesta es el cuerpo serializado directamente (JSON o XML), seguido de `\n`.
No hay cabeceras ni envoltura adicional.

### Flujo de conexión

```
Cliente                        Servidor TCP :5000
  |  ----  conectar TCP  ---->  |
  |  ---- "GET|109870456      |
  |         |json\n" --------> |
  |  <---  {JSON o XML}\n  ----  |
  |  <---  [conexión cerrada] -- |
```

### Errores

Si la cédula no existe o el formato es inválido, la respuesta sigue siendo JSON/XML
pero con `"exito": false` y un campo `"mensaje"` descriptivo.

---

## Endpoints HTTP

El servidor HTTP escucha en el **puerto 8080** usando sockets Java puros.
Implementa un subconjunto mínimo de HTTP/1.1 (solo `GET`).
Cada conexión entrante se procesa con un `ExecutorService` de tamaño fijo.

### `GET /padron`

Consulta una persona en el padrón por su número de cédula.

#### Parámetros de query

| Parámetro | Requerido | Descripción                          |
|-----------|-----------|--------------------------------------|
| `cedula`  | Sí        | Número de cédula (exactamente 9 dígitos) |
| `formato` | No        | `json` (default) o `xml`             |

#### Códigos de respuesta HTTP

| Código | Significado                                              |
|--------|----------------------------------------------------------|
| `200`  | Consulta exitosa, persona encontrada                     |
| `400`  | Solicitud inválida (cédula faltante, formato incorrecto, longitud incorrecta) |
| `404`  | Cédula no encontrada en el padrón                        |

---

## Ejemplos de requests/responses

### TCP — Consulta exitosa (JSON)

**Request** (enviar por socket al puerto 5000):
```
GET|109870456|json
```

**Response:**
```json
{
  "exito": true,
  "cedula": "109870456",
  "nombre": "MARIA FERNANDA",
  "primerApellido": "JIMENEZ",
  "segundoApellido": "ROJAS",
  "nombreCompleto": "MARIA FERNANDA JIMENEZ ROJAS",
  "provincia": "SAN JOSE",
  "canton": "SAN JOSE",
  "distrito": "CARMEN"
}
```

---

### TCP — Consulta exitosa (XML)

**Request:**
```
GET|109870456|xml
```

**Response:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<respuesta>
  <exito>true</exito>
  <cedula>109870456</cedula>
  <nombre>MARIA FERNANDA</nombre>
  <primerApellido>JIMENEZ</primerApellido>
  <segundoApellido>ROJAS</segundoApellido>
  <nombreCompleto>MARIA FERNANDA JIMENEZ ROJAS</nombreCompleto>
  <provincia>SAN JOSE</provincia>
  <canton>SAN JOSE</canton>
  <distrito>CARMEN</distrito>
</respuesta>
```

---

### TCP — Cédula no encontrada

**Request:**
```
GET|000000001|json
```

**Response:**
```json
{
  "exito": false,
  "codigoError": "404",
  "mensaje": "Cedula no encontrada en el padron."
}
```

---

### TCP — Cédula inválida (longitud incorrecta)

**Request:**
```
GET|12345|json
```

**Response:**
```json
{
  "exito": false,
  "codigoError": "400",
  "mensaje": "La cedula debe tener exactamente 9 digitos."
}
```

---

### HTTP — Consulta exitosa (JSON)

**Request:**
```
GET /padron?cedula=109870456&formato=json HTTP/1.1
Host: localhost:8080
```

O con curl:
```bash
curl "http://localhost:8080/padron?cedula=109870456&formato=json"
```

**Response:**
```
HTTP/1.1 200 OK
Content-Type: application/json; charset=UTF-8
Connection: close

{
  "exito": true,
  "cedula": "109870456",
  "nombre": "MARIA FERNANDA",
  "primerApellido": "JIMENEZ",
  "segundoApellido": "ROJAS",
  "nombreCompleto": "MARIA FERNANDA JIMENEZ ROJAS",
  "provincia": "SAN JOSE",
  "canton": "SAN JOSE",
  "distrito": "CARMEN"
}
```

---

### HTTP — Consulta exitosa (XML)

**Request:**
```bash
curl "http://localhost:8080/padron?cedula=109870456&formato=xml"
```

**Response:**
```
HTTP/1.1 200 OK
Content-Type: application/xml; charset=UTF-8
Connection: close

<?xml version="1.0" encoding="UTF-8"?>
<respuesta>
  <exito>true</exito>
  <cedula>109870456</cedula>
  <nombre>MARIA FERNANDA</nombre>
  <primerApellido>JIMENEZ</primerApellido>
  <segundoApellido>ROJAS</segundoApellido>
  <nombreCompleto>MARIA FERNANDA JIMENEZ ROJAS</nombreCompleto>
  <provincia>SAN JOSE</provincia>
  <canton>SAN JOSE</canton>
  <distrito>CARMEN</distrito>
</respuesta>
```

---

### HTTP — Cédula no encontrada (404)

**Request:**
```bash
curl "http://localhost:8080/padron?cedula=000000001"
```

**Response:**
```
HTTP/1.1 404 Not Found
Content-Type: application/json; charset=UTF-8
Connection: close

{
  "exito": false,
  "codigoError": "404",
  "mensaje": "Cedula no encontrada en el padron."
}
```

---

### HTTP — Solicitud inválida (400)

**Request (cédula faltante):**
```bash
curl "http://localhost:8080/padron"
```

**Response:**
```
HTTP/1.1 400 Bad Request
Content-Type: application/json; charset=UTF-8
Connection: close

{
  "exito": false,
  "codigoError": "400",
  "mensaje": "Parametro 'cedula' requerido."
}
```

---

### HTTP — Formato omitido (default JSON)

```bash
curl "http://localhost:8080/padron?cedula=109870456"
```

Responde en JSON (comportamiento idéntico a `formato=json`).

---

## Arquitectura

```
presentacion  →  logica  →  datos  →  entidades
              →  util    →  dto
```

```
src/main/java/com/padron/
├── entidades/
│   ├── Persona.java        cedula, codElectoral, nombre, primerApellido, segundoApellido
│   └── Direccion.java      codElectoral, provincia, canton, distrito
├── dto/
│   ├── FormatoSalida.java  enum JSON | XML
│   ├── SolicitudPadron.java
│   └── RespuestaPadron.java
├── datos/
│   ├── RepositorioPadron.java    carga y búsqueda en PADRON.txt  (~3.7M registros)
│   └── RepositorioDistelec.java  carga y búsqueda en distelec.txt (~2179 distritos)
├── logica/
│   └── ServicioPadron.java       valida cédula, orquesta repositorios
├── util/
│   └── Serializador.java         JSON y XML manual con StringBuilder
├── presentacion/
│   ├── tcp/   TcpServer.java        puerto 5000
│   └── http/  HttpServerPadron.java puerto 8080
└── Main.java                     punto de entrada
```

No se usan frameworks externos: sockets Java puros, serialización manual, repositorios en `HashMap`.

---

## Addendum de evaluaciÃ³n

Cambios incorporados para alinear el proyecto con la rÃºbrica:

- normalizaciÃ³n de cÃ©dula antes de validar y consultar
- soporte HTTP para `GET /padron/{cedula}`
- soporte de `405 Method Not Allowed`
- soporte del alias `format` ademÃ¡s de `formato`
- pruebas JUnit en `src/test/java`
- informe tÃ©cnico adicional en `INFORME_TECNICO.md`
