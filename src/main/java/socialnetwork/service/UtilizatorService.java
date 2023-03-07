package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository0;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class UtilizatorService {
    private Repository0<Long, Utilizator> repository0;

    private Repository0<Tuple<Long, Long>, Prietenie> repo_friends;

    public UtilizatorService(Repository0<Long, Utilizator> repository0, Repository0<Tuple<Long, Long>, Prietenie> repo_friends) {
        this.repository0 = repository0;
        this.repo_friends = repo_friends;
    }


    /**
     * add a user
     *
     * @param utilizator
     * @return utilizator
     */

    public Utilizator addUtilizator(Utilizator utilizator) {
        Utilizator utilizator1 = repository0.save(utilizator);
        return utilizator1;
    }

    /**
     * @return all users
     */
    public Iterable<Utilizator> getAll() {

        return repository0.findAll();
    }

    public Iterable<Prietenie> getAll2() {

        return repo_friends.findAll();
    }


    /**
     * remove a user
     *
     * @param id-user id
     * @return user-ul sters
     * @throws IOException
     */

    public Utilizator deleteUtilizator(Long id) throws IOException {
        //repository0.findOne(id);
        //List<Long> friends = (List<Long>) repository0.all_friends(id);
        repository0.delete(id);

        for (Prietenie pri: repo_friends.findAll()) {
            if(pri.getUser1().equals(id)){
                Long id2=pri.getUser2();
                delete_friendship(id,id2);
            }
            if(pri.getUser2().equals(id)){
                Long id2=pri.getUser1();
                delete_friendship(id2,id);
            }
        }
        return null;
    }

    /**
     * add a friendship between the user with id=x and the user with id=y
     *
     * @param x-id from user1
     * @param y-id from user2
     */
    public Prietenie add_friend(Long x, Long y) {
        repository0.findOne(x);
        repository0.findOne(y);
        Tuple<Long, Long> tru = new Tuple<>(x, y);
        Prietenie prietenie = repo_friends.findOne(tru);
        if (prietenie != null)
            throw new ValidationException("prietenie existenta!");
        Prietenie pri = new Prietenie(LocalDateTime.now());
        pri.setId(tru);
        return repo_friends.save(pri);
    }

    /**
     * remove friendship between the user with id=x and the user with id=y
     *
     * @param x-id from user1
     * @param y-od from user2
     * @return Prietenie
     * @throws IOException
     */
    public Prietenie delete_friendship(Long x, Long y) throws IOException {
        Tuple<Long, Long> tru = new Tuple<Long, Long>(x, y);
        Prietenie friends = repo_friends.findOne(tru);
        if (friends == null)
            throw new ValidationException("prietenia nu exista!");
        repo_friends.delete(tru);
        return null;
    }



    /**
     * DFS(X)
     *
     * @param x-Utilizator
     * @param vizitat
     * @param distanta
     * @param time
     */
    public void dfs(Utilizator x, Map<Long, Long> vizitat, Map<Long, Long> distanta, Long time) {
        time++;
        distanta.replace(x.getId(), time);
        vizitat.replace(x.getId(), (long) 1);
        for (var ele : x.getFriends()) {
            if (vizitat.get(ele) == 0) {
                Utilizator user = repository0.findOne(ele);
                dfs(user, vizitat, distanta, time);
            }
        }
    }

    /**
     * @return numarul de elemente conexe
     */
    public Integer find_number_of_conex_elem() {
        Map<Long, Long> distanta = new HashMap<Long, Long>();
        Map<Long, Long> vizitat = new HashMap<Long, Long>();
        for (var ele : repository0.findAll()) {
            distanta.put(ele.getId(), (long) 0);
            vizitat.put(ele.getId(), (long) 0);
        }
        Integer conex = 0;
        for (var ele : repository0.findAll()) {
            if (vizitat.get(ele.getId()) == 0) {
                dfs(ele, vizitat, distanta, (long) 0);
                conex++;
            }
        }
        //System.out.println(distanta);
        return conex;


    }

    /**
     * @return cel mai lung drum
     */
    public Integer cel_mai_lung_drum() {
        Map<Long, Long> distanta = new HashMap<Long, Long>();
        Map<Long, Long> vizitat = new HashMap<Long, Long>();
        for (var ele : repository0.findAll()) {
            distanta.put(ele.getId(), (long) 0);
            vizitat.put(ele.getId(), (long) 0);
        }
        Integer max = 0;
        for (var ele : repository0.findAll()) {
            dfs(ele, vizitat, distanta, (long) -1);
            for (var user : repository0.findAll()) {
                Long id = user.getId();
                if (distanta.get(id) > max) {
                    max = Math.toIntExact(distanta.get(id));
                }
            }
            for (var elems : repository0.findAll()) {
                vizitat.replace(elems.getId(), (long) 0);
                distanta.replace(elems.getId(), (long) 0);
            }
        }

        return max;


    }

    public Iterable<String> afiseaza_prieteni(Long x) {
        Utilizator user = repository0.findOne(x);
        ArrayList<String> list = new ArrayList<>();
        StreamSupport.stream(repo_friends.findAll().spliterator(), false)
                .filter(y->{
                    // System.out.println(y.toString());
                    return y.getId().getRight()==x || y.getId().getLeft()==x;
                })
                .forEach(y->{
                    Utilizator u;
                    if(y.getId().getLeft() == x){
                        u = repository0.findOne(y.getId().getRight());
                    }
                    else
                        u = repository0.findOne(y.getId().getLeft());
                    String string = new String(u.getFirstName() + " " + u.getLastName() + " " + y.getDate());
                    list.add(string);
                });
        return list;

    }

    public Utilizator getUserByName(String FirstName,String LastName) {
        Iterable<Utilizator> it = repository0.findAll();
        for(Utilizator u : it)
            if(u.getFirstName().equals(FirstName) && u.getLastName().equals(LastName))
                return u;
        return null;
    }


}
