package com.unlz.abm.processes;

import com.unlz.abm.models.Usuario;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

import static com.unlz.abm.models.Rol.USER;
import static com.unlz.abm.processes.Archivo.*;
import static com.unlz.abm.processes.Validacion.haveBlanks;
import static com.unlz.abm.processes.Validacion.isNumeric;

public class Autenticacion implements Serializable {

    private static final Scanner sc = new Scanner(System.in);
    private static final Usuario userunregistered = new Usuario();
    private static Usuario userloged = userunregistered;

    /**
     * Login para usuarios
     * @return usuario / userunregistered
     * **/
    public static Usuario login() {
        System.out.print("Ingrese el Nombre de Usuario: ");
        String username = sc.next();
        System.out.print("Ingrese la Contraseña: ");
        String password = sc.next();

        Map<Integer, Usuario> usermapArchivo = Archivo.cargarArchivo("usuarios.txt");

        if (usermapArchivo == null) {
            return userunregistered;
        }

        Collection<Usuario> usuarios = usermapArchivo.values();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) &&
                    usuario.getPassword().equals(password)) {
                return usuario;
            }
        }

        System.out.println("(!) ERROR: El Usuario No Existe o el Usuario y la Contraseña son Erroneas. (!)");

        return userunregistered;
    }

    /**
     * Registra y agrega una persona al contenedor
     * @param usermap la coleccion donde se desea agregar.
     * //@throws
     * **/
    public static void register(Map<Integer, Usuario> usermap) {
        System.out.println("////////////// Registro //////////////");
        usermap = cargarArchivo(usermap, "usuarios.txt");

        while (true) {
            int check = 0;

            System.out.print("Ingrese un nombre de usuario: ");
            String username = Proceso.scLong.next();

            System.out.print("Ingrese una contraseña: ");
            String password = Proceso.scLong.next();

            if(haveBlanks(username) || haveBlanks(password)){
                System.out.println("(!) ERROR: No se admiten espacios en blanco en el nombre o contraseña. (!)");
                break;
            }

            System.out.print("Confirmar contraseña: ");
            String passwordConfirmation = sc.next();

            // Validacion Username
            Collection<Usuario> usuarios = usermap.values();
            for (Usuario usuario : usuarios) {
                if (usuario.getUsername().equals(username)) {
                    check = 1;
                    break;
                }
            }
            if (check == 1) {
            System.out.println("(!) ERROR: El Usuario ingresado ya existe. (!)");
            continue;
            }

            // Validacion Password
            if (!password.equals(passwordConfirmation)) {
                System.out.println("(!) ERROR: Las contraseñas no son iguales. Vuelva a ingresar. (!)");
                continue;
            } else System.out.println("///// Usuario Validado con Exito /////");

            System.out.println("Complete los datos restantes...");

            String nombre, apellido;
            // Validacion Nombre y Apellido
            do {
                System.out.print("Ingrese su nombre real: ");
                nombre = Proceso.scLong.next();
                System.out.print("Ingrese su apellido: ");
                apellido = Proceso.scLong.next();
                if (isNumeric(nombre) || isNumeric(apellido)) {
                    System.out.println("(!) ERROR: Los valores ingresados no son validos (!)");
                    continue;
                }
                if (haveBlanks(username) || haveBlanks(password)) {
                    System.out.println("(!) ERROR: No se admiten espacios en blanco en el nombre o contraseña (!)");
                    continue;
                }
                break;
            } while (true);

            // Validacion DNI
            int dni;
            while (true) {
                check = 0;
                System.out.print("Ingrese su DNI: ");
                String dniStr = Proceso.scLong.next();

                if (dniStr.isEmpty() || dniStr.isBlank()) continue;
                if (!isNumeric(dniStr)) continue;
                if (haveBlanks(dniStr)) continue;
                else dni = Integer.parseInt(dniStr);
                if (dni <= 0) continue;

                for (Usuario usuario : usuarios) {
                    if (usuario.getDni() == dni) {
                        check = 1;
                        break;
                    }
                }
                if (check == 1) {
                    System.out.println("(!) ERROR: Este DNI ya existe. (!)");
                    continue;
                }
                break;
            }

            //AGREGADO
            // Validacion saldo
            double saldo;
            while (true) {
                System.out.print("Ingrese su saldo: ");
                String saldoStr = sc.next();

                if (saldoStr.isEmpty() || saldoStr.isBlank()) continue;
                if (!isNumeric(saldoStr)) continue;
                if (haveBlanks(saldoStr)) continue;

                saldo = Double.parseDouble((saldoStr));

                if (saldo <= 0) continue;

                break;
            }

            System.out.println("//// Usuario Registrado con Exito ////");

            //AGREGADO (saldo)
            usermap.put(dni, new Usuario(username, password, nombre, apellido, saldo, USER, dni));
            guardarArchivo(usermap,"usuarios.txt");
            break;
        }
    }

    public static Usuario getUserloged() {
        return userloged;
    }

    public static Usuario getUserunregistered() {
        return userunregistered;
    }

    public static void setUserloged(Usuario userloged) {
        Autenticacion.userloged = userloged;
    }

}
