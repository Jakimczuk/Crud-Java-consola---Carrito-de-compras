package com.unlz.abm.contenedores;

import com.unlz.abm.models.Articulo;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Contenedor ARTICULO
public class ContenedorA {

    private static final Scanner sc = new Scanner(System.in);
    private static final Scanner scLong = new Scanner(System.in).useDelimiter("\n");
    private static Map<Integer, Articulo> articuloMap = new HashMap<>();

    public ContenedorA() {
    }

    public ContenedorA(String nombre, String descripcion, int codigo) {
        articuloMap.put(codigo,new Articulo(nombre,descripcion, codigo));
    }

    public ContenedorA(String nombre, String descripcion, double precio, int stock, int codigo){
        articuloMap.put(codigo,new Articulo(nombre,descripcion,precio,stock, codigo));
    }

    public Map<Integer, Articulo> getArticuloMap() {
        return articuloMap;
    }

    public void setArticuloMap(Map<Integer, Articulo> articuloMap) {
        ContenedorA.articuloMap = articuloMap;
    }


}
