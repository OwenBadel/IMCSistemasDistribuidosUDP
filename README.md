# IMCSistemasDistribuidosUDP

Sistema distribuido cliente-servidor basado en el protocolo **UDP (User Datagram Protocol)** para el cálculo y clasificación del **Índice de Masa Corporal (IMC)**, desarrollado bajo el patrón de **Arquitectura Hexagonal (Puertos y Adaptadores)** en **Java 17**.

---

## 🏛️ Arquitectura Hexagonal

El proyecto se divide en dos módulos independientes (`servidor-hexagonal` y `cliente-hexagonal`), cada uno estructurado en capas estrictamente desacopladas:

```
[ Capa de Adaptadores / Vistas (Swing & UDP Socket) ]
                     │
                     ▼ (Invoca puertos de entrada / DTOs)
       [ Capa de Aplicación (Casos de Uso) ]
                     │
                     ▼ (Orquesta)
       [ Capa de Dominio (Entidades, Value Objects, Reglas) ]
                     ▲
                     │ (Implementa puertos de salida)
[ Capa de Adaptadores (Salida UDP, Notificaciones) ]
```

### 1. Capa de Dominio (`dominio`)
- **Value Objects:** `Peso`, `Altura`, `Destinatario`, `PuertoRed`, `DestinoServidor`.
- **Entidad / Modelo de Negocio:** `CalculoImc`, `ResultadoImc`, `DatosImc`, `RespuestaCliente`.
- **Puertos:** `ControladorServidorRedPort`, `PuertoSalidaRed`, `PuertoNotificacionEvento`, `ClienteUdpPort`, `PuertoNotificacionCliente`.
- **Excepciones de Dominio:** `DimensionIncorrectaException`, `CalculoInvalidoException`, `PuertoIncorrectoException`, `DestinatarioIncorrectoException`, etc.

### 2. Capa de Aplicación (`aplicacion`)
- **Puertos de Entrada (Input Ports):** `CalcularImcInputPort`, `ProcesarPeticionUdpInputPort`, `GestionarServidorInputPort`, `GestionarConexionInputPort`.
- **Servicios:** `CalcularImcService`, `ProcesarPeticionUdpService`, `GestionarServidorService`, `ClienteImcService`.
- **Mapeadores y DTOs:** `CalculoMapper`, `PeticionMapper`, `ClienteMapper`, `ResultadoImcDto`.

### 3. Capa de Adaptadores e Infraestructura (`adaptadores`)
- **Adaptadores UDP:** `CanalUdp` (gestión de `DatagramSocket`), `AdaptadorSalidaUdp`, `AdaptadorClienteUdp`, `ReceptorPeticionesUdp`.
- **Adaptadores de Notificación:** `AdaptadorNotificacionEvento`, `AdaptadorNotificacionCliente` (Patrón Observer).
- **Mapeadores de Protocolo:** `ProtocoloUdpMapper`, `UdpNetworkMapper`.

### 4. Capa de Presentación (`vistas`)
- `ServidorFrame`: Interfaz gráfica Swing con control de encendido/apagado, detección de IP local y consola de eventos en tiempo real.
- `ClienteFrame`: Interfaz gráfica Swing con conexión parametrizable por IP/puerto, entradas para Peso y Altura, visualización de IMC y clasificación médica.

---

## 📡 Protocolo de Comunicación UDP

La comunicación entre el cliente y el servidor se realiza sin conexión directa previa, mediante paquetes de datagramas codificados en **UTF-8**:

### 1. Petición del Cliente al Servidor
```text
PESO:<valor>;ALTURA:<valor>
```
*Ejemplo:* `PESO:70.0;ALTURA:1.75`

### 2. Respuesta Exitosa del Servidor
```text
ESTADO:EXITO;IMC:<valor>;MENSAJE:<clasificacion>
```
*Ejemplo:* `ESTADO:EXITO;IMC:22.86;MENSAJE:Estas bien de peso`

### 3. Respuesta de Error del Servidor
```text
ESTADO:ERROR;MENSAJE:<descripcion_del_error>
```
*Ejemplo:* `ESTADO:ERROR;MENSAJE:El peso debe ser mayor a 0. Valor recibido: -5.0`

---

## ⚖️ Tabla de Clasificación del IMC

| Rango IMC | Clasificación / Mensaje |
|:---|:---|
| `< 18.5` | *Debes consultar un Medico, tu peso es muy bajo* |
| `18.5 - 24.9` | *Estas bien de peso* |
| `> 24.9 - 29.9` | *Debes bajar un poco de peso* |
| `> 29.9` | *Debes consultar un Medico, tu peso es muy alto* |

Fórmula:
$$\text{IMC} = \frac{\text{peso}}{\text{altura}^2}$$

---

## 📁 Estructura del Repositorio

```text
IMCSistemasDistribuidosUDP/
├── .gitignore
├── README.md
├── cliente-hexagonal/
│   ├── pom.xml
│   └── src/
│       ├── main/java/owenb/imc/
│       │   ├── Main.java
│       │   ├── adaptadores/
│       │   │   ├── AdaptadorClienteUdp.java
│       │   │   ├── AdaptadorNotificacionCliente.java
│       │   │   ├── CanalUdp.java
│       │   │   ├── ObservadorCliente.java
│       │   │   ├── ProtocoloUdpMapper.java
│       │   │   └── RedUtil.java
│       │   ├── aplicacion/
│       │   │   ├── CalcularImcCommand.java
│       │   │   ├── CalcularImcInputPort.java
│       │   │   ├── ClienteImcService.java
│       │   │   ├── ClienteMapper.java
│       │   │   ├── ClienteRedException.java
│       │   │   ├── ConectarCommand.java
│       │   │   └── GestionarConexionInputPort.java
│       │   ├── dominio/
│       │   │   ├── Altura.java
│       │   │   ├── ClienteUdpPort.java
│       │   │   ├── DatosImc.java
│       │   │   ├── DestinoIncorrectoException.java
│       │   │   ├── DestinoServidor.java
│       │   │   ├── DimensionIncorrectaException.java
│       │   │   ├── DominioException.java
│       │   │   ├── EstadoConexion.java
│       │   │   ├── EventoCliente.java
│       │   │   ├── Peso.java
│       │   │   ├── PuertoNotificacionCliente.java
│       │   │   ├── RespuestaServidorException.java
│       │   │   └── ResultadoImc.java
│       │   └── vistas/
│       │       └── ClienteFrame.java
│       └── test/java/owenb/imc/
│           ├── ImcClienteDominioTest.java
│           └── ImcClienteProtocoloTest.java
└── servidor-hexagonal/
    ├── pom.xml
    └── src/
        ├── main/java/owenb/imc/
        │   ├── Main.java
        │   ├── adaptadores/
        │   │   ├── AdaptadorControlServidorRed.java
        │   │   ├── AdaptadorNotificacionEvento.java
        │   │   ├── AdaptadorSalidaUdp.java
        │   │   ├── CanalUdp.java
        │   │   ├── ObservadorServidor.java
        │   │   ├── ReceptorPeticionesUdp.java
        │   │   ├── RedUtil.java
        │   │   ├── UdpNetworkMapper.java
        │   │   └── UdpResponse.java
        │   ├── aplicacion/
        │   │   ├── CalcularImcCommand.java
        │   │   ├── CalcularImcInputPort.java
        │   │   ├── CalcularImcService.java
        │   │   ├── CalculoMapper.java
        │   │   ├── ComandoInvalidoException.java
        │   │   ├── GestionarServidorInputPort.java
        │   │   ├── GestionarServidorService.java
        │   │   ├── PeticionMapper.java
        │   │   ├── ProcesarPeticionUdpCommand.java
        │   │   ├── ProcesarPeticionUdpInputPort.java
        │   │   ├── ProcesarPeticionUdpService.java
        │   │   ├── ResultadoImcDto.java
        │   │   └── ServidorRedException.java
        │   ├── dominio/
        │   │   ├── Altura.java
        │   │   ├── CalculoImc.java
        │   │   ├── CalculoInvalidoException.java
        │   │   ├── ControladorServidorRedPort.java
        │   │   ├── Destinatario.java
        │   │   ├── DestinatarioIncorrectoException.java
        │   │   ├── DimensionIncorrectaException.java
        │   │   ├── DominioException.java
        │   │   ├── EstadoServidor.java
        │   │   ├── EventoServidor.java
        │   │   ├── Peso.java
        │   │   ├── PuertoIncorrectoException.java
        │   │   ├── PuertoNotificacionEvento.java
        │   │   ├── PuertoRed.java
        │   │   ├── PuertoSalidaRed.java
        │   │   ├── RespuestaCliente.java
        │   │   ├── RespuestaInvalidaException.java
        │   │   ├── ResultadoImc.java
        │   │   └── TipoRespuesta.java
        │   └── vistas/
        │       └── ServidorFrame.java
        └── test/java/owenb/imc/
            ├── ImcIntegracionUdpTest.java
            ├── ImcServidorAplicacionTest.java
            └── ImcServidorDominioTest.java
```

---

## 🚀 Compilación y Ejecución

### Requisitos
- **JDK 17+**
- **Apache Maven 3.8+**

### 1. Compilar y ejecutar las pruebas
Para compilar y verificar ambos proyectos:
```powershell
# En servidor-hexagonal:
cd servidor-hexagonal
mvn clean test

# En cliente-hexagonal:
cd ../cliente-hexagonal
mvn clean test
```

### 2. Ejecutar el Servidor
```powershell
cd servidor-hexagonal
mvn exec:java
```
1. Se abrirá la ventana gráfica del Servidor.
2. Ingrese el puerto deseado (por defecto `9876`).
3. Haga clic en **INICIAR**. El estado cambiará a **ONLINE**.

### 3. Ejecutar el Cliente
En otra terminal:
```powershell
cd cliente-hexagonal
mvn exec:java
```
1. Se abrirá la ventana gráfica del Cliente.
2. Configure el host (ej. `127.0.0.1`) y el puerto (`9876`).
3. Haga clic en **Conectar**.
4. Ingrese el **Peso (kg)** y la **Altura (m)**.
5. Haga clic en **CALCULAR IMC**.
6. Observe el resultado del IMC y la clasificación médica calculada por el servidor UDP.
