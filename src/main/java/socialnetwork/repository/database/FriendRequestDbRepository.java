package socialnetwork.repository.database;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDbRepository  {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<FriendRequest> validator;

    public FriendRequestDbRepository(String url, String username, String password, Validator<FriendRequest> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    public FriendRequest findOne(Tuple<Long, Long> longLongTuple) {
        FriendRequest prietenie = null;
        String sql ="SELECT * FROM friendrequest WHERE from1 = " + longLongTuple.getLeft() + " AND to1=" + longLongTuple.getRight() + ";";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery())
        {
            if(resultSet.next()) {
                Long from = resultSet.getLong("from1");
                Long to = resultSet.getLong("to1");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String status = resultSet.getString("status");
                LocalDateTime data_time = resultSet.getTimestamp("data_time").toLocalDateTime();


                Tuple<Long,Long> tru = new Tuple<>(from,to);
                prietenie = new FriendRequest(firstName,lastName,status,data_time);
                prietenie.setId(tru);;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return prietenie;
    }

    public void save(FriendRequest user) {
        validator.validate(user);
        String sql ="INSERT INTO friendrequest(from1,to1,firstname,lastname,status,data_time) VALUES (" + user.getId().getLeft() + "," + user.getId().getRight() + ",'"+user.getFirstName()+"','"+user.getLastName()+"','" + user.getStatus() + "','" + user.getData_time() + "');";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void delete(Tuple<Long, Long> longLongTuple) {
        String sql ="DELETE FROM friendrequest WHERE from1 = " + longLongTuple.getLeft() + " AND to1=" + longLongTuple.getRight() + ";";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<FriendRequest> find_all_friendrequest(Long id)
    {
        List<FriendRequest> list = new ArrayList<>();
        FriendRequest prietenie = null;
        String sql ="SELECT * FROM friendrequest WHERE to1 = " + id +";";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next()) {
                Long from = resultSet.getLong("from1");
                Long to = resultSet.getLong("to1");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String status = resultSet.getString("status");
                LocalDateTime data_time = resultSet.getTimestamp("data_time").toLocalDateTime();


                Tuple<Long,Long> tru = new Tuple<>(from,to);
                prietenie = new FriendRequest(firstName,lastName,status,data_time);
                prietenie.setId(tru);
                list.add(prietenie);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
}
