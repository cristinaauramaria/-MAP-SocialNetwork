package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository0;
import socialnetwork.repository.database.FriendRequestDbRepository;
import socialnetwork.repository.database.MessageDbRepository;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.UserChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

public class Service implements Observable<UserChangeEvent> {
    private Repository0<Long, Utilizator> repository0;

    private Repository0<Tuple<Long, Long>, Prietenie> repo_friends;

    private FriendRequestDbRepository repo_frrq;
    private MessageDbRepository repo_msg;
    private List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<UserChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(UserChangeEvent event) {
        observers.forEach(x -> x.update(event));
    }


    public Service(Repository0<Long, Utilizator> repository0, Repository0<Tuple<Long, Long>, Prietenie> repo_friends,FriendRequestDbRepository repo_frrq,MessageDbRepository repo_msg) {
        this.repository0 = repository0;
        this.repo_friends = repo_friends;
        this.repo_frrq=repo_frrq;
        this.repo_msg=repo_msg;
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

    public List<Utilizator> getFriends(Long id) {
        List<Long> friends = repo_friends.all_friends(id);
        List<Utilizator> frienda = new ArrayList<>();
        for (Long idn : friends) {

            Utilizator user2 = repository0.findOne(idn);
            frienda.add(user2);

        }
        return frienda;
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

        for (Prietenie pri : repo_friends.findAll()) {
            if (pri.getUser1().equals(id)) {
                Long id2 = pri.getUser2();
                delete_friendship(id, id2);
            }
            if (pri.getUser2().equals(id)) {
                Long id2 = pri.getUser1();
                delete_friendship(id2, id);
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
        Tuple<Long, Long> tru2 = new Tuple<Long, Long>(y, x);
        Prietenie friends2 = repo_friends.findOne(tru2);
        //Utilizator user=repository0.findOne(x);
        if (friends == null && friends2 == null)
            throw new ValidationException("prietenia nu exista!");

        repo_friends.delete(tru);
        notifyObservers(new UserChangeEvent(ChangeEventType.DELETE));
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
                .filter(y -> {
                    // System.out.println(y.toString());
                    return y.getId().getRight() == x || y.getId().getLeft() == x;
                })
                .forEach(y -> {
                    Utilizator u;
                    if (y.getId().getLeft() == x) {
                        u = repository0.findOne(y.getId().getRight());
                    } else
                        u = repository0.findOne(y.getId().getLeft());
                    String string = new String(u.getFirstName() + " " + u.getLastName() + " " + y.getDate());
                    list.add(string);
                });
        return list;

    }


    public Utilizator getUserByName(String FirstName, String LastName) {
        Iterable<Utilizator> it = repository0.findAll();
        for (Utilizator u : it)
            if (u.getFirstName().equals(FirstName) && u.getLastName().equals(LastName))
                return u;
        return null;
    }

    public void add_service_friendrequest(Long from,Long to) {
        //verificam daca exista useri
        repository0.findOne(from);
        repository0.findOne(to);

        //verific daca exista prietenia
        Tuple<Long,Long> tru,tru1,tru2;
        tru = new Tuple<>(to, from);
        tru1 = new Tuple<>(from,to);

        if(repo_friends.findOne(tru)!=null || repo_friends.findOne(tru1)!=null) {
            throw new ValidationException("Esti deja prieten cu utilizatorul ales!");
        }

        tru = new Tuple<>(from,to);
        //verificam daca exista cererea
        tru2 = new Tuple<>(to,from);
        FriendRequest pri = repo_frrq.findOne(tru);
        FriendRequest pri2 = repo_frrq.findOne(tru2);
        if(pri==null && pri2==null)
        {
            Utilizator user2=repository0.findOne(from);
            FriendRequest fr = new FriendRequest(user2.getFirstName(),user2.getFirstName(),"PENDING",LocalDateTime.now());
            fr.setId(tru);

            repo_frrq.save(fr);
            notifyObservers(new UserChangeEvent(ChangeEventType.FRIEND_RQ));
        }
        else
        if(pri2!=null && !pri2.getStatus().equals("REJECTED"))
        {
            throw new ValidationException("Exista o cerere trimisa pentru tine de la utilizatorul selectat");
        }
        else
        if(pri.getStatus().equals("PENDING") || pri.getStatus().equals("ACCEPTED")) {
            throw new ValidationException("Exista o cerere deja trimisa!");
        }


    }

    public List<FriendRequest> get_all_friendrequest(Long id) {return repo_frrq.find_all_friendrequest(id);}

    public void service_accept_friendrequest(Long to, Long from) {
        //verificam daca from si to exista
        repository0.findOne(from);
        repository0.findOne(to);

        //verificam daca cererea exista
        Tuple<Long,Long> tru = new Tuple<>(from,to);
        FriendRequest prietenie = repo_frrq.findOne(tru);
        if(prietenie!=null && prietenie.getStatus().equals("PENDING"))
        {
            Tuple<Long,Long> tru2 = null;
            if(from>to) {
                tru2 = new Tuple<>(to,from);
            }
            else
            {
                tru2 = new Tuple<>(from,to);
            }
            Prietenie pri = new Prietenie(LocalDateTime.now());
            pri.setId(tru2);
            repo_friends.save(pri);
            repo_frrq.delete(tru);

            //prietenie.setStatus("APPROVED");
            //prietenie.setData_time(LocalDateTime.now());
            //repo_frrq.save(prietenie);

            Tuple<Long,Long> tru1 = new Tuple<>(to,from);
            FriendRequest prietenie1 = repo_frrq.findOne(tru1);
            if(prietenie1!=null)
            {
                repo_frrq.delete(tru1);
            }

            notifyObservers(new UserChangeEvent(ChangeEventType.ADD_FRIEND));

        }
        else
        {
            throw new ValidationException("cererea de pretenie nu exista!");
        }


    }

    public void service_delete_friendrequest(Long to,Long from) {
        repository0.findOne(from);
        repository0.findOne(to);

        Tuple<Long,Long> tru = new Tuple<>(from,to);
        FriendRequest prietenie = repo_frrq.findOne(tru);
        if(prietenie!= null && prietenie.getStatus().equals("PENDING"))
        {
            repo_frrq.delete(tru);
            notifyObservers(new UserChangeEvent(ChangeEventType.DELETE_FRIEND));
        }
        else
        {
            throw new ValidationException("Cererea nu exista!");
        }
    }


    public void retragere_cerere (Long to,Long from) {
        //verifica daca exista cererea si daca este pennding
        Tuple<Long,Long> tru = new Tuple<>(to,from);
        FriendRequest fr = repo_frrq.findOne(tru);
        if(fr == null) {
            throw new ValidationException("Nu exista friend request pentru utilizatorul selectat!");
        }
        if(fr!=null && fr.getStatus().equals("PENDING")) {
            repo_frrq.delete(tru);
        }
        else
        {
            throw new ValidationException("Cererea de prietenie nu mai poate sa fie retrasa!");
        }

        //daca da atunci o sterg din bd
        //daca nu arunc eroare
    }
    public void add_message (Long from,Long to,String message, Long reply) {
        Message msg = new Message(from,to,message,LocalDateTime.now(), reply);
        repo_msg.save_message(msg);
        notifyObservers(new UserChangeEvent(ChangeEventType.SAVEMSG));
    }


}