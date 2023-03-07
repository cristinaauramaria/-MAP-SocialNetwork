package socialnetwork.repository.database;


import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository0;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UtilizatorDbRepository implements Repository0<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;

    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Utilizator findOne(Long id) {
        Utilizator user = null;
        String sql = "SELECT * FROM users WHERE id = " + id;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Long id3 = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");

                user = new Utilizator(firstName, lastName, password);
                user.setId((Long) id);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (user == null)
            throw new ValidationException("id inexistent");
        return user;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/academic", "postgres", "cristina2003");
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                Utilizator utilizator = new Utilizator(firstName, lastName, password);
                utilizator.setId(id);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Utilizator save(Utilizator entity) {

        String sql = "insert into users (first_name, last_name,password ) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utilizator delete(Long id) {
        Utilizator user = findOne(id);
        String sql = "DELETE FROM users WHERE id=" + id;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public Utilizator update(Utilizator entity) {
        return null;
    }

    @Override
    public void add_friends(Long id, Long id2) {
    }

    @Override
    public List<Long> all_friends(Long id) {
        return null;
    }

    @Override
    public void remove_friend(Long id, Long id2) {

    }
}
