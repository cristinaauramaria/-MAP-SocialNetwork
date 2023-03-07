package socialnetwork.domain;

import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;

    public Prietenie(LocalDateTime data){
        date = data;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }
    public Long getUser1(){return this.getId().getLeft();}
    public Long getUser2(){return this.getId().getRight();}
}
