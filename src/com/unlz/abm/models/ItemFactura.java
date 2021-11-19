package com.unlz.abm.models;

import java.io.Serializable;

public class ItemFactura implements Serializable {

    private int cantidad;
    private final double totalProducto;
    private Articulo articulo;

    public ItemFactura(Articulo articulo, int cantidad) {
        this.cantidad = cantidad;
        this.articulo = articulo;
        this.totalProducto = articulo.getPrecio() * cantidad;
     }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public double getTotalProducto() {
        return totalProducto;
    }

}
