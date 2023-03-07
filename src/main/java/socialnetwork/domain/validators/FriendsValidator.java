package socialnetwork.domain.validators;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;

public class FriendsValidator implements Validator<Prietenie>{
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        Tuple<Long,Long> elem = entity.getId();
        if(elem.getRight() == elem.getLeft())
            throw new ValidationException("id1 must be different from id2");
    }
}
