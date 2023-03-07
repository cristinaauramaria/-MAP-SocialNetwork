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
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.Service;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.UserChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomePage implements Observer<UserChangeEvent> {
    private Service service;
    private Utilizator user;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();


    @FXML
    private TableView<Utilizator> requestsTable;
    @FXML
    private TableColumn<Utilizator, String> tableColFirstName;
    @FXML
    private TableColumn<Utilizator, String> tableColLastName;

    @FXML
    private TableColumn<Utilizator, String> tableColDate;
    @FXML
    Label labelCurrentUser;

    private final ScheduledExecutorService thread = Executors.newScheduledThreadPool(1);

    public void setProps(Service service, Utilizator user) {
        this.service = service;
        this.user = user;
        service.addObserver(this);
        initModel();

    }

    private void initModel() {
        //List<Utilizator> friends=service.getFriends(user.getId());

        Iterable<Utilizator> students = service.getFriends(user.getId());
        List<Utilizator> friends = StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList());

        model.setAll(friends);

    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {
        //model.removeAll(model);
        initModel();
        //if (userChangeEvent.getType() == ChangeEventType.DELETE) {
        //  model.removeAll(model);
        // initModel();
        //}
    }

    @FXML
    public void initialize() {
        tableColFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        //tableColDate.setCellValueFactory(new PropertyValueFactory<>("password"));
        requestsTable.setItems(model);
    }

    @FXML
    public void handleRemove(ActionEvent actionEvent) {
        Utilizator userselect = requestsTable.getSelectionModel().getSelectedItem();
        if (userselect == null) {
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi un prieten!");
        } else {
            try {
                service.delete_friendship(this.user.getId(), userselect.getId());
                //model.removeAll(model);
                //initModel();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Succes!", "Prieten sters!");
            } catch (ValidationException | IOException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }

    }

    @FXML
    public void handleLogOut(ActionEvent event) throws IOException {
        thread.shutdown();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/LogIn.fxml"));
        Parent createaccountParent = loader.load();

        LogIn controller = loader.getController();
        controller.setService(service);

        Scene createaccountScene = new Scene(createaccountParent, 500, 300);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(createaccountScene);
        window.show();
    }

    @FXML
    public void handleAddNewFriend(ActionEvent event) throws IOException {
        thread.shutdown();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AddNewFriend.fxml"));
        Parent createaccountParent = loader.load();

        AddNewFriend controller = loader.getController();
        controller.setProps(service, user);

        Scene createaccountScene = new Scene(createaccountParent, 500, 300);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(createaccountScene);
        window.show();

    }

    @FXML
    public void handleFrRequest(ActionEvent event) throws IOException {
        thread.shutdown();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FriendRequest.fxml"));
        Parent createaccountParent = loader.load();

        FriendRequestCt controller = loader.getController();
        controller.setProps(service, user);

        Scene createaccountScene = new Scene(createaccountParent, 500, 300);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(createaccountScene);
        window.show();

    }

    public void handleMessage(ActionEvent event) throws IOException {
        Utilizator userselect = requestsTable.getSelectionModel().getSelectedItem();
        if (userselect == null) {
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi un prieten!");
        } else {
            thread.shutdown();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Message.fxml"));
            Parent createaccountParent = loader.load();

            MessageCt controller = loader.getController();
            controller.setProps(service, user,userselect);

            Scene createaccountScene = new Scene(createaccountParent, 500, 300);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(createaccountScene);
            window.show();
        }

    }

}
