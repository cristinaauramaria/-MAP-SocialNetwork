package socialnetwork.repository.database;

import socialnetwork.domain.Message;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class MessageDbRepository  {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Message> validator;

    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    public Iterable<Message> find_message(Long from1,Long to1) {
        Set<Message> users = new HashSet<>();
        //String sql ="SELECT * FROM message WHERE (from1 = " + from1 + "AND to1 = " + to1 +") OR ( from1= "+ to1 +"AND to1= " +from1+");";
        String sql ="SELECT * FROM message WHERE from1=" + from1 +";";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next()){
                Long from = resultSet.getLong("from1");
                Long to = resultSet.getLong("to1");
                String text = resultSet.getString("message");
                LocalDateTime data_time = resultSet.getTimestamp("data").toLocalDateTime();
                Long reply = resultSet.getLong("reply_message");

                Message msg = new Message(from,to,text,data_time,reply);
                users.add(msg);
            }
            return users;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }



    public Message save_message(Message entity) {
        validator.validate(entity);
        Message msg = entity;
        String sql ="INSERT INTO message(from1,to1,message,data,reply_message) VALUES (" + msg.getFrom()+ "," + msg.getTo()+ ",'"+msg.getMessage()+"','" + msg.getData()+ "',"+msg.getReply_message()+");";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
            entity = null;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

}
