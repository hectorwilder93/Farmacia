
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
import models.Customers;
import models.CustomersDao;
import views.SystemView;

    
public class CustomerController implements ActionListener, MouseListener, KeyListener{
    private Customers customers;
    private CustomersDao customersDao;
    private SystemView views;
    
    DefaultTableModel model = new DefaultTableModel();

    public CustomerController(Customers customers, CustomersDao customersDao, SystemView views) {
        this.customers = customers;
        this.customersDao = customersDao;
        this.views = views;
        
        //Boton de registrar clientes
        this.views.btn_register_customer.addActionListener(this);
        
        //Boton de modificar cliente
        this.views.btn_update_customer.addActionListener(this);
        //Boton de eliminar cliente
        this.views.btn_delete_customer.addActionListener(this);
        //Boton de cancelar
        this.views.btn_cancel_customer.addActionListener(this);
        //Buscador
        this.views.txt_search_customer.addKeyListener(this);
        this.views.jLabelCustomer.addMouseListener(this);
        this.views.customer_table.addMouseListener(this);
        
}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_customer) {
            //verificar si los campos estan vacios
            if (views.txt_customer_id.getText().equals("")
                    || views.txt_customer_fullname.getText().equals("")
                    || views.txt_customer_address.getText().equals("")
                    || views.txt_customer_telephone.getText().equals("")
                    || views.txt_customer_email.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                customers.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                customers.setFull_name(views.txt_customer_fullname.getText().trim());
                customers.setAddress(views.txt_customer_address.getText().trim());
                customers.setTelephone(views.txt_customer_telephone.getText().trim());
                customers.setEmail(views.txt_customer_email.getText().trim());

                if (customersDao.registerCustomerquery(customers)) {
                    cleanTable();
                    listAllCustomers();
                    JOptionPane.showMessageDialog(null, "Cliente registrado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al cliente");
                }
            }
        } else if (e.getSource() == views.btn_update_customer) {
            if (views.txt_customer_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Seleciona una fila para continuar");
            } else {
                if (views.txt_customer_id.getText().equals("")
                        || views.txt_customer_fullname.getText().equals("")
                        || views.txt_customer_address.getText().equals("")
                        || views.txt_customer_telephone.getText().equals("")
                        || views.txt_customer_email.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    customers.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                    customers.setFull_name(views.txt_customer_fullname.getText().trim());
                    customers.setAddress(views.txt_customer_address.getText().trim());
                    customers.setTelephone(views.txt_customer_telephone.getText().trim());
                    customers.setEmail(views.txt_customer_email.getText().trim());
                    
                    if(customersDao.updateCustomerquery(customers)){
                        cleanTable();
                        cleanFields();
                        listAllCustomers();
                        views.btn_register_customer.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del cliente modificados con éxito"); 
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del cliente");
                    }
                }
            }
        }else if(e.getSource() == views.btn_delete_customer){
            int row = views.customer_table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente para eliminar");
            }else{
                int id = Integer.parseInt(views.customer_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar a este cliente?");
                
                if(question == 0 && customersDao.deleteCustomerQuery(id) != false){
                    cleanTable();
                    cleanFields();
                    views.btn_register_customer.setEnabled(true);
                    listAllCustomers();
                    JOptionPane.showMessageDialog(null, "Cliente eliminado con éxito");
                }
            }             
        }else if(e.getSource() ==  views.btn_cancel_customer){
            views.btn_register_customer.setEnabled(true);
            cleanFields();
        }
    }
    
    //Listar clientes
    public void listAllCustomers(){
        List<Customers> list= customersDao.listCustomerQuery(views.txt_search_customer.getText());
        model = (DefaultTableModel) views.customer_table.getModel();
        model.setRowCount(0);
        
        Object[] row = new Object[5];
        for(int i = 0; i < list.size(); i++){
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFull_name();
            row[2] = list.get(i).getAddress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();
            model.addRow(row);
        }
        views.customer_table.setModel(model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.customer_table){
           int row = views.customer_table.rowAtPoint(e.getPoint());
           views.txt_customer_id.setText(views.customer_table.getValueAt(row, 0).toString());
           views.txt_customer_fullname.setText(views.customer_table.getValueAt(row, 1).toString());
           views.txt_customer_address.setText(views.customer_table.getValueAt(row, 2).toString());
           views.txt_customer_telephone.setText(views.customer_table.getValueAt(row, 3).toString());
           views.txt_customer_email.setText(views.customer_table.getValueAt(row, 4).toString());          
           
           //Deshabilitar algunos botones
           views.btn_register_customer.setEnabled(false);
           views.txt_customer_id.setEditable(false);
        }else if(e.getSource() == views.jLabelCustomer){
            views.jTabbedPane1.setSelectedIndex(2);
            cleanTable();
            cleanFields();
            listAllCustomers();
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
       if(e.getSource() == views.txt_search_customer){
           //limpiar tabla
           cleanTable();
           //listar clientes
           listAllCustomers();
       }
    }
    
    public void cleanFields(){
        views.txt_customer_id.setText("");
        views.txt_customer_id.setEditable(true);
        views.txt_customer_fullname.setText("");
        views.txt_customer_address.setText("");
        views.txt_customer_telephone.setText("");
        views.txt_customer_email.setText("");
    }
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }    
    
}
