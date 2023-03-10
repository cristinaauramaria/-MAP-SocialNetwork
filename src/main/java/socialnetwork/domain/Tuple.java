package socialnetwork.domain;

import java.util.Objects;


/**
 * Define a Tuple o generic type entities
 * @param <E1> - tuple first entity type
 * @param <E2> - tuple second entity type
 */
public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     *
     * @return the left side of a tuple
     */
    public E1 getLeft() {
        return e1;
    }

    /**
     * set the left side of the tuple to the value of e1
     * @param e1
     */
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    /**
     * the right side of a tuple
     * @return
     */
    public E2 getRight() {
        return e2;
    }

    /**
     * set the right side of the tuple to the value of e1
     * @param e2
     */
    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    @Override
    public String toString() {
        return "" + e1 + "," + e2;

    }

    @Override
    public boolean equals(Object obj) {
        return this.e1.equals(((Tuple) obj).e1) && this.e2.equals(((Tuple) obj).e2) || this.e1.equals(((Tuple) obj).e2) && this.e2.equals(((Tuple) obj).e1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(e1.hashCode() + e2.hashCode());
    }
}