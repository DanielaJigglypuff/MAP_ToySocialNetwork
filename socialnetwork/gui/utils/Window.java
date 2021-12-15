package socialnetwork.gui.utils;

import javafx.scene.control.Alert;

public class Window {
    public static void ALERT(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
    public static void CONFIRMATION(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
