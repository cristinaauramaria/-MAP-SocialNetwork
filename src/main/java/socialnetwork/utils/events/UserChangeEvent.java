package socialnetwork.utils.events;



import socialnetwork.domain.Utilizator;

public class UserChangeEvent implements Event {
    private ChangeEventType type;
    //private Utilizator data, oldData;

    public UserChangeEvent(ChangeEventType type) {
        this.type = type;
        //this.data = data;
    }


    public ChangeEventType getType() {
        return type;
    }


}