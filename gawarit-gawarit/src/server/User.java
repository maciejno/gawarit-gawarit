package server;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class User {

    private String username;
    private String password;
    private List<String> friends = new ArrayList<String>();

    public User(String login) {
        InputStreamReader streamReader = null;
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + login + ".txt");
            streamReader = new InputStreamReader(inputStream); // Otwieramy readera
            bufferedReader = new BufferedReader(streamReader); // Buforujemy readera
            password = bufferedReader.readLine();//wczytanie linii tekstu do bufora
            username=login;
            String friend;
            while(true){
               friend =  bufferedReader.readLine();
                if(friend!=null) friends.add(friend);
                else break;
            }

        }catch (Exception e) {
            System.err.println("Blad przy otwarciu pliku");
        }
        try {
            streamReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("BLAD PRZY ZAMYKANIU PLIKU!");
            System.exit(3);
        }
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
