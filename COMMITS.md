# Plan de Commits — Padrón Electoral (4 colaboradores)

Guía de commits pequeños distribuidos por rama y desarrollador.
Cada commit debe poder hacerse de forma independiente.

---

## Rama: `main` (base del proyecto — líder del equipo)

| # | Mensaje de commit                                        | Archivos incluidos                            |
|---|----------------------------------------------------------|-----------------------------------------------|
| 1 | `build: agregar estructura base del proyecto Ant`        | `build.xml`, `build.properties`, `.gitignore` |
| 2 | `docs: agregar README con arquitectura y guía de equipo` | `README.md`                                   |
| 3 | `docs: agregar plan de commits en COMMITS.md`            | `COMMITS.md`                                  |
| 4 | `feat: agregar esqueleto de Main.java`                   | `src/com/padron/Main.java`                    |
| 5 | `build: agregar directorio resources/data con README`    | `resources/data/README.txt`                   |

---

## Dev 1 — Rama: `feature/modelo`

Crea esta rama desde `main` desde el inicio (no necesita esperar a nadie).

| # | Mensaje de commit                                            | Archivos incluidos        |
|---|--------------------------------------------------------------|---------------------------|
| 1 | `feat(entidades): agregar clase Persona con campos del padrón`   | `entidades/Persona.java`  |
| 2 | `feat(entidades): agregar clase Direccion con códigos y nombres` | `entidades/Direccion.java`|
| 3 | `feat(dto): agregar enum FormatoSalida con JSON y XML`           | `dto/FormatoSalida.java`  |
| 4 | `feat(dto): agregar SolicitudPadron con validación básica`       | `dto/SolicitudPadron.java`|
| 5 | `feat(dto): agregar RespuestaPadron con factory methods`         | `dto/RespuestaPadron.java`|

---

## Dev 2 — Rama: `feature/repositorios`

Crear desde `main` después de que `feature/modelo` sea mergeado.

| # | Mensaje de commit                                                    | Archivos incluidos               |
|---|----------------------------------------------------------------------|----------------------------------|
| 1 | `feat(datos): agregar esqueleto de RepositorioPadron`                | `datos/RepositorioPadron.java`   |
| 2 | `feat(datos): implementar parsearLinea en RepositorioPadron`         | `datos/RepositorioPadron.java`   |
| 3 | `feat(datos): implementar cargarDesdeArchivo en RepositorioPadron`   | `datos/RepositorioPadron.java`   |
| 4 | `feat(datos): implementar buscarPorCedula en RepositorioPadron`      | `datos/RepositorioPadron.java`   |
| 5 | `feat(datos): agregar esqueleto de RepositorioDistelec`              | `datos/RepositorioDistelec.java` |
| 6 | `feat(datos): implementar parsearLinea en RepositorioDistelec`       | `datos/RepositorioDistelec.java` |
| 7 | `feat(datos): implementar cargarDesdeArchivo en RepositorioDistelec` | `datos/RepositorioDistelec.java` |
| 8 | `feat(datos): implementar buscarDireccion en RepositorioDistelec`    | `datos/RepositorioDistelec.java` |

---

## Dev 3 — Rama: `feature/logica`

Crear desde `main` después de que `feature/repositorios` sea mergeado.
Incluye también la serialización (así Dev 3 no depende de Dev 4 ni viceversa).

| # | Mensaje de commit                                                    | Archivos incluidos           |
|---|----------------------------------------------------------------------|------------------------------|
| 1 | `feat(logica): agregar esqueleto de ServicioPadron`                  | `logica/ServicioPadron.java` |
| 2 | `feat(logica): implementar validarCedula en ServicioPadron`          | `logica/ServicioPadron.java` |
| 3 | `feat(logica): implementar consultarPadron en ServicioPadron`        | `logica/ServicioPadron.java` |
| 4 | `feat(util): agregar esqueleto de Serializador`                      | `util/Serializador.java`     |
| 5 | `feat(util): implementar serialización a JSON`                       | `util/Serializador.java`     |
| 6 | `feat(util): implementar serialización a XML`                        | `util/Serializador.java`     |
| 7 | `feat(util): agregar escape de caracteres especiales en JSON`        | `util/Serializador.java`     |

---

## Dev 4 — Rama: `feature/presentacion`

Crear desde `main` después de que `feature/logica` sea mergeado.
Dev 4 implementa **ambos servidores** (TCP y HTTP) en la misma rama.

| # | Mensaje de commit                                                    | Archivos incluidos                          |
|---|----------------------------------------------------------------------|---------------------------------------------|
| 1 | `feat(tcp): agregar esqueleto de TcpServer`                          | `presentacion/tcp/TcpServer.java`           |
| 2 | `feat(tcp): implementar parsearSolicitud en TcpServer`               | `presentacion/tcp/TcpServer.java`           |
| 3 | `feat(tcp): implementar manejarCliente en TcpServer`                 | `presentacion/tcp/TcpServer.java`           |
| 4 | `feat(tcp): implementar loop principal en TcpServer`                 | `presentacion/tcp/TcpServer.java`           |
| 5 | `feat(http): agregar esqueleto de HttpServerPadron`                  | `presentacion/http/HttpServerPadron.java`   |
| 6 | `feat(http): implementar parsearParametros de query string`          | `presentacion/http/HttpServerPadron.java`   |
| 7 | `feat(http): implementar construirRespuestaHttp con headers`         | `presentacion/http/HttpServerPadron.java`   |
| 8 | `feat(http): implementar manejarCliente con request HTTP`            | `presentacion/http/HttpServerPadron.java`   |
| 9 | `feat(http): agregar respuestas de error 400 y 404`                  | `presentacion/http/HttpServerPadron.java`   |

---

## Integración final — Rama: `feature/integracion` (líder del equipo)

Último en mergearse. Conecta todo en Main.java y verifica que el sistema funcione end-to-end.

| # | Mensaje de commit                                                | Archivos incluidos                    |
|---|------------------------------------------------------------------|---------------------------------------|
| 1 | `feat: conectar repositorios y servicio en Main.java`           | `Main.java`                           |
| 2 | `feat: iniciar servidores TCP y HTTP en threads separados`      | `Main.java`                           |
| 3 | `feat: agregar shutdown hook para cierre limpio`                | `Main.java`                           |
| 4 | `feat: leer puertos desde build.properties o argumentos`       | `Main.java`, `build.properties`       |

---

## Resumen visual

```
Dev 1: feature/modelo
             ↓
Dev 2: feature/repositorios
             ↓
Dev 3: feature/logica   (logica/ + util/)
             ↓
Dev 4: feature/presentacion   (tcp/ + http/)
             ↓
       feature/integracion
             ↓
           main
```

## Quién toca qué (sin conflictos)

| Archivo / Directorio          | Dueño  | Nadie más lo modifica |
|-------------------------------|--------|-----------------------|
| `entidades/`, `dto/`          | Dev 1  | ✓                     |
| `datos/`                      | Dev 2  | ✓                     |
| `logica/`, `util/`            | Dev 3  | ✓                     |
| `presentacion/`               | Dev 4  | ✓                     |
| `Main.java`                   | Líder  | ✓                     |
| `build.xml`, `README.md`      | Líder  | coordinar antes       |
