package com.conexion.db;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class InventarioGUI extends JFrame {
    private ProductoDAO productoDAO = new ProductoDAO();
    private JTextField txtNombre, txtMarca, txtModelo, txtPrecio, txtCantidad, txtCategoria, txtBuscar;
    private JTextArea txtResultados;
    
    public InventarioGUI() {
        setTitle("Gestión de Inventario");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 2));

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        add(txtMarca);

        add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        add(txtModelo);

        add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        add(txtPrecio);

        add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        add(txtCantidad);

        add(new JLabel("Categoría:"));
        txtCategoria = new JTextField();
        add(txtCategoria);

        JButton btnAgregar = new JButton("Agregar Producto");
        btnAgregar.addActionListener(e -> agregarProducto());
        add(btnAgregar);

        JButton btnEliminar = new JButton("Eliminar Producto");
        btnEliminar.addActionListener(e -> eliminarProducto());
        add(btnEliminar);

        JButton btnActualizar = new JButton("Actualizar Producto");
        btnActualizar.addActionListener(e -> actualizarProducto());
        add(btnActualizar);

        JButton btnBuscar = new JButton("Buscar Producto");
        btnBuscar.addActionListener(e -> buscarProducto());
        add(btnBuscar);

        txtBuscar = new JTextField();
        add(txtBuscar);
        
        txtResultados = new JTextArea(5, 20);
        add(new JScrollPane(txtResultados));
    }
    
    private void agregarProducto() {
        String nombre = txtNombre.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        double precio = Double.parseDouble(txtPrecio.getText());
        int cantidad = Integer.parseInt(txtCantidad.getText());
        String categoria = txtCategoria.getText();
        productoDAO.agregarProducto(nombre, marca, modelo, precio, cantidad, categoria);
        JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
    }
    
    private void eliminarProducto() {
        String nombre = txtNombre.getText();
        productoDAO.eliminarProducto(nombre);
        JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
    }
    
    private void actualizarProducto() {
        String nombre = txtNombre.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        double precio = Double.parseDouble(txtPrecio.getText());
        int cantidad = Integer.parseInt(txtCantidad.getText());
        String categoria = txtCategoria.getText();
        productoDAO.actualizarProducto(nombre, marca, modelo, precio, cantidad, categoria);
        JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
    }
    
    private void buscarProducto() {
        String valor = txtBuscar.getText();
        List<String> resultados = productoDAO.buscarProductos("Nombre", valor);
        txtResultados.setText("");
        for (String producto : resultados) {
            txtResultados.append(producto + "\n");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventarioGUI().setVisible(true));
    }
}

