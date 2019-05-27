package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class Connection implements Runnable {

    public User user = new User();
    String line = null;

    BufferedReader reciever;
    BufferedWriter transmitter;
    Socket socket;
    final String tempUsername;

    public Connection(Integer index, Socket sckt) {
        socket = sckt;
        tempUsername = index.toString();
        try {
            reciever = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            transmitter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void run() { //wunkcja run, odpowiadająca za obsługę połączenia z pojedynczym użytkownikiem
        ServerMain.StatusUpdate(); //aktualizacja terminala
        //procedura logowania
        ServerMain.Monitor("Polaczenie nawiazane...");
        try {
            if(!reciever.readLine().equals("~$instr&")){ //procedura logowania
                ServerMain.Monitor("Niepoprawny protokol. Koniec polaczenia. (~$instr& login)");
                socket.close();
                ServerMain.connections.remove(tempUsername); //usuwa klasę connection użytkownika z listy aktywnych połączeń
                ServerMain.StatusUpdate();
                return;
            }
            line = reciever.readLine();
            if((!line.equals("~$login&"))&&(!line.equals("~$register&"))) { //spaawdzanie poprawności protokołu
                ServerMain.Monitor("Niepoprawny protokol. Koniec polaczenia. (~$login& / ~$register&)");
                socket.close();
                ServerMain.connections.remove(tempUsername);
                ServerMain.StatusUpdate();
                return;
            }
            ServerMain.Monitor("Zapytanie poprawne...");
            String login = reciever.readLine(); //odczytanie loginu i hasła wysyłanego przez usera
            String pass = reciever.readLine();
            if(!reciever.readLine().equals("~$end&")) { //dalsze sprawdzanie poprawności protokołu
                ServerMain.Monitor("Niepoprawny protokol. Koniec polaczenia. (endless)");
                socket.close();
                ServerMain.connections.remove(tempUsername);
                ServerMain.StatusUpdate();
                return;
            }
            ServerMain.Monitor("Nowy uzytkownik identyfikuje sie jako: " + login);
            //właściwe logowanie na serwer
            if(line.equals("~$login&")) {
               if(user.login(login,pass)) { //user.login zwraca true, jeśli użytkownik jest zarejestrowany
                   accpass();
                   ServerMain.Monitor("Uzytkownik " + user.username + " zalogowal sie na serwer.");
               }
                else{
                    rejpass(); //gdy nie ma "konta" użytkownika
                    ServerMain.Monitor("Nie udalo sie zalogowac uzytkownika " + login + " (bledny login/haslo)");
                    ServerMain.connections.remove(tempUsername);
                    try {
                        socket.close();
                    } catch (IOException ex) {
                       ex.printStackTrace();
                       ServerMain.Monitor("Wysyapil problem z zerwaniem polaczenia. (rejpass)");
                    }

               }
            }
            //rejestracja
            if(line.equals("~$register&")) {
                user.register(login, pass);
                ServerMain.Monitor("Zarejestrowano nowego uzytkownika " + user);
            }


        } catch (IOException e) {
            e.printStackTrace();
            if(ServerMain.connections.containsKey(tempUsername))
                ServerMain.connections.remove(tempUsername);
            if(ServerMain.connections.containsKey(user.username))
                ServerMain.connections.remove(user.username);
            ServerMain.StatusUpdate();
            ServerMain.Monitor("Wysyapil problem z polaczeniem. (login procedure)");
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                ServerMain.Monitor("Wysyapil problem z zerwanie polaczenia. (login procedure)");
            }
            return;
        }



        while(true) { //główna pętla obsługująca danego użytkownika
            ServerMain.StatusUpdate();
            try {
                line = reciever.readLine();

                if(line.equals("~$instr&")) {
                    line = reciever.readLine();



                    if(line.equals("~$logout&")) { //procedura wylogowywania
                        if (reciever.readLine().equals("~$end&")) {
                            try {
                                transmitter.write("~$instr&");
                                transmitter.write("\n");
                                transmitter.write("~$end&");
                                transmitter.write("\n");
                                transmitter.flush();
                                socket.close();
                                ServerMain.Monitor("Uzytkownik " + user.username + " wylogowal sie.");
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                ServerMain.Monitor("Problem z zerwaniem polaczenia. (logout)");
                            }
                            ServerMain.connections.remove(user.username);
                            ServerMain.StatusUpdate();
                            break;
                        }
                    }
                }

                if(line.equals("~$message&")) { //prcedura wysyłania wiadomości
                    System.out.println("Uzytkownik " + user.username + " probuje wyslac wiadomosc...");//debugging
                    String recipant = reciever.readLine();
                    System.out.println(recipant);
                    String content = reciever.readLine();
                    System.out.println(content);
                    //if (reciever.readLine().equals("~$end&")) {
                    line = reciever.readLine();
                    System.out.println(line); //więcej debuggingu
                    if(line.equals("~$end&")) {
                        System.out.println("Uzytkownik " + user.username + " nadal probuje wyslac wiadomosc...");
                        sendMessage(recipant, content);
                    }
                }


            } catch (IOException e) { //koniec głównej pętli + zabezpieczenia
                e.printStackTrace();
                ServerMain.Monitor("Uzytkownik " + user.username + " - problem z polaczeniem.");
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ServerMain.Monitor("Problem z zerwaniem polaczenia. (" + user.username + ")");
                }
                ServerMain.connections.remove(user.username);
                ServerMain.StatusUpdate();
                break;
            }
        }

    }

    private void rejpass() { //funkcja do odrzuania logowania
        try {
            transmitter.write("~$instr&");
            transmitter.write("\n");
            transmitter.write("~$rejpass&");
            transmitter.write("\n");
            transmitter.write("Bledny login/haslo");
            transmitter.write("\n");
            transmitter.write("~$end&");
            transmitter.write("\n");
            transmitter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            ServerMain.Monitor("Problem z wyslaniem. (rejpass)");
            return;
        }
    }

    private void accpass() { //funkcja do akceptacji logowania
        try {
            transmitter.write("~$instr&");
            transmitter.write("\n");
            transmitter.write("~$accpass&");
            transmitter.write("\n");
            ServerMain.connections.put(user.username, ServerMain.connections.get(tempUsername));
            ServerMain.connections.remove(tempUsername);
            for (String friend : user.friends) {
                transmitter.write(friend);
                transmitter.write("\n");
                if (ServerMain.connections.containsKey(friend))
                    transmitter.write("true");
                else
                    transmitter.write("false");
                transmitter.write("\n");
            }
            transmitter.write("~$end&");
            transmitter.write("\n");
            transmitter.flush();
        } catch (IOException e) {
        e.printStackTrace();
        ServerMain.Monitor("Problem z wyslaniem. (accpass)");
        return;
        }
    }

    private void register() { //funkcja do obsługi rejestracji

    }

    void sendMessage(String target, String message) { //funkcja do osługi wysyłania wiadomości
        try {
            System.out.println(target);
            if (ServerMain.connections.containsKey(target)) { //gdy adresat jest zalogowany
                System.out.println("dzieja sie rzeczy");
                ServerMain.connections.get(target).transmitter.write("~$message&");
                ServerMain.connections.get(target).transmitter.write("\n");
                ServerMain.connections.get(target).transmitter.write(user.username);
                ServerMain.connections.get(target).transmitter.write("\n");
                ServerMain.connections.get(target).transmitter.write(message);
                ServerMain.connections.get(target).transmitter.write("\n");
                ServerMain.connections.get(target).transmitter.write("~$end&");
                ServerMain.connections.get(target).transmitter.flush();
                ServerMain.Monitor("Wiadomosc: " + user.username + " > " + target);
            } else { //gdy adresat nie jest zalogowany
                transmitter.write("~$message&");
                transmitter.write("\n");
                transmitter.write(target);
                transmitter.write("\n");
                transmitter.write("Uzytkownik " + target + " jest niezalogowany.");
                transmitter.write("\n");
                transmitter.write("~$end&");
                transmitter.write("\n");
                transmitter.flush();
                ServerMain.Monitor("Wiadomosc: " + user.username + " ><< " + target);
            }
        } catch (IOException e) {
            e.printStackTrace();
            ServerMain.Monitor("Problem z wyslaniem wiadomosci. (" + user.username + " > " + target);
        }

    }

}
