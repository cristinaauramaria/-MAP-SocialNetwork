package socialnetwork.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.Service;
import socialnetwork.utils.events.UserChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageCt implements Observer<UserChangeEvent> {
    private Service service;
    private Utilizator user;
    private Utilizator friend;

    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    @FXML
    private TextField MessageText;
    @FXML
    private TextArea requestsdata;


    @FXML
    Label labelCurrentUser;


    private final ScheduledExecutorService thread = Executors.newScheduledThreadPool(1);

    public void setProps(Service service, Utilizator user, Utilizator friend) {
        this.service = service;
        this.user = user;
        this.friend = friend;
        labelCurrentUser.setText(user.getFirstName() + " " + user.getLastName());
        service.addObserver(this);
        initModel();

    }

    private void initModel() {


    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        thread.shutdown();
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

    @FXML
    void handleSendMessage(ActionEvent event) {

        if (MessageText.getText() == null) {
            MessageAlert.showErrorMessage(null, "Trebuie sa introduci un mesaj!");
        } else {
            try {
                service.add_message(user.getId(), friend.getId(), MessageText.getText(),0L);
                //MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Succes!", "Prieten sters!");
                MessageText.clear();
            } catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }


    }
}
