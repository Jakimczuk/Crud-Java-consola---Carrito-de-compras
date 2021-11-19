package com.unlz.abm;

import com.unlz.abm.contenedores.ContenedorA;
import com.unlz.abm.contenedores.ContenedorU;
import com.unlz.abm.models.Articulo;
import com.unlz.abm.models.Rol;

import static com.unlz.abm.processes.Proceso.*;

public class Main {
    public static void main(String[] args) {

        /*
        Map<Integer, Articulo> articuloMap = new HashMap<>();
        articuloMap.put(1,new Articulo("Ladrillo","Ladrillos para construcción."));

        Map<Integer, Usuario> userMap = new HashMap<>();
        userMap.put(0,new Usuario("admin","admin",Rol.USER_ADMIN));
        */

        ContenedorU usermap = new ContenedorU("admin","admin",Rol.USER_ADMIN);
        ContenedorA articulosMap = new ContenedorA();
        articulosMap.getArticuloMap().put(1, new Articulo("Ladrillo", "Ladrillo para Construcción", 17, 1000, 1));
        articulosMap.getArticuloMap().put(2, new Articulo("Bolsa de Cemento", "Bolsa de Cemento de 20kg", 500, 500, 2));
        articulosMap.getArticuloMap().put(3, new Articulo("Ceramicos", "Tabla de Ceramicas 35x35", 815, 700, 3));
        articulosMap.getArticuloMap().put(4, new Articulo("Chapa", "Chapa Techo Acanalada Galvanizada", 950, 700, 4));

        userMenu(usermap.getUserMap(),articulosMap.getArticuloMap());

        }
    }


