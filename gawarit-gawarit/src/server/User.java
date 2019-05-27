package server;


import java.io.*;
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
            InputStream inputStream = getClass().getResourceAsStream("/" + login + ".txt");
            streamReader = new InputStreamReader(inputStream); // Otwieramy readera
            bufferedReader = new BufferedReader(streamReader); // Buforujemy readera
            String truePass = bufferedReader.readLine();//wczytanie linii tekstu do bufora
            if(!pass.equals(truePass))
                return false;

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

    void register(String login, String pass) {
        
    }


    void PrintInfo() { //do debugowania
        System.out.println("Username: " + username );
        System.out.println( "Password: " + password );
        System.out.println("Towarzysze " + username + ":" );
        for (String friend : friends)
            System.out.println(friend);
    }

    void RewriteUser() {

    }

}
