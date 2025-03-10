package com.conexion.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    public void agregarProducto(String nombre, String marca, String modelo, double precio, int cantidad, String categoria) {
        String sql = "INSERT INTO InventarioProductos (Nombre, Marca, Modelo, Precio, Cantidad, Categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, marca);
            pstmt.setString(3, modelo);
            pstmt.setDouble(4, precio);
            pstmt.setInt(5, cantidad);
            pstmt.setString(6, categoria);
            pstmt.executeUpdate();
            System.out.println("Producto agregado exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
        }
    }

    public void eliminarProducto(String nombre) {
        String sql = "DELETE FROM InventarioProductos WHERE Nombre = ?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
            System.out.println("Producto eliminado exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
        }
    }
    
    public void actualizarProducto(String nombre, String marca, String modelo, double precio, int cantidad, String categoria) {
        String sql = "UPDATE InventarioProductos SET Marca = ?, Modelo = ?, Precio = ?, Cantidad = ?, Categoria = ? WHERE Nombre = ?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, marca);
            pstmt.setString(2, modelo);
            pstmt.setDouble(3, precio);
            pstmt.setInt(4, cantidad);
            pstmt.setString(5, categoria);
            pstmt.setString(6, nombre);
            pstmt.executeUpdate();
            System.out.println("Producto actualizado exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    public List<String> buscarProductos(String criterio, String valor) {
        List<String> productos = new ArrayList<>();
        String sql = "SELECT * FROM InventarioProductos WHERE " + criterio + " LIKE ?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + valor + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                productos.add(rs.getString("Nombre") + " - " + rs.getString("Marca") + " - " + rs.getString("Modelo"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar productos: " + e.getMessage());
        }
        return productos;
    }
}

