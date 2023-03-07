package socialnetwork.ui;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.UtilizatorService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Ui {
    private UtilizatorService service;


    public Ui(UtilizatorService service) {
        this.service = service;
    }

    /**
     * prints all entities
     */
    public void print_all() {
        service.getAll().forEach(System.out::println);
    }

    /**
     * add a user
     *
     * @throws IOException

    private void add_utilizator() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String element = reader.readLine();
            String[] sir = element.split(" ");
            if (sir.length < 3)
                throw new IllegalArgumentException("The number of arguments is invalid!");
            Utilizator utilizator = new Utilizator(sir[1], sir[2]);
            utilizator.setId(Long.parseLong(sir[0]));
            service.addUtilizator(utilizator);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException et) {
            System.out.println(et.getMessage());
        }

    }*/

    private void add_utilizator() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String elem = reader.readLine();
            String[] sir=elem.split(" ");
            if(sir.length<2)
                throw new IllegalArgumentException("The number of arguments is invalid!");

            Utilizator user = new Utilizator(sir[0],sir[1],sir[2]);
            //user.setId(Long.parseLong(sir[0]));
            service.addUtilizator(user);
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        catch (IllegalArgumentException et) {
            System.out.println(et.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * remove a user
     *
     * @throws IOException
     */

    private void delete_utilizator() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.parseLong(reader.readLine());
            service.deleteUtilizator(id);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException et) {
            System.out.println(et.getMessage());
        }

    }

    /**
     * add a friendship
     * @throws IOException
     */
    private void add_friend() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String elem = reader.readLine();
        String[] elem2=elem.split(" ");

        try {
            if(elem2.length!=2)
                throw new IllegalArgumentException("The number of arguments is invalid!");
            Long x = Long.parseLong(elem2[0]);
            Long y = Long.parseLong(elem2[1]);
            service.add_friend(x,y);

        }catch(IllegalArgumentException  e) {
            System.out.println(e.getMessage());
        } catch (ValidationException et) {
            System.out.println(et.getMessage());
        }

    }

    /**
     * remove a friendship
     * @throws IOException
     */
    private void delete_friendship() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String elem = reader.readLine();
        String[] elem2=elem.split(" ");

        try {
            if ( elem2.length < 2)
                throw new IllegalArgumentException("The number of arguments is invalid!");
            Long x = Long.parseLong(elem2[0]);
            Long y = Long.parseLong(elem2[1]);
            service.delete_friendship(x,y);

        }catch(IllegalArgumentException  e) {
            System.out.println(e.getMessage());
        } catch (ValidationException et) {
            System.out.println(et.getMessage());
        }

    }

    private void cauta_prieteni() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String elem = reader.readLine();
        String[] elem2=elem.split(" ");

        try {
            if ( elem2.length!= 1)
                throw new IllegalArgumentException("The number of arguments is invalid!");
            Long x = Long.parseLong(elem2[0]);
            ArrayList<String> lista = (ArrayList<String>) service.afiseaza_prieteni(x);
            lista.forEach(System.out::println);

        }catch(IllegalArgumentException  e) {
            System.out.println(e.getMessage());
        } catch (ValidationException et) {
            System.out.println(et.getMessage());
        }
    }

    public static void menu(){
        System.out.println("                 *Menu*");
        System.out.println("          add -> to add a user");
        System.out.println("       delete -> to delete a user");
        System.out.println("   add friend -> to add a frienship between to users");
        System.out.println("delete friend -> to delete a frienship between to users");
        System.out.println("            1 -> the number of communities");
        System.out.println("            2 -> the most sociable community");
        System.out.println("         exit -> to exit the program");

    }


    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("introduce 'menu' for options\n");
        while (true) {
            System.out.print("introduce comanda: ");
            String cmd = reader.readLine();
            if(cmd.compareTo("exit")==0){
                break;
            }
            switch (cmd) {
                case "add":
                    System.out.print("introduce nume prenume parola: ");
                    add_utilizator();
                    break;
                case "all":
                    print_all();
                    break;
                case "delete":
                    System.out.print("introduce id user: ");
                    delete_utilizator();
                    break;
                case "add friend":
                    System.out.print("introduce id1 id2: ");
                    add_friend();
                    break;
                case "delete friend":
                    System.out.print("introduce id1 id2: ");
                    delete_friendship();
                    break;
                case "1":
                    System.out.print("nr de elem conexe ");
                    System.out.println(service.find_number_of_conex_elem());
                    break;
                case "2":
                    System.out.print("drum: ");
                    System.out.println(service.cel_mai_lung_drum());
                    break;
                case "cauta prieteni":
                    System.out.print("Introduce(id): ");
                    cauta_prieteni();
                    break;
                case "menu":
                    menu();
                    break;
                default:
                    System.out.println("comanda invalida!");
                    break;
            }
        }
    }

}
