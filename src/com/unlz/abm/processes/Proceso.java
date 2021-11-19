package com.unlz.abm.processes;

import com.unlz.abm.models.Articulo;
import com.unlz.abm.models.Factura;
import com.unlz.abm.models.Rol;
import com.unlz.abm.models.Usuario;

import java.io.Serializable;
import java.util.Map;
import java.util.Scanner;

import static com.unlz.abm.processes.Archivo.verUsuariosArchivo;
import static com.unlz.abm.processes.Autenticacion.*;

public class Proceso implements Serializable {

    public static final Scanner sc = new Scanner(System.in);
    public static final Scanner scLong = new Scanner(System.in).useDelimiter("\n");

    /**
     * Menu de manejo de usuarios.
     * **/
    public static void userMenu(Map<Integer, Usuario> usermap, Map<Integer, Articulo> articuloMap) {
        String opcion = "";
        //Collection<Usuario> usuarios = usermap.values();
        if (Archivo.cargarArchivo("usuarios.txt") == null ) {
            Archivo.guardarArchivo(usermap, "usuarios.txt");
        } else {
            usermap = Archivo.cargarArchivo("usuarios.txt");
        }

        if (Archivo.cargarArchivoArticulo("articulos.txt") == null ) {
            Archivo.guardarArchivoArticulo(articuloMap, "articulos.txt");
        } else {
            articuloMap = Archivo.cargarArchivoArticulo("articulos.txt");
        }

        while (!opcion.equals("0")) {
            StringBuilder sb = new StringBuilder("//////// Bienvenido ////////");
            sb.append("\n").append("1 - Ver Catalogo");

            // Menu
            if (getUserloged().getRol() == null) {
                sb.append("\n").append("2 - Registrarse");
                sb.append("\n").append("3 - Iniciar Sesion");
            } else {
                sb.append("\n").append("2 - Ver Perfil");
                sb.append("\n").append("3 - Cerrar Sesion");
                sb.append("\n").append("4 - Ingresar dinero");
                sb.append("\n").append("5 - Transfefir dinero");
                sb.append("\n").append("6 - Retirar dinero");
                if (getUserloged().getRol() == Rol.USER_ADMIN) {
                    sb.append("\n").append("7 - Ver Usuarios");
                }
            }
            sb.append("\n").append("0 - Salir");
            System.out.print(sb + "\n" + "opcion: ");
            opcion = sc.next();

            switch (opcion) {
                case "1": // Ver Catalogo
                    productMenu(usermap, articuloMap, getUserloged());
                    break;
                case "2": // Registrarse
                    if (getUserloged().getRol() == null) {
                        register(usermap);
                    } else { // Ver perfil
                        //System.out.println(usermap.get(Autenticacion.getUserloged().getDni()));
                        System.out.println(getUserloged());
                    }
                    break;
                case "3": // Iniciar Sesion
                    if (getUserloged().getRol() == null) {
                        setUserloged(login());
                    } else { // Cerrar Sesion
                        setUserloged(getUserunregistered());
                    }
                    break;
                case "4": // Ingresar dinero
                    if (getUserloged().getRol() != null){
                        Usuario.ingresarDinero(getUserloged(),usermap);
                    }
                    break;
                case "5": // Transferir
                    if (getUserloged().getRol() != null){
                        Usuario.transferirDinero(getUserloged(), usermap);
                    }
                    break;
                case "6": // Retirar dinero
                    if (getUserloged().getRol() != null){
                        Usuario.retirarDinero(getUserloged(), usermap);
                    }
                    break;
                case "7":
                    if (getUserloged().getRol() == Rol.USER_ADMIN) {
                        System.out.println(verUsuariosArchivo());
                    } else {
                        System.out.println("(!) ERROR: Ingrese una opción valida. (!)");
                    }
                    break;
                case "0":
                    System.out.println("Saliendo del Programa...");
                    break;
                default:
                    System.out.println("(!) ERROR: Ingrese una opción valida. (!)");
                    break;
            }
        }
    }

    /**
     * Menu Tienda
     * **/
    public static void productMenu(Map<Integer,Usuario> usermap, Map<Integer, Articulo> articuloMap, Usuario u) {
        String opcion = "";

        System.out.println("//////////////////////");
        while (!opcion.equals("0")) {

            StringBuilder sb = new StringBuilder();

            if (getUserloged().getRol() == null){
                sb.append("1 - Ver Articulos").append("\n");
            }
            if (getUserloged().getRol() == Rol.USER) {
                sb.append("1 - Ver Articulos").append("\n");
                sb.append("2 - Agregar Articulos al Carro").append("\n");
                if (getUserloged().getCarro().size() > 0){
                    sb.append("3 - Ver mi  Carro").append("\n");
                    sb.append("4 - Pagar y Generar Factura").append("\n");
                }
            }
            if (getUserloged().getRol() == Rol.USER_ADMIN) {
                sb.append("1 - Listar Articulos").append("\n");
                sb.append("2 - Agregar Articulo").append("\n");
                sb.append("3 - Borrar Articulo").append("\n");
                sb.append("4 - Editar Articulo").append("\n");
            }

            sb.append("0 - Volver al Menu Principal").append("\n");

            System.out.print(sb + "opcion: ");
            opcion = sc.next();

            switch (opcion) {
                case "1":
                    u.verArticulos(articuloMap);
                    break;
                case "2":
                    if (getUserloged().getRol() == Rol.USER_ADMIN) {
                        u.agregarArticulo(articuloMap);
                    }
                    if (getUserloged().getRol() == Rol.USER) {
                        Usuario.agregarAlCarro(getUserloged(),usermap, articuloMap);
                    }
                    break;
                case "3":
                    if (getUserloged().getRol() == Rol.USER_ADMIN) {
                        u.eliminarArticulo(articuloMap);
                    }
                    if (getUserloged().getRol() == Rol.USER) {
                        if (getUserloged().getCarro().size() > 0){
                            Factura.totalYDetalle(u);
                        }
                    }
                    break;
                case "4":
                    if (getUserloged().getRol() == Rol.USER_ADMIN) {
                        u.editarArticulo(articuloMap);
                    }
                    if (getUserloged().getRol() == Rol.USER) {
                        if (getUserloged().getCarro().size() > 0){
                            Factura.confirmarCompra(u, articuloMap, usermap);
                        }
                    }
                    break;
                case "0":
                    System.out.println("Volviendo al menu principal...");
                    break;
            }
        }
    }
}