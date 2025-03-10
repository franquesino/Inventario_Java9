package com.conexion.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductoDAO {
    public void agregarProducto(Producto producto) {
        String sql = "INSERT INTO InventarioProductos VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getMarca());
            pstmt.setString(3, producto.getModelo());
            pstmt.setDouble(4, producto.getPrecio());
            pstmt.setInt(5, producto.getCantidad());
            pstmt.setString(6, producto.getCategoria());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar producto: " + e.getMessage());
        }
    }

    public void eliminarProducto(String nombre) {
        String sql = "DELETE FROM InventarioProductos WHERE Nombre = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage());
        }
    }
    
    public void actualizarProducto(Producto producto) {
        String sql = "UPDATE InventarioProductos SET Marca = ?, Modelo = ?, Precio = ?, "
                   + "Cantidad = ?, Categoria = ? WHERE Nombre = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getMarca());
            pstmt.setString(2, producto.getModelo());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getCantidad());
            pstmt.setString(5, producto.getCategoria());
            pstmt.setString(6, producto.getNombre());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage());
        }
    }

    public List<Producto> buscarProductos(String criterio, String valor) {
        List<Producto> productos = new ArrayList<>();
        List<String> criteriosValidos = Arrays.asList("Nombre", "Marca", "Modelo", "Categoria");
        
        if (!criteriosValidos.contains(criterio)) {
            throw new IllegalArgumentException("Criterio de búsqueda no válido");
        }
        
        String sql = "SELECT * FROM InventarioProductos WHERE " + criterio + " LIKE ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + valor + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Producto p = new Producto();
                p.setNombre(rs.getString("Nombre"));
                p.setMarca(rs.getString("Marca"));
                p.setModelo(rs.getString("Modelo"));
                p.setPrecio(rs.getDouble("Precio"));
                p.setCantidad(rs.getInt("Cantidad"));
                p.setCategoria(rs.getString("Categoria"));
                productos.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en búsqueda: " + e.getMessage());
        }
        return productos;
    }
    
    public List<Producto> obtenerTodosProductos() {
        return buscarProductos("Nombre", "");
    }
}