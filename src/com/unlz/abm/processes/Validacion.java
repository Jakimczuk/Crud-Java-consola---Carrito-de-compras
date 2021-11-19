package com.unlz.abm.processes;

public class Validacion {

    /**
     * Comprueba si hay espacios en blanco.
     * @param text (String)
     * @return true / false
     * **/
    public static boolean haveBlanks(String text){
        return text.contains(" ");
    }

    /**
     * Comprueba si el numero ingresado es un numero
     * @param text (String)
     * @return true / false
     * **/
    public static boolean isNumeric(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
