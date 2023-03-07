package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long>{

    private Long from;
    private Long to;
    private String message;
    private LocalDateTime data;
    private Long reply_message;


    public Message(Long from, Long to, String message, LocalDateTime data, Long reply_message) {

        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.reply_message = reply_message;
    }


    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Long getReply_message() {
        return reply_message;
    }

    public void setReply_message(Long reply_message) {
        this.reply_message = reply_message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message1 = (Message) o;
        return getFrom().equals(message1.getFrom()) &&
                getTo().equals(message1.getTo()) &&
                getMessage().equals(message1.getMessage()) &&
                getData().equals(message1.getData()) &&
                getReply_message().equals(message1.getReply_message());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo(), getMessage(), getData(), getReply_message());
    }

    @Override
    public String toString() {
        return "Message{" +
                ", message='" + message + '\n' +
                ", data=" + data + '\n' +
                ", reply_message=" + reply_message + '\n' +
                '}';
    }
}
