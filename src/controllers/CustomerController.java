
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.Customers;
import models.CustomersDao;
import views.SystemView;


public class CustomerController implements ActionListener{
    private Customers customers;
    private CustomersDao customersDao;
    private SystemView views;

    public CustomerController(Customers customers, CustomersDao customersDao, SystemView views) {
        this.customers = customers;
        this.customersDao = customersDao;
        this.views = views;
        
        //Boton de registrar clientes
        this.views.btn_register_customer.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_register_customer){
            //verificar si los campos estan vacios
            if(views.txt_customer_id.getText().equals("")
                || views.txt_customer_fullname.getText().equals("")
                || views.txt_customer_address.getText().equals("")
                || views.txt_customer_telephone.getText().equals("")
                || views.txt_customer_email.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        }else{
                customers.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                customers.setFull_name(views.txt_customer_fullname.getText().trim());
                customers.setAddress(views.txt_customer_address.getText().trim());
                customers.setEmail(views.txt_customer_email.getText().trim());
               
                if(customersDao.registerCustomerquery(customers)){
                    JOptionPane.showMessageDialog(null, "Cliente registrado con exito");
                }else{
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al cliente");
                }
            }
        }
    }    
}
