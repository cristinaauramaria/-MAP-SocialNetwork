package socialnetwork.domain;

import java.time.LocalDateTime;

public class FriendRequest extends Entity<Tuple<Long,Long>> {
    private String status;
    LocalDateTime data_time;

    private String firstName;
    private String lastName;

    public FriendRequest(String status) {
        this.status = status;
    }

    public FriendRequest(String firstName,String lastName,String status, LocalDateTime data_time) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.status = status;
        this.data_time = data_time;
    }

    public LocalDateTime getData_time() {
        return data_time;
    }

    public void setData_time(LocalDateTime data_time) {
        this.data_time = data_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUser1(){return this.getId().getLeft();}
    public Long getUser2(){return this.getId().getRight();}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
