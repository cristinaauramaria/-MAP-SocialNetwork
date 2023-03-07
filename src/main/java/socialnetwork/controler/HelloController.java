package socialnetwork.controler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import socialnetwork.repository.database.UtilizatorDbRepository;
import socialnetwork.service.UtilizatorService;

import java.security.Provider;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

}