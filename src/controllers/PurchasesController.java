package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicCombobox;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import models.Purchases;
import models.PurchasesDao;
import views.SystemView;

public class PurchasesController implements KeyListener, ActionListener{
    private Purchases purchase;
    private PurchasesDao purchaseDao;
    private SystemView views;
    private int getIdSupplier = 0;
    private int item = 0;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;
    
    //Instanciar el modelo productos
    Products product = new Products();
    ProductsDao productDao = new ProductsDao();
    String rol = rol_user;

    public PurchasesController(Purchases purchase, PurchasesDao purchaseDao, SystemView views) {
        this.purchase = purchase;
        this.purchaseDao = purchaseDao;
        this.views = views;
        //Botón de agregar
        this.views.btn_add_product_to_buy.addActionListener(this);
        //Botón de comprar
        this.views.btn_confirm_purchase.addActionListener(this);
        this.views.txt_purchase_product_code.addKeyListener(this);
        this.views.txt_purchase_price.addKeyListener(this);
        
    }
    
     @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_add_product_to_buy){
            DynamicCombobox supplier_cmb = (DynamicCombobox) views.cmb_purchase_supplier.getSelectedItem();
            int supplier_id = supplier_cmb.getId();
            
            if(getIdSupplier == 0){
                getIdSupplier = supplier_id;
            }else{
                if(getIdSupplier != supplier_id){
                    JOptionPane.showMessageDialog(null, "No puede realizar una misma compra a varios proveedores");                    
                }else{
                    int amount = Integer.parseInt(views.txt_purchase_amount.getText());
                    String product_name = views.txt_purchase_product_name.getText();
                    double price = Double.parseDouble(views.txt_purchase_price.getText());
                    int purchase_id = Integer.parseInt(views.txt_purchase_id.getText());
                    String supplier_name = views.cmb_purchase_supplier.getSelectedItem().toString();
                    
                    if(amount > 0){
                        temp = (DefaultTableModel) views.purchases_table.getModel();
                        for(int i= 0; i < views.purchases_table.getRowCount(); i++){
                           if(views.purchases_table.getValueAt(i, 1).equals(views.txt_purchase_product_name.getText())){
                               JOptionPane.showMessageDialog(null, "El producto ya esta registrado en la tabla de compras");
                               views.txt_purchase_product_code.requestFocus();
                               return;
                           }                
                        }
                        ArrayList list = new ArrayList();
                        item= 1;
                        list.add(item);
                        list.add(purchase_id);
                        list.add(product_name);
                        list.add(amount);
                        list.add(price);                        
                        list.add(amount *price);
                        list.add(supplier_name);
                        
                        Object[] obj = new Object[6];
                        obj[0] = list.get(1);
                        obj[1] = list.get(2);
                        obj[2] = list.get(3);
                        obj[3] = list.get(4);
                        obj[4] = list.get(5);
                        obj[5] = list.get(6);
                        temp.addRow(obj);
                        views.purchases_table.setModel(temp);
                        cleanFieldsPurchases();
                        views.cmb_purchase_supplier.setEditable(false);
                        views.txt_purchase_product_code.requestFocus();
                        calculatePurchase();
                    }
                }
            }
        }else if(e.getSource() == views.btn_confirm_purchase){
            insertPurchases();
        }
    }
    
    private void insertPurchases(){
        double total = Double.parseDouble(views.txt_purchase_total_to_pay.getText());
        int id = purchaseDao.purchaseId();
        if(purchaseDao.registerPurchaseQuery(getIdSupplier,id, total)){
           int purchase_id = purchaseDao.purchaseId();
           
           for(int i=0; i<views.purchases_table.getRowCount();i++){
               int product_id = Integer.parseInt(views.purchases_table.getValueAt(i, 0).toString());
               int purchase_amount = Integer.parseInt(views.purchases_table.getValueAt(i, 2).toString());
               double purchase_price = Double.parseDouble(views.purchases_table.getValueAt(i, 3).toString());
               double purchase_subtotal = purchase_price * purchase_amount;
               
               //Registrar detalle de la compra
               purchaseDao.registerPurchaseDetailQuery(purchase_id, purchase_price, purchase_amount, purchase_subtotal, product_id);
           }
           cleanTableTemp();
           JOptionPane.showMessageDialog(null, "Compra generada con éxito");
           cleanFieldsPurchases();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getSource()==  views.txt_purchase_product_code){
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                if(views.txt_purchase_product_code.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Ingresa el código del producto a comprar");
                }else{
                    int id = Integer.parseInt(views.txt_purchase_product_code.getText());
                    product = productDao.searchCode(id);
                    views.txt_purchase_product_name.setText(product.getName());
                    views.txt_purchase_id.setText(""+ product.getId());
                    views.txt_purchase_amount.requestFocus();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == views.txt_purchase_price){
            int quantity;
            double price = 0.0;
            
            if(views.txt_purchase_amount.getText().equals("")){
                quantity = 1;
                views.txt_purchase_price.setText("" + price);
            }else{
                quantity = Integer.parseInt(views.txt_purchase_amount.getText());
                price = Double.parseDouble(views.txt_purchase_price.getText());
                views.txt_purchase_subtotal.setText(""+ quantity * price);
            }
        }
    } 
    
    //Limpiar campos
    public void cleanFieldsPurchases(){
        views.txt_purchase_product_name.setText("");
        views.txt_purchase_price.setText("");
        views.txt_purchase_amount.setText("");
        views.txt_purchase_product_code.setText("");
        views.txt_purchase_subtotal.setText("");
        views.txt_purchase_id.setText("");
        views.txt_purchase_total_to_pay.setText("");
    }
    
    //calcular a pagar
    public void calculatePurchase(){
        double total = 0.00;
        int numRow = views.purchases_table.getRowCount();
        
        for(int i= 0; i < numRow; i++){
            //Pasa el indice de la columna que se sumará
            total = total + Double.parseDouble(String.valueOf(views.purchases_table.getValueAt(i,4)));
        }
        views.txt_purchase_total_to_pay.setText("" + total);
    }
    
    //Limpiar tabla temporal
    public void cleanTableTemp(){
        for(int i = 0; i<temp.getRowCount();i++){
            temp.removeRow(i);
            i = i -1;
        }
    }
    
}
