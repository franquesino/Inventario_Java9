package com.conexion.db;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InventarioGUI extends JFrame {
    private ProductoDAO productoDAO = new ProductoDAO();
    private JTextField txtBuscar;
    private DefaultTableModel modeloTabla;
    private JTable tablaResultados;
    private JComboBox<String> comboBusqueda;

    public InventarioGUI() {
        setTitle("Gestión de Inventario");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // Panel de búsqueda mejorado
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        
        // Combo box para criterios de búsqueda
        comboBusqueda = new JComboBox<>(new String[]{"Nombre", "Marca", "Categoría"});
        panelBusqueda.add(comboBusqueda, BorderLayout.WEST);
        
        txtBuscar = new JTextField();
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarProducto());
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        // Tabla de resultados
        modeloTabla = new DefaultTableModel(
            new Object[]{"Nombre", "Marca", "Modelo", "Precio", "Cantidad", "Categoría"}, 0
        );
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Evento doble click para editar
        tablaResultados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    cargarDatosSeleccionados();
                }
            }
        });

        // Botones principales
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAgregar = new JButton("Agregar Producto");
        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarProducto());
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarProducto());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        // Ensamblar interfaz
        add(panelBusqueda, BorderLayout.NORTH);
        add(new JScrollPane(tablaResultados), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        actualizarTabla();
        setVisible(true);
    }

    private void mostrarFormularioAgregar() {
        FormularioProductoDialog dialog = new FormularioProductoDialog(this);
        dialog.setVisible(true);
        
        if(dialog.isOkPressed()) {
            Producto nuevoProducto = dialog.getProducto();
            if(nuevoProducto != null) {
                productoDAO.agregarProducto(nuevoProducto);
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente!");
            }
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoDAO.obtenerTodosProductos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getNombre(),
                p.getMarca(),
                p.getModelo(),
                String.format("$%,.2f", p.getPrecio()),
                p.getCantidad(),
                p.getCategoria()
            });
        }
    }

    private void buscarProducto() {
        try {
            String criterio = mapearCriterioBusqueda((String) comboBusqueda.getSelectedItem());
            String valor = txtBuscar.getText();
            
            List<Producto> resultados = productoDAO.buscarProductos(criterio, valor);
            
            modeloTabla.setRowCount(0);
            for (Producto p : resultados) {
                modeloTabla.addRow(new Object[]{
                    p.getNombre(),
                    p.getMarca(),
                    p.getModelo(),
                    String.format("$%,.2f", p.getPrecio()),
                    p.getCantidad(),
                    p.getCategoria()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en búsqueda: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String mapearCriterioBusqueda(String criterioVisible) {
        return switch (criterioVisible) {
            case "Categoría" -> "Categoria";
            default -> criterioVisible;
        };
    }

    private void cargarDatosSeleccionados() {
        int fila = tablaResultados.getSelectedRow();
        if(fila >= 0) {
            FormularioProductoDialog dialog = new FormularioProductoDialog(this);
            
            dialog.getTxtNombre().setText(tablaResultados.getValueAt(fila, 0).toString());
            dialog.getTxtMarca().setText(tablaResultados.getValueAt(fila, 1).toString());
            dialog.getTxtModelo().setText(tablaResultados.getValueAt(fila, 2).toString());
            dialog.getTxtPrecio().setText(tablaResultados.getValueAt(fila, 3).toString().replace("$", ""));
            dialog.getTxtCantidad().setText(tablaResultados.getValueAt(fila, 4).toString());
            dialog.getTxtCategoria().setText(tablaResultados.getValueAt(fila, 5).toString());
            
            dialog.setVisible(true);
            
            if(dialog.isOkPressed()) {
                Producto productoActualizado = dialog.getProducto();
                productoDAO.actualizarProducto(productoActualizado);
                actualizarTabla();
            }
        }
    }

    private void eliminarProducto() {
        int fila = tablaResultados.getSelectedRow();
        if(fila >= 0) {
            String nombre = tablaResultados.getValueAt(fila, 0).toString();
            productoDAO.eliminarProducto(nombre);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarProducto() {
        cargarDatosSeleccionados();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventarioGUI());
    }
}