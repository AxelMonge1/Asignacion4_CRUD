/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.itson.asignacion4_262799;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author axelm
 */
public class Asignacion4_262799 {
    private static final String URL = "jdbc:mysql://localhost:3306/MyDataBase_262799";
    private static final String USER = "root"; 
    private static final String PSW = "JC9u1Lu%"; //Contraseña de su computadora
    
    /*
     * Es la sintaxis para crear la conexion a mysql  
    */
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PSW);
    }
    
    public static void crearTabla() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS clientes ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nombre VARCHAR(100),"
                + "password VARCHAR(100)"
                + ");";
        try(Connection con = getConnection();
            Statement st = con.createStatement();){
            st.execute(sql);
            System.out.println("Tabla creada con exito");
        }catch(SQLException ex){
            System.err.println(ex.getMessage());
        }    
    }
    
    public static void obtenerClientes(){
        String sql = "SELECT * FROM clientes";
        try(Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);){
            while(rs.next()){
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                System.out.println("Id: " + id + " Nombre: " + nombre);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public static boolean login(String nombre, String password){
        String sql = "SELECT * FROM clientes WHERE nombre='"
                + nombre + "' AND password='"
                + password + "';";
        try(Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);){
            return rs.next();
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public static boolean loginSeguro(String nombre, String password){
        String sql = "SELECT * FROM clientes WHERE nombre=? AND password=?";
        try(Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(sql);){
            pst.setString(1, nombre);
            pst.setString(2, password);
            try(ResultSet rs = pst.executeQuery()){
                return rs.next();
            }catch(SQLException ex){
                ex.printStackTrace();
                return false;
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public static void insertar(String nombre, String password){
        String sql = "INSERT INTO clientes(nombre, password) VALUES(?, ?);";
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, nombre);
            ps.setString(2, password);
            int filas = ps.executeUpdate();
            if(filas > 0){
                try(ResultSet rs = ps.getGeneratedKeys()){
                    while(rs.next()){
                        System.out.println("Insertado cliente con ID: " + rs.getInt(1));
                    }
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public static void obtenerClientePorId(int id){
        String sql = "SELECT id, nombre, password FROM clientes WHERE id =?;";
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    System.out.println("ID: " + rs.getInt("id")
                    + ", Nombre: " + rs.getString("nombre")
                    + ", Password: " + rs.getString("password"));
                }else{
                    System.out.println("No se encontaron clientes con el ID: " + id);
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public static void actualizar(int id, String nombre, String nuevoPassword){
        String sql = "UPDATE clientes SET nombre=?, password=? WHERE id=?;";
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, nombre);
            ps.setString(2, nuevoPassword);
            ps.setInt(3, id);
            int filas = ps.executeUpdate();
            if(filas > 0){
                System.out.println("Cliente actualizado con ID: " + id);
            }else{
                System.out.println("No se encontro algun cliente con el ID: " + id);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public static void eliminar(int id){
        String sql = "DELETE FROM clientes WHERE id=?";
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if(filas > 0){
                System.out.println("Cliente eliminado con ID: " + id);
            }else{
                System.out.println("No se encontro algun cliente con el ID: " + id);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        crearTabla();
        
        System.out.println("\n--------Inserciones--------");
        insertar("Juan", "1234");
        insertar("Maria", "abcd");
        insertar("Pedro", "qwerty");
        insertar("Ana", "pass123");
        
        System.out.println("\n--------Todos los clientes--------");
        obtenerClientes();
        
        System.out.println("\n--------Consulta por id--------");
        obtenerClientePorId(1);
        obtenerClientePorId(3);
        obtenerClientePorId(99);
        
        System.out.println("\n--------Actualizacion--------");
        actualizar(2, "Maria Actualizada", "newpass");
        obtenerClientePorId(2);
        
        System.out.println("\n--------Eliminacion--------");
        eliminar(3);
        obtenerClientes();
        
        System.out.println("\n--------Login normal--------");
        System.out.println("Login Juan/1234: " + login("Juan","1234"));
        System.out.println("Login Ana/newpass: " + login("Ana","newpass"));
        System.out.println("Login incorrecto: " + login("Ana","wrong"));
        System.out.println("\n--------Login seguro--------");
        System.out.println("Login seguro Juan/1234: " + loginSeguro("Juan", "1234"));
        System.out.println("Login seguro Ana/newpass: " + loginSeguro("Ana","newpass"));
        System.out.println("Login seguro incorrecto: " + loginSeguro("Ana","wrong"));
   }
}
