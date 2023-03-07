package socialnetwork.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.FriendRequest;
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

public class FriendRequestCt implements Observer<UserChangeEvent> {
    private Service service;
    private Utilizator user;

    ObservableList<FriendRequest> model = FXCollections.observableArrayList();


    @FXML
    private TableView<FriendRequest> requestsTable;
    @FXML
    private TableColumn<Utilizator, String> tableColFirstName;
    @FXML
    private TableColumn<Utilizator, String> tableColLastName;
    @FXML
    private TableColumn<Utilizator, String> tableColStatus;
    @FXML
    private TableColumn<Utilizator, String> tableColDateTime;

    @FXML
    Label labelCurrentUser;

    private final ScheduledExecutorService thread = Executors.newScheduledThreadPool(1);

    public void setProps(Service service, Utilizator user) {
        this.service = service;
        this.user = user;
        labelCurrentUser.setText(user.getFirstName() + " " + user.getLastName());
        service.addObserver(this);
        initModel();

    }
    private void initModel() {

        List<FriendRequest> friendRequests=service.get_all_friendrequest(user.getId());
        //List<FriendRequest> friends = StreamSupport.stream(friendRequests.spliterator(), false).collect(Collectors.toList());

        model.setAll(friendRequests);


    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }

    @FXML
    public void initialize() {

        tableColFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColDateTime.setCellValueFactory(new PropertyValueFactory<>("data_time"));
        requestsTable.setItems(model);

    }
    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        thread.shutdown();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/HomePage.fxml"));
        Parent createaccountParent = loader.load();

        HomePage controller = loader.getController();
        controller.setProps(service,user);

        Scene createaccountScene = new Scene(createaccountParent, 500, 300);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(createaccountScene);
        window.show();
    }

    @FXML
    public void handleAccept(ActionEvent actionEvent) {
        FriendRequest users = requestsTable.getSelectionModel().getSelectedItem();
        if (users == null) {
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi o cerere!");
        } else if (!users.getStatus().equals("PENDING")) {
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi o cerere de prietenie!");
        } else {
            try {
                service.service_accept_friendrequest(user.getId(), users.getUser1());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Succes!", "Prieten adaugat!");
            } catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }

    }
    @FXML
    public void handleDecline(ActionEvent actionEvent) {
        FriendRequest users = requestsTable.getSelectionModel().getSelectedItem();
        if (users == null) {
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi o cerere!");
        } else if (!users.getStatus().equals("PENDING")) {
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi o cerere de prietenie!");
        } else {
            try {
                service.service_delete_friendrequest(user.getId(), users.getUser1());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Succes!", "Cerere stearsa!");
            } catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
    }


}
