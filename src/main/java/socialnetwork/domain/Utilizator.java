package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long> {
    //add more fields
    private String firstName;
    private String lastName;
    private List<Long> friends;
    private String password;

    public Utilizator(String firstName, String lastName,String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        //friends = new ArrayList<Long>();
        this.password= password;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public List<Long> getFriends() {
        return friends;
    }

    public void setFriends(List<Long> list) {
        this.friends = list;
    }

    /**
     * add id in friends list
     *
     * @param id
     */
    public void add_friend(Long id) {
        friends.add(id);
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + friends +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}