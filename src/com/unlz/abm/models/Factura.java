package com.unlz.abm.models;

import com.unlz.abm.processes.Archivo;

import java.io.Serializable;
import java.util.Map;

public class Factura implements Serializable {

    public static double Total(Usuario u){
        double total = 0;
        for (ItemFactura articulos : u.getCarro()) {
            total = total + (articulos.getTotalProducto());
        }
        return total;
    }

    public static void totalYDetalle(Usuario u){
        StringBuilder sb = new StringBuilder();

        sb.append("//////////////////////////").append("\n");
        for (ItemFactura itemFactura : u.getCarro()) {
            sb.append(itemFactura.getArticulo().getCodigo()).append(" ").append(itemFactura.getArticulo().getNombre())
                    .append(" x").append(itemFactura.getCantidad())
                    .append(" $").append(itemFactura.getArticulo().getPrecio())
                    .append(" ");
        }
        System.out.println(sb);
        System.out.println("//////////////////////////");
        System.out.println("Total del carro: $" + Factura.Total(u));
        System.out.println("//////////////////////////");
    }

    public static void confirmarCompra(Usuario u, Map<Integer, Articulo> articuloMap, Map<Integer, Usuario> usermap){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        double total = Total(u);
        usermap = Archivo.cargarArchivo(usermap, "usuarios.txt");
        articuloMap = Archivo.cargarArchivoArticulo(articuloMap, "articulos.txt");

        if(u.getDinero() >= total){
            sb.append("-").append("\tProdct.").append(" Cantd.").append(" Precio").append("\tTotal").append("\n");

            for (ItemFactura itemFactura : u.getCarro()) {
                sb.append(itemFactura.getArticulo().getCodigo())
                        .append("\t").append(itemFactura.getArticulo().getNombre())
                        .append(" x").append(itemFactura.getCantidad())
                        .append("  $").append(itemFactura.getArticulo().getPrecio())
                        .append(" \t| $").append(itemFactura.getTotalProducto())
                        .append("\n");

                int codigo = itemFactura.getArticulo().getCodigo();
                int stock = itemFactura.getArticulo().getStock();
                int cantidad = itemFactura.getCantidad();

                // Setear stock disminuido con la cantidad
                articuloMap.get(codigo).setStock(stock - cantidad);
            }

            sb.append("-").append("\t").append("Total: $").append(total).append("\n");;

            System.out.println(sb);

            // Setear dinero restado al usuario.
            double dineroUser = u.getDinero();
            u.setDinero(dineroUser - total);
            usermap.get(u.getDni()).setDinero(dineroUser - total);

            u.getCarro().clear();
            usermap.get(u.getDni()).getCarro().clear();

            System.out.println("Dinero Total del Usuario: " + u.getDinero());
            //System.out.println("Usermap Dinero Total del Usuario: " + usermap.get(u.getDni()).getDinero());
        }else{
            System.out.println("... No tienes Dinero Suficiente en tu Cartera ...");
        }

        Archivo.guardarArchivo(usermap, "usuarios.txt");
        Archivo.guardarArchivoArticulo(articuloMap, "articulos.txt");

    }

}
