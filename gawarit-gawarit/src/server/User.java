package server;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class User {

    public String username;
    private String password;
    public List<String> friends = new ArrayList<String>();

    public User() {
        //tu chyba jednak nic nie będzie... :(
        }

    boolean login(String login, String pass) {

        username=login;
        InputStreamReader streamReader = null;
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = new FileInputStream(new File(login + ".txt"));
            streamReader = new InputStreamReader(inputStream); // Otwieramy readera
            bufferedReader = new BufferedReader(streamReader); // Buforujemy readera
            String truePass = bufferedReader.readLine();
            if(!pass.equals(truePass))
                return false;

            password = pass;
            String friend;
            while(true){
                friend =  bufferedReader.readLine();
                if(friend!=null) friends.add(friend);
                else break;
            }
        }catch (Exception e) {
            System.err.println("Blad przy otwarciu pliku");
            return false;
        }
        try {
            streamReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Blad przy zamykaniu pliku.");
            return false;
        }
        return true;
    }

    boolean register(String login, String pass) {
        try {
            username = login;
            password = pass;
            File file = new File(login + ".txt");

            if(file.exists())
                return false;

            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(pass + "\n");
            writer.close();
            addFriend(login);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    void printInfo() { //do debugowania
        System.out.println("Username: " + username );
        System.out.println( "Password: " + password );
        System.out.println("Towarzysze " + username + ":" );
        for (String friend : friends)
            System.out.println(friend);
    }

    void rewriteUser() {
        try {
            File file = new File(username + ".txt");

            if(!file.exists()) {
                System.out.println("Problem: pliku nie odnaleziono");
                return;
            }
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(password + "\r\n");
            for (String friend : friends)
                writer.write(friend + "\r\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void addFriend(String friend) {
        friends.add(friend);
        rewriteUser();
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
        rewriteUser();
    }

}
