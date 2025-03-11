package com.conexion.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

public class FormularioProductoDialog extends JDialog {
	// Cambiar la declaración de los campos a public
    private JTextField txtNombre = new JTextField(20);
    private JTextField txtMarca = new JTextField(20);
    private JTextField txtModelo = new JTextField(20);
    private JTextField txtPrecio = new JTextField(20);
    private JTextField txtCantidad = new JTextField(20);
    private JTextField txtCategoria = new JTextField(20);
	private boolean okPressed = false;
    
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtMarca() { return txtMarca; }
    public JTextField getTxtModelo() { return txtModelo; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JTextField getTxtCantidad() { return txtCantidad; }
    public JTextField getTxtCategoria() { return txtCategoria; }

    public FormularioProductoDialog(Frame parent) {
        super(parent, "Nuevo Producto", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Marca:"));
        panel.add(txtMarca);
        panel.add(new JLabel("Modelo:"));
        panel.add(txtModelo);
        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);
        panel.add(new JLabel("Categoría:"));
        panel.add(txtCategoria);

        JButton btnOk = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");

        btnOk.addActionListener(e -> {
            if(validarCampos()) {
                okPressed = true;
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnOk);
        panelBotones.add(btnCancel);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        configurarValidacionNumerica();
    }

    private void configurarValidacionNumerica() {
        // Validación para precio
        txtPrecio.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                char decimalSeparator = new DecimalFormatSymbols().getDecimalSeparator();
                
                if (!(Character.isDigit(c) || 
                     c == KeyEvent.VK_BACK_SPACE || 
                     c == KeyEvent.VK_DELETE ||
                     c == decimalSeparator)) {
                    e.consume();
                }
            }
        });
        
        // Validación para cantidad
        txtCantidad.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || 
                     c == KeyEvent.VK_BACK_SPACE || 
                     c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });
    }

    private boolean validarCampos() {
        try {
            NumberFormat format = NumberFormat.getInstance();
            
            if(txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es requerido", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if(txtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El precio es requerido", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if(txtCantidad.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La cantidad es requerida", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            double precio = format.parse(txtPrecio.getText().trim()).doubleValue();
            if(precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            int cantidad = format.parse(txtCantidad.getText().trim()).intValue();
            if(cantidad < 0) {
                JOptionPane.showMessageDialog(this, "La cantidad no puede ser negativa", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            return true;
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato numérico inválido\nEjemplos válidos:\n- 1000\n- 1,000.50\n- 1.000,50", 
                "Error de formato", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public Producto getProducto() {
        try {
            NumberFormat format = NumberFormat.getInstance();
            return new Producto(
                txtNombre.getText().trim(),
                txtMarca.getText().trim(),
                txtModelo.getText().trim(),
                format.parse(txtPrecio.getText().trim()).doubleValue(),
                format.parse(txtCantidad.getText().trim()).intValue(),
                txtCategoria.getText().trim()
            );
        } catch (ParseException e) {
            return null;
        }
    }

    public boolean isOkPressed() {
        return okPressed;
    }
}
