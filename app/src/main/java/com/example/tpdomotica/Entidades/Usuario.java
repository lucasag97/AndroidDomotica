package com.example.tpdomotica.Entidades;

public class Usuario {

    private Integer _id;
    private String nombre;
    private String apellido;
    private String dni;
    private String username;
    private String password;
    private String rol;

    public Usuario(String nombre, String apellido, String dni, String username, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String apellido) {
        this.dni = dni;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String password) {
        this.rol = rol;
    }
}
