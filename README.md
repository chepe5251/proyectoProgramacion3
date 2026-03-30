# Sistema de Consulta de Padrón Electoral

Sistema que permite consultar el padrón electoral costarricense por número de cédula,
disponible a través de interfaces TCP y HTTP con respuesta en JSON o XML.

---

## Arquitectura por capas

```
src/com/padron/
├── entidades/          Clases del dominio: Persona, Direccion
├── dto/                Objetos de transferencia: SolicitudPadron, RespuestaPadron, FormatoSalida
├── datos/              Repositorios: lectura de PADRON.txt y distelec.txt
├── logica/             Lógica de negocio: ServicioPadron
├── util/               Herramientas transversales: Serializador (JSON/XML)
├── presentacion/
│   ├── tcp/            Servidor TCP: TcpServer
│   └── http/           Servidor HTTP: HttpServerPadron
└── Main.java           Punto de entrada
```

**Regla de dependencias (las capas solo dependen hacia abajo):**

```
presentacion  →  logica  →  datos  →  entidades
                 logica  →  dto
                 util    →  dto
```

---

## Requisitos

- JDK 11 o superior
- Apache Ant 1.10+
- Archivos `PADRON.txt` y `distelec.txt` colocados en `resources/data/` (no van en el repo)

---

## Cómo compilar y ejecutar

```bash
# Compilar
ant compile

# Ejecutar
ant run

# Generar JAR
ant jar

# Limpiar
ant clean
```

---

## Estructura de ramas (trabajo en equipo)

El proyecto está dividido en 4 partes independientes, una por colaborador:

| Dev | Rama                    | Archivos propios                                         | Depende de         |
|-----|-------------------------|----------------------------------------------------------|--------------------|
| 1   | `feature/modelo`        | `entidades/`, `dto/`                                     | —                  |
| 2   | `feature/repositorios`  | `datos/`                                                 | feature/modelo     |
| 3   | `feature/logica`        | `logica/`, `util/Serializador.java`                      | feature/repositorios |
| 4   | `feature/presentacion`  | `presentacion/tcp/`, `presentacion/http/`                | feature/logica     |
| —   | `main`                  | `Main.java`, `README.md`, `build.xml`                    | todos              |

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
ant compile             # verificar que compila
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
- `build.xml` — coordinar cambios en el canal del equipo
- `README.md` — cualquiera puede editar secciones distintas

Si dos personas necesitan el mismo archivo, coordinarlo antes de hacer cambios.

---

## Colocar los archivos de datos

Los archivos `PADRON.txt` y `distelec.txt` NO están en el repositorio (ver `.gitignore`).
Cada desarrollador debe obtenerlos y colocarlos en:

```
resources/data/PADRON.txt
resources/data/distelec.txt
```

---

## Estado del proyecto

| Componente              | Estado       |
|-------------------------|--------------|
| Entidades y DTOs        | Esqueleto    |
| Repositorio Padrón      | Pendiente    |
| Repositorio Distelec    | Pendiente    |
| Lógica de negocio       | Pendiente    |
| Serialización JSON/XML  | Pendiente    |
| Servidor TCP            | Pendiente    |
| Servidor HTTP           | Pendiente    |
