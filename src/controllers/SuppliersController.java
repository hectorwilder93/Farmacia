
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static models.EmployeesDao.rol_user;


import models.SupplierDao;
import models.Suppliers;
import views.SystemView;


public class SuppliersController implements ActionListener, MouseListener, KeyListener{
    private Suppliers supplier;
    private SupplierDao supplierDao;
    private SystemView views;
    String rol = rol_user;
    
    DefaultTableModel model = new DefaultTableModel();

    public SuppliersController(Suppliers supplier, SupplierDao supplierDao, SystemView views) {
        this.supplier = supplier;
        this.supplierDao = supplierDao;
        this.views = views;
        
        //Botón de registrar proveedor
        this.views.btn_register_supplier.addActionListener(this);
        //Botón de modificar proveedor
        this.views.btn_update_supplier.addActionListener(this);
        //Botón de eliminar proveedores
        this.views.btn_delete_supplier.addActionListener(this);
        //Botón de cancelar
        this.views.btn_cancel_supplier.addActionListener(this);
        this.views.supplier_table.addMouseListener(this);
        this.views.txt_search_supplier.addKeyListener(this);
        this.views.jLabelSuppliers.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_supplier) {
            if (views.txt_supplier_name.getText().equals("")
                    || views.txt_supplier_description.getText().equals("")
                    || views.txt_supplier_telephone.getText().equals("")
                    || views.txt_supplier_address.getText().equals("")
                    || views.txt_supplier_email.getText().equals("")
                    || views.cmb_supplier_city.getSelectedItem().toString().equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                //Realizar inserción
                supplier.setName(views.txt_supplier_name.getText().trim());
                supplier.setDescription(views.txt_supplier_description.getText().trim());
                supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                supplier.setAddress(views.txt_supplier_address.getText().trim());
                supplier.setEmail(views.txt_supplier_email.getText().trim());
                supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
                supplier.setId(Integer.parseInt(views.txt_supplier_id.getText()));

                if (supplierDao.registerSupplierQuery(supplier)) {
                    //Limpiar la tabla
                    cleanTable();
                    //Limpiar campos
                    cleanFields();
                    //Listar la tabla
                    listAllSupplier();
                    JOptionPane.showMessageDialog(null, "Proveedor registrado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el proveedor");
                }
            }
        } else if (e.getSource() == views.btn_update_supplier) {
            if (views.txt_supplier_id.equals("")) {
                JOptionPane.showMessageDialog(null,"Selecciona una fila para continuar");
             }else{
                if (views.txt_supplier_name.getText().equals("")
                        || views.txt_supplier_address.getText().equals("") 
                        || views.txt_supplier_telephone.getText().equals("")                                                                      
                        || views.txt_supplier_email.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    supplier.setName(views.txt_supplier_name.getText().trim());
                    supplier.setDescription(views.txt_supplier_description.getText().trim());                     
                    supplier.setAddress(views.txt_supplier_address.getText().trim());
                    supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                    supplier.setEmail(views.txt_supplier_email.getText().trim());
                    supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
                    supplier.setId(Integer.parseInt(views.txt_supplier_id.getText()));
                    
                    if (supplierDao.updateSupplierQuery(supplier)) {
                        //Limpiar la tabla
                        cleanTable();
                        //Limpiar campos
                        cleanFields();
                        //Listar proveedores
                        listAllSupplier();
                        views.btn_register_supplier.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del proveedor modificados con éxito");
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del proveedor");
                    }
                }
            } 
        }else if(e.getSource() == views.btn_delete_supplier){
            int row = views.supplier_table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(null, "Debes seleecionar un proveedor para eliminar");
            }else{
                int id = Integer.parseInt(views.supplier_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar este proveedor?");
                if(question == 0 && supplierDao.deleteSupplierQuery(id) != false){
                    //Limpar tabla
                    cleanTable();
                    //Limpiar campos
                    cleanFields();
                    //Listar proveedores
                    listAllSupplier();
                     JOptionPane.showMessageDialog(null, "Proveedor eliminado con exito");
                }
            }
        }else if(e.getSource() == views.btn_cancel_supplier){
            //Limpiar los campos
            cleanFields();
            views.btn_register_supplier.setEnabled(true);
        }
    }
    
    //Listar proveedores
    public void listAllSupplier(){
        if(rol.equals("Administrador")){
            List<Suppliers> list = supplierDao.listSuppliersQuery(views.txt_search_supplier.getText());
            model = (DefaultTableModel)views.supplier_table.getModel();
            Object[] row = new Object[7];
            for(int i=0; i<list.size(); i++){
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                row[2] = list.get(i).getDescription();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getCity();
                model.addRow(row);                
            }
            views.supplier_table.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.supplier_table){
            int row = views.supplier_table.rowAtPoint(e.getPoint());
            views.txt_supplier_id.setText(views.supplier_table.getValueAt(row, 0).toString());
            views.txt_supplier_name.setText(views.supplier_table.getValueAt(row, 1).toString());
            views.txt_supplier_description.setText(views.supplier_table.getValueAt(row, 2).toString());
            views.txt_supplier_address.setText(views.supplier_table.getValueAt(row, 3).toString());
            views.txt_supplier_telephone.setText(views.supplier_table.getValueAt(row, 4).toString());
            views.txt_supplier_email.setText(views.supplier_table.getValueAt(row, 5).toString());
            views.cmb_supplier_city.setSelectedItem(views.supplier_table.getValueAt(row, 6).toString());
            //deshabilitar botones
            views.btn_register_supplier.setEnabled(false);
            views.txt_supplier_id.setEditable(false);
        }else if(e.getSource() == views.jLabelSuppliers){
            if(rol.equals("Administrador")){
                views.jTabbedPane1.setSelectedIndex(4);
                //Limpiar tabla
                cleanTable();
                //Limpiar campos
                cleanFields();
                //Listar proveedores
                listAllSupplier();
            }else{
                views.jTabbedPane1.setEnabledAt(4, false);
                views.jLabelSuppliers.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tienes privilegios de administrador para acceder a esta vista");
            }        
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource()== views.txt_search_supplier){
            //limpiar table
            cleanTable();
            //Listar proveedores
            listAllSupplier();
        }
    }
    public void cleanTable(){
        for(int i= 0; i<model.getRowCount(); i++){
            model.removeRow(i);
            i = i -1;
        }
    }
    
    public void cleanFields(){
        views.txt_supplier_id.setText("");
        views.txt_supplier_id.setEditable(true);
        views.txt_supplier_name.setText("");
        views.txt_supplier_description.setText("");
        views.txt_supplier_address.setText("");
        views.txt_supplier_telephone.setText("");        
        views.txt_supplier_email.setText("");
        views.cmb_supplier_city.setSelectedIndex(0);        
    }
    
}
