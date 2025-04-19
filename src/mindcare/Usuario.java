/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mindcare;

/**
 *
 * @author MAOZ-
 */
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private String clave;
    private String tipo; // "paciente" o "psicologo"

    // Constructor
    public Usuario(int idUsuario, String nombre, String correo, String clave, String tipo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Método para mostrar información
    public void mostrarInfo() {
        System.out.println("ID: " + idUsuario);
        System.out.println("Nombre: " + nombre);
        System.out.println("Correo: " + correo);
        System.out.println("Tipo: " + tipo);
    }

    // toString()
    @Override
    public String toString() {
        return idUsuario + "," + nombre + "," + correo + "," + clave + "," + tipo;
    }
}