# 🌍 Red Sísmica Nacional - Sistema de Gestión y Supervisión de Eventos Sísmicos

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-FF8800?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![Hibernate / JPA](https://img.shields.io/badge/JPA%20%2F%20Hibernate-ORM-59666C?style=for-the-badge&logo=hibernate&logoColor=white)](https://hibernate.org/)
[![H2 Database](https://img.shields.io/badge/H2-Database-003B57?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.h2database.com/)
[![Lombok](https://img.shields.io/badge/Project-Lombok-BC002D?style=for-the-badge&logo=lombok&logoColor=white)](https://projectlombok.org/)

Proyecto Práctico Integrador desarrollado para la cátedra **Diseño de Sistemas de Información (DSI)** de la **Universidad Tecnológica Nacional - Facultad Regional Córdoba (UTN-FRC)**.

---

## 📌 Contexto del Proyecto y Evolución de Entregas

El proyecto modela el dominio de una **Red Sísmica Nacional**, cuyo propósito es la captura continua de señales telúricas a través de estaciones sismológicas, el procesamiento de series temporales y la intervención de analistas para la supervisión humana de eventos detectados.

El desarrollo se articuló en dos etapas iterativas:

- **[Primera Entrega (branch: `primera-entrega`)](https://github.com/Cristulo/tpaiSismos/tree/primera-entrega):** Implementación basal del caso de uso principal utilizando la arquitectura en capas y el patrón **BCE (Boundary-Control-Entity)**, con un manejo de estados respaldado en persistencia relacional tradicional.
- **[Segunda Entrega (branch: `segunda-entrega`)](https://github.com/Cristulo/tpaiSismos/tree/segunda-entrega):** Evolución orientada a la excelencia en diseño de software. Se refactorizó la lógica condicional hacia el **Patrón State (GoF)**, se reasignaron responsabilidades bajo patrones **GRASP (Experto en Información)** y se integró un sistema de diseño estandarizado para la interfaz gráfica en JavaFX.

---

## 🎯 Caso de Uso Central: "Registrar Revisión Manual de Evento Sísmico"

El flujo de trabajo implementa la supervisión humana de las detecciones automáticas de la red:

```mermaid
flowchart TD
    A([Analista Sismológico]) -->|1. Solicita revisar eventos sísmicos| B[PantallaRegistrarRevisionManual]
    B -->|2. Consultar eventos pendientes de revisión| C[RegistrarRevisionManualController]
    C -->|3. Recuperar eventos y sismógrafos| D[(Base de Datos)]
    D -->|4. Retornar entidades| C
    C -->|5. Mostrar grilla con eventos pendientes| B
    A -->|6. Selecciona un evento sísmico| B
    B -->|7. Notificar selección| C
    C -->|8. Bloquear evento en revisión| E[EventoSismico]
    C -->|9. Clasificar series temporales por estación| E
    C -->|10. Generar sismogramas visuales| F[GenerarSismogramaController]
    C -->|11. Formatear y enviar DetalleEventoSismicoDTO| B
    A -->|12. Analiza datos y confirma / rechaza el evento| B
    B -->|13. Notificar resolución con responsable| C
    C -->|14. Registrar nuevo estado y cerrar revisión| E
```

### Principios aplicados en el flujo:
1. **Patrón Controlador (GRASP):** `RegistrarRevisionManualController` actúa como mediador de la interacción, delegando la lógica de negocio a los objetos de dominio sin contener reglas operativas propias del evento.
2. **Desacoplamiento con DTO:** La vista consume `DetalleEventoSismicoDTO`, evitando acoplar la tecnología de interfaz (JavaFX) a las entidades gestionadas por el ORM.
3. **Inmutabilidad y Trazabilidad:** Los cambios de estado no sobreescriben datos históricos; generan instancias de `CambioEstado` con marca temporal y auditoría del empleado operador.

---

## 🏛 Arquitectura en Capas y Patrón BCE

El sistema implementa una separación rigurosa de responsabilidades basada en el patrón **Boundary-Control-Entity (BCE)**, integrado con el contenedor de inversión de control de Spring Boot:

```mermaid
flowchart TB
    subgraph CapaPresentacion["Capa de Presentación (Boundary)"]
        UI_Principal["VentanaPrincipal / PantallaPrincipal"]
        UI_Revision["PantallaRegistrarRevisionManual"]
        Views_FXML["Vistas FXML + light-theme.css"]
        UI_Revision -.-> Views_FXML
    end

    subgraph CapaControl["Capa de Aplicación y Control (Control)"]
        Ctrl_Revision["RegistrarRevisionManualController"]
        Ctrl_Sismograma["GenerarSismogramaController"]
        Spring_Factory["SpringFXMLLoader / ApplicationContext"]
    end

    subgraph CapaDominio["Capa de Dominio (Entity)"]
        Ent_Evento["EventoSismico"]
        Ent_Estado["Estado / CambioEstado"]
        Ent_Estacion["EstacionSismologica / Sismografo"]
        Ent_Serie["SerieTemporal / MuestraSismica"]
        Ent_Org["Empleado / Usuario / Rol"]
    end

    subgraph CapaPersistencia["Capa de Persistencia y Datos"]
        Repositories["Spring Data JPA Repositories"]
        Hibernate["Hibernate ORM"]
        H2_DB[(H2 Database)]
    end

    CapaPresentacion -->|Invoca eventos de usuario| CapaControl
    CapaControl -->|Coordina entidades del dominio| CapaDominio
    CapaControl -->|Inyecta dependencias via Spring| Spring_Factory
    CapaDominio -->|Mapeo Objeto-Relacional| Repositories
    Repositories --> Hibernate --> H2_DB
```

---

## 📐 Diagrama de Clases - Primera Entrega

Estructura de entidades relacionales modeladas para soportar el caso de uso antes de la introducción del patrón de comportamiento:

```mermaid
classDiagram
    direction TB

    class EventoSismico {
        -LocalDateTime fechaHoraOcurrencia
        -LocalDateTime fechaHoraFin
        -String latitudEpicentro
        -String longitudEpicentro
        -Float valorMagnitud
        -Estado estadoActual
        -List~CambioEstado~ cambiosEstado
        -List~SerieTemporal~ seriesTemporal
        +bloquearEnRevision(Estado)
        +rechazarEventoSismico(Estado, Empleado)
        +buscarDatosEventoSismicoSeleccionado() List~String~
    }

    class Estado {
        -String nombre
        -String ambito
        +sosPendienteRevision() Boolean
        +sosBloqueadoEnRevision() Boolean
        +sosRechazado() Boolean
        +esAmbito(String) Boolean
    }

    class CambioEstado {
        -LocalDateTime fechaHoraInicio
        -LocalDateTime fechaHoraFin
        -Estado estado
        -Empleado responsableInspeccion
        +esEstadoActual() Boolean
    }

    class Sismografo {
        -Integer identificadorSismografo
        -Integer nroSerie
        -LocalDate fechaAdquisicion
        -EstacionSismologica estacionSismologica
        -List~SerieTemporal~ seriesTemporales
        -Estado estadoActual
    }

    class EstacionSismologica {
        -String codigoEstacion
        -String nombre
        -String latitud
        -String longitud
    }

    class SerieTemporal {
        -LocalDateTime fechaHoraRegistro
        -Boolean condicionAlarma
        -Float frecuenciaMuestreo
        -List~MuestraSismica~ muestrasSismicas
    }

    class Empleado {
        -Integer id
        -String nombre
        -String apellido
        -String mail
    }

    EventoSismico "1" o-- "1..*" CambioEstado : registra historial
    CambioEstado "*" --> "1" Estado : referencia a
    EventoSismico "*" --> "1" Estado : estadoActual
    CambioEstado "*" --> "0..1" Empleado : auditado por
    EventoSismico "1" o-- "*" SerieTemporal : compuesto por
    Sismografo "1" o-- "*" SerieTemporal : adquiere
    Sismografo "*" --> "1" EstacionSismologica : ubicado en
```

---

## 🔄 Patrón de Diseño State (GoF) - Segunda Entrega

En la segunda entrega se reemplazó la lógica condicional por el **Patrón State**, otorgándole polimorfismo dinámico al ciclo de vida del evento sísmico.

### Diagrama de Clases del Patrón State

```mermaid
classDiagram
    direction TB

    class EventoSismico {
        -Estado estadoActual
        -IStateEventoSismico estadoActualComportamiento
        -List~CambioEstado~ cambiosEstado
        +bloquearEnRevision(Empleado)
        +rechazar(Empleado)
        +confirmar(Empleado)
        +derivarExperto(Empleado)
        +validarAutoConfirmado(Empleado)
        +getSeriesTemporalClasificadasPorEstacionSismologica(Map)
    }

    class IStateEventoSismico {
        <<abstract>>
        #prohibida(String accion)
        +bloquearEnRevision(EventoSismico, Empleado)
        +rechazar(EventoSismico, Empleado)
        +confirmar(EventoSismico, Empleado)
        +derivarExperto(EventoSismico, Empleado)
        +sosPendienteDeRevision() Boolean
        +sosBloqueadoEnRevision() Boolean
        +sosRechazado() Boolean
        +sosConfirmado() Boolean
    }

    class PendienteDeRevision {
        +sosPendienteDeRevision() Boolean
        +bloquearEnRevision(EventoSismico, Empleado)
    }

    class BloqueadoEnRevision {
        +sosBloqueadoEnRevision() Boolean
        +rechazar(EventoSismico, Empleado)
        +confirmar(EventoSismico, Empleado)
        +derivarExperto(EventoSismico, Empleado)
    }

    class Rechazado {
        +sosRechazado() Boolean
    }

    class Confirmado {
        +sosConfirmado() Boolean
    }

    class AutoDetectado {
        +sosAutoDetectado() Boolean
    }

    class DerivadoAExperto {
    }

    EventoSismico o-- IStateEventoSismico : delega comportamiento (@Transient)
    IStateEventoSismico <|-- PendienteDeRevision
    IStateEventoSismico <|-- BloqueadoEnRevision
    IStateEventoSismico <|-- Rechazado
    IStateEventoSismico <|-- Confirmado
    IStateEventoSismico <|-- AutoDetectado
    IStateEventoSismico <|-- DerivadoAExperto
```

### Máquina de Transición de Estados

```mermaid
stateDiagram-v2
    [*] --> AutoDetectado: Detección automática de sensores
    AutoDetectado --> PendienteDeRevision: Clasificación inicial de señales
    
    PendienteDeRevision --> BloqueadoEnRevision: Analista inicia revisión manual (bloquearEnRevision)
    
    BloqueadoEnRevision --> Rechazado: Descarte de falso positivo (rechazar)
    BloqueadoEnRevision --> Confirmado: Confirmación de parámetros validados (confirmar)
    BloqueadoEnRevision --> DerivadoAExperto: Señales anómalas complejas (derivarExperto)
    
    Rechazado --> [*]
    Confirmado --> [*]
```

### Mecanismos de Diseño Destacados:
- **Protección por Defecto:** La clase base `IStateEventoSismico` arroja por defecto `IllegalStateException("Acción no permitida desde <Estado>")` ante cualquier transición inválida, blindando la integridad del negocio.
- **Coexistencia Híbrida Limpia (JPA + GoF):** Se mantiene `@ManyToOne Estado estadoActual` para garantizar la persistencia relacional con integridad referencial (claves foráneas) y se utiliza `@Transient IStateEventoSismico estadoActualComportamiento` para la ejecución del polimorfismo en tiempo de ejecución, sincronizado mediante `@PostLoad`.
- **GRASP Experto en Información:** La clasificación y agrupamiento de series temporales por estación sismológica fue trasladada desde el controlador hacia `EventoSismico`, ya que es este último quien conoce sus propias series.

---

## 🏛 Resumen Académico

| Concepto de Diseño | Aplicación Concreta en el Proyecto |
| :--- | :--- |
| **BCE (Boundary-Control-Entity)** | Separación estricta entre vistas JavaFX/FXML (Boundary), controladores de caso de uso (Control) y entidades JPA/Hibernate (Entity). |
| **Patrón State (GoF)** | Desacoplamiento de las transiciones operativas de `EventoSismico` en clases concretas polimórficas (`PendienteDeRevision`, `BloqueadoEnRevision`, etc.). |
| **Experto en Información (GRASP)** | Filtrado y asignación de series temporales encapsulada en la entidad que posee los datos (`EventoSismico`). |
| **Creador (GRASP)** | Creación y administración del historial de cambios de estado encapsulada en la entidad contenedora. |
| **Bajo Acoplamiento** | Uso de `DetalleEventoSismicoDTO` para transferir información a la UI sin exponer modelos relacionales de persistencia. |
| **Auditoría e Inmutabilidad** | Modelado temporal con `CambioEstado`, registrando inicio, fin y responsable sin sobreescribir la traza histórica. |

---

*Cátedra de Diseño de Sistemas de Información (DSI) — Universidad Tecnológica Nacional, Facultad Regional Córdoba (UTN-FRC).*
