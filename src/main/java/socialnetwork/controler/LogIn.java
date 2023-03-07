package socialnetwork.controler;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socialnetwork.HelloApplication;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.Service;
import socialnetwork.service.UtilizatorService;

import java.io.IOException;
import java.net.URL;

public class LogIn {

    @FXML
    private TextField FirstName;

    @FXML
    private TextField LastName;

    @FXML
    private PasswordField password;

    @FXML
    private Text nameErrorText;

    @FXML
    private Text passwordErrorText;
    private Stage stage;
    private Service service;


    public void setService(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    @FXML
    protected void onLogInButtonCLick(ActionEvent event) throws IOException {
        Utilizator user = service.getUserByName(FirstName.getText(), LastName.getText());
        System.out.println(user);
        if(user == null) // show a message
        {
            nameErrorText.setVisible(true);
            passwordErrorText.setVisible(false);
        }
        else if(!password.getText().equals(user.getPassword())) { // show a message
            passwordErrorText.setVisible(true);
            nameErrorText.setVisible(false);
        }
        else { // enter Application
            nameErrorText.setVisible(false);
            passwordErrorText.setVisible(false);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/HomePage.fxml"));
            Parent createaccountParent = loader.load();

            HomePage controller = loader.getController();
            controller.setProps(service, user);

            Scene createaccountScene = new Scene(createaccountParent, 500, 300);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(createaccountScene);
            window.show();
        }
    }

}