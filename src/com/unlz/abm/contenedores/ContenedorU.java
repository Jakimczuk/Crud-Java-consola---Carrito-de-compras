package com.unlz.abm.contenedores;

import com.unlz.abm.models.Rol;
import com.unlz.abm.models.Usuario;

import java.util.HashMap;
import java.util.Map;

// Contenedor USUARIOS
public class ContenedorU {

    private Map<Integer, Usuario> userMap = new HashMap<>();

    public ContenedorU(String nombre, String password, Rol rol) {
        this.userMap.put(0,new Usuario(nombre,password,Rol.USER_ADMIN));
    }

    public Map<Integer, Usuario> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<Integer, Usuario> userMap) {
        this.userMap = userMap;
    }

    public void VerUsuarios(){
        this.userMap.forEach((k, v) -> System.out.println(v));
    }
}
