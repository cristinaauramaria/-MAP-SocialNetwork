package socialnetwork;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.FriendsValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository0;
import socialnetwork.repository.database.FriendshipDbRepository;
import socialnetwork.repository.database.UtilizatorDbRepository;
import socialnetwork.repository.file.FriendsFile;
import socialnetwork.service.UtilizatorService;
import socialnetwork.ui.Ui;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Validator validator = new UtilizatorValidator();
        Validator validator_friendship = new FriendsValidator();
        //String fileName= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        //String fileName2=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.prietenie");
        //String fileName = "data/users.csv";
        //String fileName2 = "data/prietenie.csv";
        //Repository0<Long, Utilizator> userFileRepository = new UtilizatorFile0(fileName, new UtilizatorValidator());
        //Repository0<Tuple<Long, Long>, Prietenie> friendsFileRepository = new FriendsFile(fileName2, new FriendsValidator());
        //Repository userFileRepository=new InMemoryRepository(validator);
        // Repository friendsFileRepository=new InMemoryRepository(validator1);
        Repository0<Long,Utilizator> userFileRepository= new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","cristina2003", validator);
        Repository0<Tuple<Long,Long>,Prietenie> friendshipDbRepository = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","cristina2003", validator_friendship);
        UtilizatorService service = new UtilizatorService(userFileRepository, friendshipDbRepository);
        Ui ui = new Ui(service);
        ui.run();

        //userFileRepository.findAll().forEach(System.out::println);
    }
}


