package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository0<ID, E extends Entity<ID>> implements Repository0<ID, E> {

    private Validator<E> validator;
    Map<ID, E> entities;

    public InMemoryRepository0(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        E user = entities.get(id);
        if( user == null)
            throw new ValidationException("id " + id + " nu exista");
        return user;
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            throw new ValidationException("Id existent!\n");
        } else entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        E user = findOne(id);
        entities.remove(id, user);
        return user;
    }

    @Override
    public E update(E entity) {

        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(), entity);

        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;

    }


    @Override
    public void add_friends(Long id1, Long id2) {
        Utilizator user1 = (Utilizator) entities.get(id1);
        Utilizator user2 = (Utilizator) entities.get(id2);
        if (user1.getFriends().contains(id2)) {
            throw new IllegalArgumentException("friendship exist already!");
        }
        user1.add_friend(id2);
        user2.add_friend(id1);
    }

    @Override
    public List<Long> all_friends(Long id) {
        Utilizator user = (Utilizator) entities.get(id);
        return user.getFriends();

    }

    @Override
    public void remove_friend(Long id, Long id2) {
        Utilizator user = (Utilizator) entities.get(id);
        List<Long> elems = new ArrayList<Long>();
        for (Long ele : user.getFriends()) {
            if (ele != id2) {
                elems.add(ele);
            }
        }
        user.setFriends(elems);
    }

}
