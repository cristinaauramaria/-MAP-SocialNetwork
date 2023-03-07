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
import javafx.scene.text.Text;
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

public class AddNewFriend implements Observer<UserChangeEvent> {
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
    Label labelCurrentUser;
    @FXML
    private Button SendFRQ;

    @FXML
    private Button DeleteFRQ;

    private final ScheduledExecutorService thread = Executors.newScheduledThreadPool(1);

    public void setProps(Service service, Utilizator user) {
        this.service = service;
        this.user = user;
        labelCurrentUser.setText(user.getFirstName() + " " + user.getLastName());
        service.addObserver(this);
        initModel();

    }
    private void initModel() {
        //List<Utilizator> friends=service.getFriends(user.getId());

        Iterable<Utilizator> students = service.getAll();
        List<Utilizator> friends = StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList());

        model.setAll(friends);

    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {initModel();
    }

    @FXML
    public void initialize() {
        tableColFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        //tableColDate.setCellValueFactory(new PropertyValueFactory<>("password"));
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
    public void handleAddFriend(ActionEvent event){
        Utilizator userselect = requestsTable.getSelectionModel().getSelectedItem();
        if (userselect == null) {
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi un prieten!");
        } else if (userselect.getId().equals(user.getId())) {
            MessageAlert.showErrorMessage(null, "Utilizatorul selectat esti tu!");
        } else {
            try {
                service.add_service_friendrequest(user.getId(),userselect.getId());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Succes!", "Cerere de prietenie trimisa!");
                //SendFRQ.setVisible(false);
                //DeleteFRQ.setVisible(true);
            } catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        
    }
    @FXML
    public void handleDeleteFrRequest(ActionEvent event){
        Utilizator userselect = requestsTable.getSelectionModel().getSelectedItem();
        if (userselect == null) {
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi un utilizator!");
        } else {
            try {
                service.retragere_cerere(user.getId(), userselect.getId());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Succes!", "Cererea a fost stearsa!");
            } catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
    }
}
