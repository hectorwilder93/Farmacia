
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import static models.EmployeesDao.rol_user;
import models.SupplierDao;
import models.Suppliers;
import views.SystemView;


public class SuppliersController implements ActionListener{
    private Suppliers supplier;
    private SupplierDao supplierDao;
    private SystemView views;
    String rol = rol_user;

    public SuppliersController(Suppliers supplier, SupplierDao supplierDao, SystemView views) {
        this.supplier = supplier;
        this.supplierDao = supplierDao;
        this.views = views;
        
        //Botón de registrar proveedor
        this.views.btn_register_supplier.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_register_supplier){
            if(views.txt_supplier_name.getText().equals("")
                    ||views.txt_supplier_description.getText().equals("")                   
                    ||views.txt_supplier_telephone.getText().equals("")
                    ||views.txt_supplier_address.getText().equals("")
                    ||views.txt_supplier_email.getText().equals("")
                    ||views.cmb_supplier_city.getSelectedItem().toString().equals("")){
                
                JOptionPane.showMessageDialog(null,"Todos los campos son obligatorios");                
            }else{
                //Realizar inserción
                supplier.setName(views.txt_supplier_name.getText().trim());
                supplier.setDescription(views.txt_supplier_description.getText().trim());
                supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                supplier.setAddress(views.txt_supplier_address.getText().trim());                
                supplier.setEmail(views.txt_supplier_email.getText().trim());
                supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
                
                if(supplierDao.registerSupplierQuery(supplier)){
                  JOptionPane.showMessageDialog(null,"Proveedor registrado exitosamente");  
                }else{
                    JOptionPane.showMessageDialog(null,"Ha ocurrido un error al registrar el proveedor"); 
                }
            }
        }        
    } 
    
}
