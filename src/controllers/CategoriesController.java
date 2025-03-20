
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import models.Categories;
import models.CategoriesDao;
import static models.EmployeesDao.rol_user;
import views.SystemView;


public class CategoriesController implements ActionListener, MouseListener, KeyListener {

    private Categories category;
    private CategoriesDao categoryDao;
    private SystemView views;
    String rol = rol_user;

    public CategoriesController(Categories category, CategoriesDao categoryDao, SystemView views) {
        this.category = category;
        this.categoryDao = categoryDao;
        this.views = views;
        //Botón de registra categoria
        this.views.btn_register_category.addActionListener(this);
        this.views.category_table.addMouseListener(this);
        this.views.txt_search_category.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_category) {
            if (views.txt_category_name.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los cmapos son obligatorios");
            } else {
                category.setName(views.txt_category_name.getText().trim());

                if (categoryDao.registerCategoryQuery(category)) {
                    cleanTable();
                    listAllCategories();
                    JOptionPane.showMessageDialog(null, "Categoría registrada con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar la categoría");
                }
            }
        }
    }

    //Listar categorias
    public void listAllCategories() {
        List<Categories> list = categoryDao.listCategoriesQuery(views.txt_search_category.getText());
        TableModel model = views.category_table.getModel();
        Object[] row = new Object[2];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getName();
       
        }
        views.category_table.setModel(model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.category_table) {
            int row = views.category_table.rowAtPoint(e.getPoint());
            views.txt_category_id.setText(views.category_table.getValueAt(row, 0).toString());
            views.txt_category_name.setText(views.category_table.getValueAt(row, 1).toString());
            views.btn_register_category.setEnabled(false);

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
        if (e.getSource() == views.txt_search_category) {
            //limpiaar tabla
            cleanTable();
            //Limpiar categorias
            listAllCategories();
        }
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
}
