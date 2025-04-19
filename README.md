# 🧠 MindCare - Sistema de Atención Psicológica

Bienvenido al proyecto **MindCare**, una aplicación básica desarrollada en Java orientada a objetos. El sistema gestiona servicios de atención psicológica con almacenamiento en archivos `.txt`, cumpliendo con los requisitos académicos establecidos.

---

## ✅ Características principales

- Registro de usuarios (pacientes y psicólogos)
- Gestión de citas (crear, reprogramar, cancelar)
- Visualización de historial clínico
- Gestión de disponibilidad por parte de psicólogos
- Almacenamiento de datos en archivos `.txt`
- Preparado para futura integración de interfaz gráfica (GUI)

---

## 🗂️ Estructura del proyecto
MindCare/ │ 
    ├── src/ │ 
        ├── Usuario.java │ 
        ├── Paciente.java │ 
        ├── Psicologo.java │ 
        ├── Cita.java │ 
        ├── Historial.java │ 
        ├── GestorArchivos.java │ 
        ├── Test.java 
        └── MindCare.java │ 
    ├── pacientes.txt 
    ├── psicologos.txt 
    ├── citas.txt 
    ├── historiales.txt 
    └── README.md


---

## 🧱 Clases del sistema

### `Usuario`
Clase base para `Paciente` y `Psicologo`.

- **Atributos:**
  - `idUsuario: int`
  - `nombre: String`
  - `correo: String`
  - `clave: String`

- **Métodos:**
  - `mostrarInfo()`
  - Getters, Setters, `toString()`

---

### `Paciente` (hereda de `Usuario`)

- **Atributos:**
  - `idPaciente: int`
  - `fechaNacimiento: String`
  - `historial: Historial`
  - `citasAgendadas: List<Cita>`

- **Métodos:**
  - `verCitas()`
  - `verHistorial()`
  - `actualizarDatos()`

---

### `Psicologo` (hereda de `Usuario`)

- **Atributos:**
  - `idPsicologo: int`
  - `especialidad: String`
  - `disponibilidad: List<String>` (ej: `16-04-2025H15:00`)
  - `citasAsignadas: List<Cita>`

- **Métodos:**
  - `verAgenda()`
  - `agregarDisponibilidad(String)`
  - `atenderCita(Cita)`

---

### `Cita`

- **Atributos:**
  - `idCita: int`
  - `fecha: String`
  - `hora: String`
  - `idPaciente: int`
  - `idPsicologo: int`

- **Métodos:**
  - `mostrarDetalle()`
  - `reprogramar(String nuevaFecha, String nuevaHora)`

---

### `Historial`

- **Atributos:**
  - `idHistorial: int`
  - `idPaciente: int`
  - `notas: String`
  - `diagnosticos: String`
  - `citasRelacionadas: List<Integer>`

- **Métodos:**
  - `agregarNota(String)`
  - `agregarDiagnostico(String)`
  - `mostrarHistorial()`

---

### `GestorArchivos`
Clase utilitaria para la gestión de archivos `.txt`.

- **Métodos:**
  - `guardarEnArchivo(String archivo, String contenido)`
  - `leerArchivo(String archivo): List<String>`
  - `eliminarRegistro(String archivo, String contenido)`
  - `actualizarRegistro(String archivo, String viejo, String nuevo)`
  - `limpiarArchivo(String archivo)`

---

### `Test`
Clase creada para probar todas las funcionalidades antes de integrar la GUI.

- Crea instancias de usuarios, pacientes, psicólogos, citas, etc.
- Utiliza los métodos de escritura/lectura en `.txt`.
- Sirve como base para las pruebas antes de integrar con interfaz gráfica.

---

