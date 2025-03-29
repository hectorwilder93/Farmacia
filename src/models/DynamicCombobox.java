
package models;


public class DynamicCombobox {
    private int id;
    private String displayText; // Nombre a mostrar
    
    // Constructor
    public DynamicCombobox(int id, String displayText) {
        this.id = id;
        this.displayText = displayText;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDisplayText() {
        return displayText;
    }
    
    @Override
    public String toString() {
        return displayText; // Esto es lo que mostrará el JComboBox
    }
}
