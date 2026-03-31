# Sistema de Consulta de Padrón Electoral

Sistema que permite consultar el padrón electoral costarricense por número de cédula,
disponible a través de interfaces TCP y HTTP con respuesta en JSON o XML.

---

## Arquitectura por capas

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
│   ├── RepositorioPadron.java    carga y búsqueda en PADRON.txt
│   └── RepositorioDistelec.java  carga y búsqueda en distelec.txt
├── logica/
│   └── ServicioPadron.java       orquesta repositorios y produce RespuestaPadron
├── util/
│   └── Serializador.java         convierte RespuestaPadron a JSON o XML
├── presentacion/
│   ├── tcp/   TcpServer.java     escucha en puerto 5000
│   └── http/  HttpServerPadron.java  escucha en puerto 8080
└── Main.java                     punto de entrada

src/main/resources/data/
└── (colocar aquí PADRON.txt y distelec.txt — no van en el repo)
```

**Regla de dependencias (las capas solo dependen hacia abajo):**

```
presentacion  →  logica  →  datos  →  entidades
                 logica  →  dto
                 util    →  dto
```

---

## Requisitos

- JDK 17 o superior (probado con JDK 24)
- Apache Maven 3.8+
- Archivos `PADRON.txt` y `distelec.txt` colocados en `src/main/resources/data/` (no van en el repo)

---

## Cómo compilar y ejecutar

```bash
# Compilar
mvn compile

# Ejecutar pruebas
mvn test

# Generar JAR ejecutable
mvn package

# Ejecutar (sin argumentos — usa rutas por defecto)
java -jar target/padron-electoral-1.0-SNAPSHOT.jar

# Ejecutar con rutas personalizadas a los archivos de datos
java -jar target/padron-electoral-1.0-SNAPSHOT.jar /ruta/a/PADRON.txt /ruta/a/distelec.txt

# Limpiar
mvn clean
```

Una vez corriendo, el sistema escucha en:
- **TCP:** puerto `5000` — enviar `cedula=XXXXXXXXX&formato=json`
- **HTTP:** puerto `8080` — `GET /padron?cedula=XXXXXXXXX&formato=json`

---

## Estructura de ramas (trabajo en equipo)

El proyecto está dividido en 4 partes independientes, una por colaborador:

| Dev | Rama                    | Archivos propios                                         | Depende de         |
|-----|-------------------------|----------------------------------------------------------|--------------------|
| 1   | `feature/modelo`        | `entidades/`, `dto/`                                     | —                  |
| 2   | `feature/repositorios`  | `datos/`                                                 | feature/modelo     |
| 3   | `feature/logica`        | `logica/`, `util/Serializador.java`                      | feature/repositorios |
| 4   | `feature/presentacion`  | `presentacion/tcp/`, `presentacion/http/`                | feature/logica     |
| —   | `main`                  | `Main.java`, `README.md`, `pom.xml`                      | todos              |

> **Regla clave:** nadie toca archivos de otro desarrollador.
> Si se necesita un cambio en código ajeno, se abre un issue y se coordina.

**Orden de merges recomendado** (respetar dependencias):

```
feature/modelo
      ↓
feature/repositorios
      ↓
feature/logica          ← incluye serialización JSON/XML
      ↓
feature/presentacion    ← TCP y HTTP juntos
      ↓
main (integración final en Main.java)
```

---

## Reglas de commits

### Formato del mensaje

```
<tipo>(<alcance>): <descripción corta en imperativo>

[cuerpo opcional: qué y por qué, no el cómo]
```

### Tipos válidos

| Tipo       | Cuándo usarlo                              |
|------------|--------------------------------------------|
| `feat`     | Nueva funcionalidad                        |
| `fix`      | Corrección de bug                          |
| `refactor` | Cambio que no agrega ni arregla nada       |
| `test`     | Agregar o corregir pruebas                 |
| `docs`     | Cambios solo en documentación              |
| `build`    | Cambios en build.xml o configuración       |
| `chore`    | Tareas menores (gitignore, limpieza, etc.) |

### Ejemplos

```
feat(datos): implementar carga de PADRON.txt en RepositorioPadron
fix(logica): corregir validación de cédula con menos de 9 dígitos
feat(tcp): parsear solicitud en formato cedula=X&formato=Y
docs(readme): agregar instrucciones de instalación
```

### Commits pequeños (obligatorio)

- Un commit = un cambio lógico
- Si el mensaje necesita "y" para describirse, dividirlo
- Nunca commitear archivos generados (`build/`, `dist/`, `*.class`)

---

## Flujo de trabajo en equipo

### 1. Antes de empezar a trabajar

```bash
git checkout main
git pull origin main
git checkout -b feature/mi-tarea
```

### 2. Durante el desarrollo

```bash
# Commits frecuentes y pequeños
git add src/com/padron/datos/RepositorioPadron.java
git commit -m "feat(datos): implementar parsearLinea en RepositorioPadron"

git add src/com/padron/datos/RepositorioPadron.java
git commit -m "feat(datos): implementar cargarDesdeArchivo en RepositorioPadron"
```

### 3. Antes de abrir Pull Request

```bash
git checkout main
git pull origin main
git checkout feature/mi-tarea
git merge main          # resolver conflictos si los hay
mvn compile             # verificar que compila
```

### 4. Pull Request

- Título claro: `feat(datos): implementar repositorios de PADRON y distelec`
- Descripción: qué se hizo, cómo probarlo
- Pedir review a al menos un compañero

---

## Cómo evitar conflictos

Cada desarrollador tiene archivos "propios" según la tabla de ramas.
Los únicos archivos compartidos son:

- `Main.java` — solo el líder toca este archivo
- `pom.xml` — coordinar cambios en el canal del equipo
- `README.md` — cualquiera puede editar secciones distintas

Si dos personas necesitan el mismo archivo, coordinarlo antes de hacer cambios.

---

## Colocar los archivos de datos

Los archivos `PADRON.txt` y `distelec.txt` **no están en el repositorio** (superan el límite de GitHub o son datos sensibles). Cada desarrollador debe descargarlos del TSE y colocarlos manualmente.

### Dónde descargar

1. Ir a [https://www.tse.go.cr](https://www.tse.go.cr)
2. Navegar a **Servicios → Padrón Electoral → Descarga del Padrón**
3. Descargar:
   - **PADRON_COMPLETO.zip** — padrón con todos los electores (~193 MB descomprimido)
   - **distelec.zip** — catálogo de distritos electorales (~172 KB descomprimido)

### Qué hacer con los archivos descargados

1. Descomprimir ambos ZIP.
2. Renombrar el archivo del padrón a `PADRON.txt` si viene con otro nombre (p. ej. `PADRON_COMPLETO.txt`).
3. Colocarlos en:

```
src/main/resources/data/PADRON.txt
src/main/resources/data/distelec.txt
```

### Formato esperado

| Archivo        | Separador | Campos (en orden)                                                          |
|----------------|-----------|----------------------------------------------------------------------------|
| `PADRON.txt`   | coma `,`  | cedula, codelec, fechacaduc, nombre, primerApellido, segundoApellido       |
| `distelec.txt` | coma `,`  | codele, provincia, canton, distrito                                         |

O bien pasar las rutas como argumentos al ejecutar (ver sección de ejecución arriba).

---

## Estado del proyecto

| Componente              | Estado     | Notas                                                                      |
|-------------------------|------------|----------------------------------------------------------------------------|
| Entidades y DTOs        | Completado | `Persona`, `Direccion`, `FormatoSalida`, `SolicitudPadron`, `RespuestaPadron` |
| Repositorio Padrón      | Completado | carga 3 740 286 personas desde PADRON.txt                                  |
| Repositorio Distelec    | Completado | carga 2 179 distritos desde distelec.txt                                   |
| Lógica de negocio       | Completado | `consultarPadron()` y `validarCedula()` implementados                      |
| Serialización JSON/XML  | Completado | JSON y XML generados manualmente con StringBuilder                         |
| Servidor TCP            | Completado | puerto 5000                                                                |
| Servidor HTTP           | Completado | puerto 8080                                                                |
