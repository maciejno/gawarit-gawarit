package server;


import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class User {

    private String Username;
    private String Password;
    private List<String> Friends = new ArrayList<String>();

    public User(String login) {
        InputStreamReader streamReader = null;
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + login + ".txt");
            streamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(streamReader);
            String line;
            line = bufferedReader.readLine();
            Username = login;
            Password = line;

            while (true) {
                line = bufferedReader.readLine();
                if (line == null) break;
                else Friends.add(line);
            }
        } catch (IOException e) {
            System.err.println("Blad przy wczytaniu pliku");
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
        System.out.println("Username: " + Username );
        System.out.println( "Password:" + Password );
        System.out.println("Towarzysze " + Username + ":" );
        for (String Friend : Friends)
            System.out.println(Friend);
    }

    void RewriteUser() {

    }

}
