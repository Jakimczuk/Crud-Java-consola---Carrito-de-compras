package com.unlz.abm.processes;

import com.unlz.abm.models.Articulo;
import com.unlz.abm.models.Usuario;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Archivo implements Serializable {

    static final long serialVersionUID = 7818375828146090155L;
    public static void guardarArchivo(Map<Integer, Usuario> usermap, String RUTA) {
        File archivo = new File(RUTA);
        try {
            archivo.createNewFile();
            FileOutputStream fos = new FileOutputStream(archivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(usermap);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Usuario> cargarArchivo(String RUTA){
        File archivo = new File(RUTA);
        HashMap<Integer, Usuario> users = null;
        try {
            FileInputStream fis = new FileInputStream(archivo);
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (HashMap<Integer, Usuario>) ois.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static Map<Integer, Usuario> cargarArchivo(Map<Integer, Usuario> usermap, String RUTA){
        File archivo = new File(RUTA);
        HashMap<Integer, Usuario> users = null;
        try {
            FileInputStream fis = new FileInputStream(archivo);
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (HashMap<Integer, Usuario>) ois.readObject();
        } catch (FileNotFoundException e) {
            guardarArchivo(usermap, RUTA);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static Map<Integer, Articulo> cargarArchivoArticulo(String RUTA){
        File archivo = new File(RUTA);
        HashMap<Integer, Articulo> articuloHashMap = null;
        try {
            FileInputStream fis = new FileInputStream(archivo);
            ObjectInputStream ois = new ObjectInputStream(fis);
            articuloHashMap = (HashMap<Integer, Articulo>) ois.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return articuloHashMap;
    }

    public static Map<Integer, Articulo> cargarArchivoArticulo(Map<Integer, Articulo> articuloMap, String RUTA){
        File archivo = new File(RUTA);
        HashMap<Integer, Articulo> articuloHashMap = null;
        try {
            FileInputStream fis = new FileInputStream(archivo);
            ObjectInputStream ois = new ObjectInputStream(fis);
            articuloHashMap = (HashMap<Integer, Articulo>) ois.readObject();
        } catch (FileNotFoundException e) {
            guardarArchivoArticulo(articuloMap, RUTA);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return articuloHashMap;
    }

    public static void guardarArchivoArticulo(Map<Integer, Articulo> articuloMap, String RUTA) {
        File archivo = new File(RUTA);
        try {
            archivo.createNewFile();
            FileOutputStream fos = new FileOutputStream(archivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(articuloMap);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String verUsuariosArchivo(){
        Map<Integer, Usuario> usermapArchivo = cargarArchivo("usuarios.txt");
        StringBuilder sb = new StringBuilder("//////////////////////////////////////");

        if (usermapArchivo == null) {
            return "No hay Usuarios en la base de datos.";
        }

        usermapArchivo.forEach((k, v) -> {
            sb.append("\nID: ").append(v.getId()).append("\n");
            sb.append("Nombre de Usuario: ").append(v.getUsername()).append("\n");
            sb.append("Contraseña: ").append(v.getPassword()).append("\n");
            if (v.getFirstname() != null && v.getLastname() != null) {
                sb.append("Nombre y Apellido: ").append(v.getNombreYapellido()).append("\n");
            }
            if(v.getDni() != 0){
                sb.append("DNI: ").append(k).append("\n");
            }
            sb.append("Dinero: ").append(v.getDinero()).append("\n");
            sb.append("////////////////////////////");
        });
        return String.valueOf(sb);
    }


}
