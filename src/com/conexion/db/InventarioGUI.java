package com.conexion.db;

import javax.swing.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InventarioGUI extends JFrame {
	private static final long serialVersionUID = 1L;
    private ProductoDAO productoDAO = new ProductoDAO();
    private JTextField txtNombre, txtMarca, txtModelo, txtPrecio, txtCantidad, txtCategoria, txtBuscar;
    private DefaultTableModel modeloTabla;
    private JTable tablaResultados;

    public InventarioGUI() {
        setTitle("Gestión de Inventario");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // Panel de entrada de datos
        JPanel panelEntrada = new JPanel(new GridLayout(6, 2, 5, 5));
        panelEntrada.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelEntrada.add(txtNombre);
        
        panelEntrada.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        panelEntrada.add(txtMarca);
        
        panelEntrada.add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        panelEntrada.add(txtModelo);
        
        panelEntrada.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelEntrada.add(txtPrecio);
        
        panelEntrada.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        panelEntrada.add(txtCantidad);
        
        panelEntrada.add(new JLabel("Categoría:"));
        txtCategoria = new JTextField();
        panelEntrada.add(txtCategoria);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(crearBoton("Agregar", e -> agregarProducto()));
        panelBotones.add(crearBoton("Actualizar", e -> actualizarProducto()));
        panelBotones.add(crearBoton("Eliminar", e -> eliminarProducto()));

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        txtBuscar = new JTextField();
        JButton btnBuscar = crearBoton("Buscar", e -> buscarProducto());
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        // Tabla de resultados
        modeloTabla = new DefaultTableModel(
            new Object[]{"Nombre", "Marca", "Modelo", "Precio", "Cantidad", "Categoría"}, 0
        );
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Evento doble click
        tablaResultados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    cargarDatosSeleccionados();
                }
            }
        });

        // Ensamblar interfaz
        add(panelEntrada, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);
        add(new JScrollPane(tablaResultados), BorderLayout.CENTER);
        add(panelBusqueda, BorderLayout.NORTH);

        actualizarTabla();
        setVisible(true);
    }

    private JButton crearBoton(String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(accion);
        return boton;
    }

    private boolean validarCamposNumericos() {
        try {
            Double.parseDouble(txtPrecio.getText());
            Integer.parseInt(txtCantidad.getText());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en formato numérico", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private Producto obtenerProductoDeCampos() {
        Producto p = new Producto();
        p.setNombre(txtNombre.getText());
        p.setMarca(txtMarca.getText());
        p.setModelo(txtModelo.getText());
        p.setPrecio(Double.parseDouble(txtPrecio.getText()));
        p.setCantidad(Integer.parseInt(txtCantidad.getText()));
        p.setCategoria(txtCategoria.getText());
        return p;
    }

    private void agregarProducto() {
        try {
            if (!validarCamposNumericos()) return;
            productoDAO.agregarProducto(obtenerProductoDeCampos());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Producto agregado exitosamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        try {
            productoDAO.eliminarProducto(txtNombre.getText());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        try {
            if (!validarCamposNumericos()) return;
            productoDAO.actualizarProducto(obtenerProductoDeCampos());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProducto() {
        try {
            List<Producto> resultados = productoDAO.buscarProductos("Nombre", txtBuscar.getText());
            modeloTabla.setRowCount(0);
            for (Producto p : resultados) {
                modeloTabla.addRow(new Object[]{
                    p.getNombre(), p.getMarca(), p.getModelo(),
                    p.getPrecio(), p.getCantidad(), p.getCategoria()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tablaResultados.getSelectedRow();
        txtNombre.setText(tablaResultados.getValueAt(fila, 0).toString());
        txtMarca.setText(tablaResultados.getValueAt(fila, 1).toString());
        txtModelo.setText(tablaResultados.getValueAt(fila, 2).toString());
        txtPrecio.setText(tablaResultados.getValueAt(fila, 3).toString());
        txtCantidad.setText(tablaResultados.getValueAt(fila, 4).toString());
        txtCategoria.setText(tablaResultados.getValueAt(fila, 5).toString());
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoDAO.obtenerTodosProductos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getNombre(), p.getMarca(), p.getModelo(),
                p.getPrecio(), p.getCantidad(), p.getCategoria()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventarioGUI());
    }
}