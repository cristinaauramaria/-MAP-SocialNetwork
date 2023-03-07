package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import socialnetwork.controler.LogIn;
import socialnetwork.domain.Message;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository0;
import socialnetwork.repository.database.FriendRequestDbRepository;
import socialnetwork.repository.database.FriendshipDbRepository;
import socialnetwork.repository.database.MessageDbRepository;
import socialnetwork.repository.database.UtilizatorDbRepository;
import socialnetwork.service.Service;
import socialnetwork.service.UtilizatorService;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Validator validator = new UtilizatorValidator();
        Validator validator_friendship = new FriendsValidator();
        Validator validator_frrq=new FriendRequestValidator();
        Validator validator_msg=new MessageValidator();

        Repository0<Long, Utilizator> userFileRepository= new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","cristina2003", validator);
        Repository0<Tuple<Long,Long>, Prietenie> friendshipDbRepository = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","cristina2003", validator_friendship);
        FriendRequestDbRepository repo_frrq= new FriendRequestDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","cristina2003", validator_frrq);
        MessageDbRepository repo_msg=new MessageDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","cristina2003", validator_msg);

        Service service = new Service(userFileRepository, friendshipDbRepository,repo_frrq,repo_msg);
        //service.add_message(12L,13L,"buna",0L);
        repo_msg.find_message(12L, 13L);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);

        Image icon = new Image("C:\\Users\\Cristina\\Desktop\\Anul II\\Sem I\\Map\\MAP-proiect\\src\\main\\resources\\imgs\\wp.jpg");
        stage.getIcons().add(icon);

        LogIn logInController = fxmlLoader.getController();
        logInController.setService(service);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}