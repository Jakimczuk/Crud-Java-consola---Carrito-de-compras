package com.unlz.abm.models;

import java.io.Serializable;

public class Articulo implements Serializable {
    private int codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    public Articulo(String nombre, String descripcion, int codigo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Articulo(String nombre, String descripcion, double precio, int stock, int codigo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Codigo: \t").append(this.codigo);
        sb.append("\n").append("Nombre: \t").append(this.nombre);
        sb.append("\n").append("Descr.: \t").append(this.descripcion);
        if (this.precio != 0){
            sb.append("\n").append("Precio: \t").append(this.precio);
        }
        if (this.stock != 0){
            sb.append("\n").append("Stock: \t\t").append(this.stock);
        }

        return String.valueOf(sb);
    }
}

