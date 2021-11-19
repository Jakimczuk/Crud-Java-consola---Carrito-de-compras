package com.unlz.abm.models;
import com.unlz.abm.processes.Archivo;
import com.unlz.abm.processes.Proceso;

import java.io.Serializable;
import java.util.*;

import static com.unlz.abm.processes.Archivo.*;
import static com.unlz.abm.processes.Validacion.haveBlanks;
import static com.unlz.abm.processes.Validacion.isNumeric;

public class Usuario implements Serializable {
    private int dni;
    private final int id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private List<ItemFactura> carro = new ArrayList<>();
    private double dinero;
    private Rol rol;
    private static int idFinal;

    public Usuario() {
        this.id = 0;
        this.rol = null;
        this.firstname = "";
        this.lastname = "";
    }

    public Usuario(String username, String password, Rol rol) {
        this.id = idFinal++;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(String username, String password, String firstname, String lastname, double dinero, Rol rol, int dni) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dinero = dinero;
        this.rol = rol;
        //AGREGADO
        this.dni = dni;
        this.id = idFinal++;
    }

    public static void ingresarDinero(Usuario u, Map<Integer,Usuario> usermap) {
        String dineroStr;

        usermap = cargarArchivo(usermap, "usuarios.txt");

        System.out.println("///// Cargar Dinero a la Cuenta /////");
        System.out.println("Saldo actual: " + u.getDinero());
        while (true) {
            System.out.print("Ingrese monto de saldo (\"0\" para salir): ");
            dineroStr = Proceso.sc.next();

            if (dineroStr.equals("0")){
                break;
            }

            if (dineroStr.isEmpty() || dineroStr.isBlank()) continue;
            if (!isNumeric(dineroStr)) continue;
            double dinero = Double.parseDouble((dineroStr));

            if (dinero <= 0) {
                System.out.println("(!) ERROR: Saldo ingresado menor que 0 (!)");
                continue;
            }

            dinero = u.getDinero() + Double.parseDouble(dineroStr);
            usermap.get(u.getDni()).setDinero(dinero);
            u.setDinero(dinero);
            System.out.println("Nuevo saldo: " + usermap.get(u.getDni()).getDinero());
            Archivo.guardarArchivo(usermap,"usuarios.txt");
            break;
        }

    }

    public static void transferirDinero(Usuario u, Map<Integer, Usuario> usermap){
        String dniUsuarioStr;
        double dinero;
        int dniUsuario = -1;
        usermap = cargarArchivo(usermap,"usuarios.txt");
        while (dniUsuario != 0){
            int check = -1;
            System.out.println("///// Transferencia /////");
            System.out.print("Ingrese el DNI del usuario que deseas transferir (\"0\" para salir): ");
            dniUsuarioStr = Proceso.sc.next();

            if (isNumeric(dniUsuarioStr)) {
                dniUsuario = Integer.parseInt(dniUsuarioStr);
            } else {
                System.out.println("(!) ERROR: Ingrese un DNI valido. (!)");
                continue;
            }

            if(dniUsuario == u.getDni()){
                System.out.println("(!) ERROR: No puede ingresar tu DNI. (!)");
                continue;
            }

            if (dniUsuario == 0){
                break;
            }

            if (usermap.get(dniUsuario) != null){
                System.out.println("///// Usuario del DNI /////");
                System.out.println(usermap.get(dniUsuario));
                System.out.println("///////////////////////////");

                System.out.println("Tu Saldo Actual: " + u.getDinero());

                // Validacion
                while(true){
                    System.out.print("Ingrese monto de saldo a transferir: ");
                    String dineroStr = Proceso.scLong.next();

                    if (haveBlanks(dineroStr)){
                        System.out.println("(!) ERROR: El valor ingresado es invalido. (!)");
                        continue;
                    }
                    if (dineroStr.isEmpty() || dineroStr.isBlank()){
                        System.out.println("(!) ERROR: El valor ingresado es invalido. (!)");
                        continue;
                    }
                    if (!isNumeric(dineroStr)){
                        System.out.println("(!) ERROR: El valor ingresado es invalido. (!)");
                        continue;
                    }
                    dinero = Double.parseDouble((dineroStr));

                    if (dinero <= 0) {
                        System.out.println("(!) ERROR: Saldo ingresado menor que 0 (!)");
                        check = 1;
                        continue;
                    }
                    break;
                }
            } else {
                System.out.println("(!) ERROR: El Usuario no Existe (!)");
                continue;
            }

            double dineroVal = u.getDinero() - dinero;
            if(dineroVal < 0){
                System.out.println("(!) ERROR: No hay suficiente saldo (!)");
                break;
            }

            if (check != 1){
                usermap.get(dniUsuario).setDinero(usermap.get(dniUsuario).getDinero() + dinero);
                dinero = u.getDinero() - dinero;

                usermap.get(u.getDni()).setDinero(dinero); // userloged en el hashmap
                u.setDinero(dinero); // userloged

                System.out.println("Tu Saldo Actual (NUEVO): " + u.getDinero());
                System.out.println("Saldo de la persona transferida: " + usermap.get(dniUsuario).getDinero());
            }

            Archivo.guardarArchivo(usermap,"usuarios.txt");
            break;
        }
    }

    public static void retirarDinero(Usuario u, Map<Integer,Usuario> usermap){
        String dineroStr;

        usermap = cargarArchivo(usermap, "usuarios.txt");

        System.out.println("///// Retirar Dinero de la Cuenta /////");
        System.out.println("Saldo actual: " + u.getDinero());
        while (true) {
            System.out.print("Ingrese monto de saldo (\"0\" para salir): ");
            dineroStr = Proceso.scLong.next();

            if (dineroStr.equals("0")){
                break;
            }

            if (dineroStr.isEmpty() || dineroStr.isBlank()) continue;
            if (!isNumeric(dineroStr)) continue;
            if (haveBlanks(dineroStr)) continue;
            double dinero = Double.parseDouble((dineroStr));

            if (dinero < 0) {
                System.out.println("(!) ERROR: Saldo ingresado menor que 0 (!)");
                continue;
            }

            double dineroVal = u.getDinero() - dinero;
            if (dineroVal < 0){
                System.out.println("(!) ERROR: El dinero a retirar es mayor que el dinero en su cuenta (!)");
                continue;
            }

            System.out.println("////// Dinero Retirado con Exito //////");
            u.setDinero(dineroVal);
            System.out.println("Saldo actual (NUEVO): " + u.getDinero());
            Archivo.guardarArchivo(usermap,"usuarios.txt");
            break;
        }
    }

    public static void agregarAlCarro(Usuario u, Map<Integer,Usuario> usermap, Map<Integer,Articulo> articuloMap){
        System.out.println("///// Agregar Producto al Carro /////");
        String idToBuyStr,opcion = "", check = "0";
        int idToBuy;

        articuloMap = cargarArchivoArticulo(articuloMap,"articulos.txt");
        usermap = cargarArchivo(usermap,"usuarios.txt");

        while (true){
            System.out.print("Ingrese el ID del Articulo: ");
            idToBuyStr = Proceso.scLong.next();

            if (idToBuyStr.isEmpty() || idToBuyStr.isBlank()) continue;
            if (haveBlanks(idToBuyStr)) continue;
            if (isNumeric(idToBuyStr)){
                idToBuy = Integer.parseInt(idToBuyStr);
                if (articuloMap.get(idToBuy) == null) continue;
                break;
            } else {
                System.out.println("(!) ERROR: Ingrese un ID Valido. (!)");
            }
        }
        System.out.println("////////////////////////////");
        System.out.println(articuloMap.get(idToBuy));
        System.out.println("////////////////////////////");

        System.out.print("Confirmar seleccion (s/N): ");
        opcion = Proceso.scLong.next();

        switch (opcion.toLowerCase()){
            case "s":
                int cantidad = 0;
                while (true){
                    System.out.print("Cantidad: ");
                    String cantidadStr = Proceso.scLong.next();

                    if (cantidadStr.isEmpty()) continue;
                    if (haveBlanks(cantidadStr)) continue;
                    if (!isNumeric(cantidadStr)) continue;

                    cantidad = Integer.parseInt(cantidadStr);
                    if (cantidad < 0){
                        System.out.println("ERROR: El Valor ingresado es menor a 0.");
                        continue;
                    }

                    break;
                }

                int stock = articuloMap.get(idToBuy).getStock();
                if (stock < cantidad){
                    System.out.println("(!) ERROR: La cantidad supera al stock del Articulo.");
                    break;
                }

                /* else: Disminuye el stcok
                else{
                    articuloMap.get(idToBuy).setStock(stock - cantidad);
                }
                */

                Articulo articulo = articuloMap.get(idToBuy);
                ItemFactura articuloCompra = new ItemFactura(articulo,cantidad);

                u.addArticulo(articuloCompra);
                check = "1";
                usermap.get(u.getDni()).addArticulo(articuloCompra);
                break;
            case "n":
                System.out.println("Saliendo de \"Agregar Producto al Carro\" ...");
                break;
            default:
                System.out.println("(!) ERROR: Ingrese una opcion valida. (!)");
                break;
        }

        guardarArchivo(usermap,"usuarios.txt");
        guardarArchivoArticulo(articuloMap,"articulos.txt");
    }

    public void agregarArticulo(Map<Integer,Articulo> articuloMap){
        String codigoStr;
        int codigo = -2;
        int check;
        articuloMap = cargarArchivoArticulo(articuloMap,"articulos.txt");

        do{
            check = 0;
            System.out.println("///// Agregar Producto /////");
            while (true) {
                System.out.print("Ingrese Codigo de Producto: ");
                codigoStr = Proceso.scLong.next();

                if (isNumeric(codigoStr)) {
                    codigo = Integer.parseInt(codigoStr);
                }
                if (codigo < 0){
                    System.out.println("(!) ERROR: El Valor Ingresado es Invalido");
                    continue;
                }
                break;
            }

            Collection<Articulo> articulos = articuloMap.values();

            for (Articulo articulo : articulos) {
                if (codigo == articulo.getCodigo()) {
                    System.out.println("(!) ERROR: Este codigo ya existe en otro articulo. (!)");
                    check = 1;
                    break;
                }
            }

        }while (check == 1);

        String nombreArticulo;
        while (true){
            System.out.print("Ingrese Nombre del Articulo: ");
            nombreArticulo = Proceso.scLong.next();

            if (nombreArticulo.isEmpty() || nombreArticulo.isBlank()) continue;
            if (isNumeric(nombreArticulo)) continue;
            if (haveBlanks(nombreArticulo)) continue;
            break;
        }

        String descripcion;
        while (true){
            System.out.print("Ingrese una Descripcion: ");
            descripcion = Proceso.scLong.next();

            if (descripcion.isEmpty() || descripcion.isBlank()) continue;
            if (isNumeric(descripcion)) continue;
            break;
        }

        double precio = 0;
        while (true){
            System.out.print("Ingrese el Precio: ");
            String precioStr = Proceso.scLong.next();

            if (haveBlanks(precioStr)) continue;
            if (precioStr.isEmpty() || precioStr.isBlank()) continue;
            if (isNumeric(precioStr)) precio = Double.parseDouble(precioStr);
            if (precio < 0) continue;
            break;
        }

        int stock = 0;
        while (true){
            System.out.print("Ingrese el Stock: ");
            String stockStr = Proceso.scLong.next();

            if (haveBlanks(stockStr)) continue;
            if (stockStr.isEmpty() || stockStr.isBlank()) continue;
            if (isNumeric(stockStr)) stock = Integer.parseInt(stockStr);
            if (stock < 0) continue;
            break;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("////// Confirmación //////").append("\n");
        sb.append("Codigo: ").append(codigo).append("\n");
        sb.append("Nombre: ").append(nombreArticulo).append("\n");
        sb.append("Descripcion: ").append(descripcion).append("\n");
        sb.append("Precio: ").append(precio).append("\n");
        sb.append("Stock: ").append(stock).append("\n");
        sb.append("//////////////////////////").append("\n");

        System.out.print("Confirmar (s/N): ");
        while (true){
            String confirmar = Proceso.sc.next();
            if (confirmar.equalsIgnoreCase("s")){
                articuloMap.put(codigo,new Articulo(descripcion,descripcion,precio,stock,codigo));
                break;
            }
            if (confirmar.equalsIgnoreCase("n")){
                System.out.println("... Proceso \"Crear Articulo\" Cancelado ...");
                break;
            }
        }
        guardarArchivoArticulo(articuloMap, "articulos.txt");
    }

    public void eliminarArticulo(Map<Integer,Articulo> articuloMap){
        System.out.println("///// Eliminar Producto /////");
        String idToRemoveStr;
        int idToRemove;
        articuloMap = cargarArchivoArticulo(articuloMap,"articulos.txt");

        while (true){
            System.out.print("Ingrese el ID del Articulo: ");
            idToRemoveStr = Proceso.sc.next();

            if (isNumeric(idToRemoveStr)){
                idToRemove = Integer.parseInt(idToRemoveStr);
                break;
            }else{
                System.out.println("(!) ERROR: Ingrese un ID Valido. (!)");
            }
        }

        if (articuloMap.get(idToRemove) == null){
            System.out.println("/////////////////////////////");
            System.out.println("! El ID ingresado No existe !");
            System.out.println("/////////////////////////////");
        } else {
            System.out.println("//// Producto a Eliminar ////");
            System.out.println(articuloMap.get(idToRemove));
            System.out.println("/////////////////////////////");
            System.out.print("Confirmar (s/N): ");
            while (true){
                String confirmar = Proceso.sc.next();
                if (confirmar.equalsIgnoreCase("s")){
                    articuloMap.remove(idToRemove);
                    break;
                }
                if (confirmar.equalsIgnoreCase("n")){
                    break;
                }
            }
        }
        guardarArchivoArticulo(articuloMap, "articulos.txt");
    }

    public void editarArticulo(Map<Integer,Articulo> articuloMap){
        System.out.println("///// Editar Producto /////");
        String idToEditStr;
        int idToEdit;
        articuloMap = cargarArchivoArticulo(articuloMap,"articulos.txt");

        while (true){
            System.out.print("Ingrese el ID del Articulo: ");
            idToEditStr = Proceso.sc.next();

            if (isNumeric(idToEditStr)){
                idToEdit = Integer.parseInt(idToEditStr);
                break;
            }else{
                System.out.println("(!) ERROR: Ingrese un ID Valido. (!)");
            }
        }

        String opcion = "";
        if (articuloMap.get(idToEdit) == null){
            System.out.println("///////////////////////////");
            System.out.println("! El ID ingresado No existe !");
            System.out.println("///////////////////////////");
        } else {
            while (!opcion.equals("0")){
                System.out.println("//// Producto a Editar ////");
                System.out.println(articuloMap.get(idToEdit));
                System.out.print("///////////////////////////");
                StringBuilder sb = new StringBuilder();
                sb.append("\n").append("1 - Editar Nombre");
                sb.append("\n").append("2 - Editar Descripcion");
                sb.append("\n").append("3 - Editar Precio");
                sb.append("\n").append("4 - Editar Stock");
                sb.append("\n").append("5 - Editar Codigo");
                sb.append("\n").append("0 - Salir");
                sb.append("\n").append("opcion: ");
                System.out.print(sb);
                opcion = Proceso.scLong.next();
                switch (opcion){
                    //AGREGADO TODAS LAS VALIDACIONES
                    case "1": // Editar Nombre
                        System.out.println("Nombre Actual del Articulo: " + articuloMap.get(idToEdit).getNombre());
                        System.out.print("Ingrese el Nuevo Nombre: ");
                        String newNombre = Proceso.scLong.next();
                        if (isNumeric(newNombre)) {
                            System.out.println("(!) ERROR: El nombre ingresado es invalido (!)");
                            continue;
                        }
                        articuloMap.get(idToEdit).setNombre(newNombre);
                        break;
                    case "2": // Editar Descripción
                        System.out.println("Descripción Actual del Articulo: \n" + articuloMap.get(idToEdit).getDescripcion());
                        System.out.print("Ingrese la nueva descripcion del Articulo: ");
                        String newDescripcion = Proceso.scLong.next();
                        if (isNumeric(newDescripcion)) {
                            System.out.println("(!) ERROR: La descripcion ingresada no es valida (!)");
                            continue;
                        }
                        articuloMap.get(idToEdit).setDescripcion(newDescripcion);
                        break;
                    case "3": // Editar Precio
                        System.out.println("Precio Actual del Articulo: " + articuloMap.get(idToEdit).getPrecio());
                        System.out.print("Ingrese el Nuevo Precio: ");
                        String newPrecioStr = Proceso.sc.next();

                        if (newPrecioStr.isEmpty() || newPrecioStr.isBlank()) continue;
                        if (!isNumeric(newPrecioStr)) continue;
                        if (haveBlanks(newPrecioStr)) continue;

                        double newPrecio = Double.parseDouble((newPrecioStr));

                        if (newPrecio <= 0){
                            System.out.println("(!) ERROR: Precio ingresado menor a 0 (!)");
                            continue;
                        }
                        articuloMap.get(idToEdit).setPrecio(newPrecio);
                        break;
                    case "4": // Editar Stock
                        System.out.println("Stock Actual del Articulo: " + articuloMap.get(idToEdit).getStock());
                        System.out.print("Ingrese Nuevo Limite de Stock: ");
                        String newStockStr = Proceso.sc.next();

                        if (newStockStr.isEmpty() || newStockStr.isBlank()) continue;
                        if (!isNumeric(newStockStr)) continue;
                        if (haveBlanks(newStockStr)) continue;

                        int newStock = Integer.parseInt((newStockStr));

                        if (newStock <= 0){
                            System.out.println("(!) ERROR: Stock ingresado menor a 0 (!)");
                            continue;
                        }
                        articuloMap.get(idToEdit).setStock(newStock);
                        break;
                    case "0":
                        System.out.println("Volviendo al Menu Tienda...");
                        break;
                }
            }
        }
        guardarArchivoArticulo(articuloMap, "articulos.txt");
    }

    public void verArticulos(Map<Integer,Articulo> articuloMap){

        articuloMap = cargarArchivoArticulo(articuloMap,"articulos.txt");

        Collection<Articulo> articulos = articuloMap.values();

        for (Articulo articulo : articulos) {
            System.out.println("//////////////////////");
            System.out.println(articulo);
        }
        System.out.println("//////////////////////");
    }

    public void confirmarCompra(Map<Integer,Articulo> articuloMap, Map<Integer,Usuario> usermap, Usuario u){
        // Hacer
    }

    public String verCarro(){
        StringBuilder sb = new StringBuilder();
        sb.append("/////////////////////").append("\n");
        for (ItemFactura producto : this.carro) {
            sb.append(producto.getArticulo()).append("\n");
            sb.append("Cantidad: \t").append(producto.getCantidad()).append("\n");
            sb.append("/////////////////////");
        }
        return String.valueOf(sb);

    }

    public List<ItemFactura> getCarro() {
        return carro;
    }

    public void setCarro(List<ItemFactura> carro) {
        this.carro = carro;
    }

    public void addArticulo(ItemFactura a){
        this.carro.add(a);
    }

    public int getId() {
        return id;
    }

    public static int getIdFinal() {
        return idFinal;
    }

    public String getNombreYapellido(){
        return getFirstname() + " " + getLastname();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    //AGREGADO
    public double getDinero() {
        return dinero;
    }
    //AGREGADO
    public void setDinero(double dinero) {
        this.dinero = dinero;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public int getDni() {
        return dni;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("-= Usuario " + this.rol + " =-")
                .append("\nUsuario: ").append(this.username)
                .append("\nContraseña: ").append(this.password);
        if (this.firstname != null && this.lastname != null && this.dni != 0){
                    sb.append("\nNombre y Apellido: ").append(getNombreYapellido())
                    .append("\nDNI: ").append(getDni())
                    //AGREGADO
                    .append("\nSaldo: ").append(getDinero());
        }
        return String.valueOf(sb);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(username, usuario.username) && Objects.equals(password, usuario.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}