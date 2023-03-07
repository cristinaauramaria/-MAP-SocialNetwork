package socialnetwork.repository.database;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.FriendsValidator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipDbRepository implements Repository0<Tuple<Long, Long>, Prietenie> {
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;

    public FriendshipDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Prietenie findOne(Tuple<Long, Long> longLongTuple) {
        Prietenie prietenie = null;
        String sql ="SELECT * FROM friendships WHERE id1 = " + longLongTuple.getLeft() + " AND id2=" + longLongTuple.getRight() + ";";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery())
        {
            if(resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDateTime data_time = resultSet.getTimestamp("data_time").toLocalDateTime();

                Tuple<Long,Long> tru = new Tuple<>(id1,id2);
                prietenie = new Prietenie(data_time);
                prietenie.setId(tru);;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return prietenie;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships ");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDateTime data_time = resultSet.getTimestamp("data_time").toLocalDateTime();

                Tuple<Long,Long> tru = new Tuple<>(id1,id2);
                Prietenie prietenie = new Prietenie(data_time);
                prietenie.setId(tru);
                users.add(prietenie);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Prietenie save(Prietenie entity) {
        validator.validate(entity);
        Prietenie user = entity;
        String sql ="INSERT INTO friendships(id1,id2,data_time) VALUES (" + user.getId().getLeft() + "," + user.getId().getRight() + ",'" + user.getDate() + "');";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
            entity = null;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    @Override
    public Prietenie delete(Tuple<Long, Long> longLongTuple) {
        String sql ="DELETE FROM friendships WHERE id1 = " + longLongTuple.getLeft() + " AND id2=" + longLongTuple.getRight() + ";";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String sql1 ="DELETE FROM friendships WHERE id1 = " + longLongTuple.getRight() + " AND id2=" + longLongTuple.getLeft() + ";";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql1)) {
            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Prietenie update(Prietenie entity) {
        return null;
    }

    @Override
    public void add_friends(Long id, Long id2) {

    }

    @Override
    public void remove_friend(Long id, Long id2) {

    }
    @Override
    public List<Long> all_friends(Long id) {
        List<Long> list = new ArrayList<>();
        String sql ="SELECT id1,id2 FROM friendships WHERE id1 = " + id + " OR id2=" + id + ";";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");

                if(id1==id)
                    list.add(id2);
                else
                    list.add(id1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

}
