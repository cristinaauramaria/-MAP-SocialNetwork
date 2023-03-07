package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class FriendsFile extends AbstractFileRepository0<Tuple<Long,Long>, Prietenie> {

    public FriendsFile(String fileName, Validator<Prietenie> validator) {
        super(fileName, validator);
    }


    @Override
    public Prietenie extractEntity(List<String> attributes) {
        Prietenie elem = new Prietenie(LocalDateTime.parse(attributes.get(2)));
        Tuple<Long,Long> elem2 = new Tuple<Long, Long>(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1)));
        elem.setId(elem2);
        return elem;

    }

    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getId().getLeft()+";"+entity.getId().getRight()+";"+entity.getDate();
    }


}
