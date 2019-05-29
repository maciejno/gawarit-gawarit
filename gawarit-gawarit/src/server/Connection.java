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

    BufferedReader receiver;
    BufferedWriter transmitter;
    Socket socket;
    final String tempUsername;

    public Connection(Integer index, Socket sckt) {
        socket = sckt;
        tempUsername = index.toString();
        try {
            receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            transmitter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void run() {//funkcja run, odpowiadająca za obsługę połączenia z pojedynczym użytkownikiem

        ServerMain.Monitor("Polaczenie nawiazane...");

        String login;
        String pass;


        while(true){ //procedura logowania/rejestracji
            ServerMain.StatusUpdate(); //aktualizacja terminala

            try {
                line = receiver.readLine();

                if(line.equals("~$instr&")) {

                    line = receiver.readLine();

                    if (line.equals("~$login&")) {
                        login = receiver.readLine();
                        pass = receiver.readLine();
                        if(!receiver.readLine().equals("~$end&"))
                            continue;
                        ServerMain.Monitor("Uzytkownik identyfikuje sie jako: " + login);

                        if(ServerMain.connections.containsKey(login)) { //sprawdza czy uzytkownik jest juz zalogowany
                            rejpass("Uzytkownik o podanej nazwie jest juz zalogowany"); //gdy uzytkownik jest zalogowany
                            ServerMain.Monitor("Uzytkownik: " + login + " jest juz zalogowany.");
                            ServerMain.connections.remove(tempUsername);
                            try {
                                socket.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                ServerMain.Monitor("Wysyapil problem z zerwaniem polaczenia. (login procedure)");
                            }
                            return;
                        }

                        if (user.login(login, pass)) { //user.login zwraca true, jeśli użytkownik jest zarejestrowany
                            accpass();
                            ServerMain.Monitor("Uzytkownik " + user.username + " zalogowal sie na serwer.");
                            break;
                        } else {
                            rejpass("Bledny login/haslo"); //gdy nie ma "konta" użytkownika
                            ServerMain.Monitor("Nie udalo sie zalogowac uzytkownika " + login + " (bledny login/haslo)");
                            ServerMain.connections.remove(tempUsername);
                            try {
                                socket.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                ServerMain.Monitor("Wysyapil problem z zerwaniem polaczenia. (login procedure)");
                            }
                            return;
                        }
                    }

                    if (line.equals("~$register&")) {
                        login = receiver.readLine(); //odczytanie loginu i hasła wysyłanego przez usera
                        pass = receiver.readLine();
                        if(!receiver.readLine().equals("~$end&"))
                            continue;
                        ServerMain.Monitor("Nowy uzytkownik: " + login);
                        if (user.register(login, pass)) {
                            register();
                            ServerMain.Monitor("Uzytkownik " + user.username + " zarejestrowal sie.");
                            break;
                        } else {
                            rejpass("Podana nazwa juz istnieje");
                            ServerMain.Monitor("Nie udalo sie zarejestrowac uzytkownika " + login);
                            ServerMain.connections.remove(tempUsername);
                            try {
                                socket.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                ServerMain.Monitor("Wysyapil problem z zerwaniem polaczenia. (register procedure)");
                            }
                            return;
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                if(ServerMain.connections.containsKey(tempUsername))  //jeśli user jest dopiero świeżym wątkiem
                    ServerMain.connections.remove(tempUsername);
                if(ServerMain.connections.containsKey(user.username)) //jeśli user już się zalogował
                    ServerMain.connections.remove(user.username);
                ServerMain.StatusUpdate();
                ServerMain.Monitor("Zerwano polaczenie. (login procedure)");
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ServerMain.Monitor("Wysyapil problem z zerwaniem polaczenia. (login procedure)");
                }
                return;
            }

        }


        while(true) { //główna pętla obsługująca danego użytkownika
            ServerMain.StatusUpdate();
            try {
                line = receiver.readLine();

                if(line.equals("~$instr&")) {
                    line = receiver.readLine();

                    if(line.equals("~$friends&")) {
                        System.out.println("Uzytkownik " + user.username + " prosi o znajomych.");
                        if(receiver.readLine().equals("~$end&"))
                            sendFriends();
                    }

                    if(line.equals("~$logout&")) { //procedura wylogowywania
                        if (receiver.readLine().equals("~$end&")) {
                            logout();
                            ServerMain.connections.remove(user.username);
                            ServerMain.StatusUpdate();
                            return;
                        }
                    }


                    if(line.equals("~$addfriend&")) {
                        try {
                            String newfriend = receiver.readLine();
                            if(receiver.readLine().equals("~$end&")) {
                                if (user.friends.contains(newfriend)) { //gdy juz jest w znajomych
                                    transmitter.write("~$instr&");
                                    transmitter.write("\n");
                                    transmitter.write("~$acceptedinv&");
                                    transmitter.write("\n");
                                    transmitter.write(newfriend);
                                    transmitter.write("\n");
                                    transmitter.write("~$end&");
                                    transmitter.write("\n");
                                    transmitter.flush();
                                    ServerMain.Monitor(user.username + " >+<< " + newfriend + " (juz jest w znajomych)");
                                }
                                else{ //gdy nie ma w znajomych
                                    if(ServerMain.connections.containsKey(newfriend)) { //gdy zalogowany
                                        ServerMain.connections.get(newfriend).transmitter.write("~$instr&");
                                        ServerMain.connections.get(newfriend).transmitter.write("\n");
                                        ServerMain.connections.get(newfriend).transmitter.write("~$newinv&");
                                        ServerMain.connections.get(newfriend).transmitter.write("\n");
                                        ServerMain.connections.get(newfriend).transmitter.write(user.username);
                                        ServerMain.connections.get(newfriend).transmitter.write("\n");
                                        ServerMain.connections.get(newfriend).transmitter.write("~$end&");
                                        ServerMain.connections.get(newfriend).transmitter.write("\n");
                                        ServerMain.connections.get(newfriend).transmitter.flush();
                                        if(ServerMain.connections.get(newfriend).receiver.readLine().equals("~$instr&")) { //drugi odpowiada
                                            String response = ServerMain.connections.get(newfriend).receiver.readLine();
                                            String responsefriend = ServerMain.connections.get(newfriend).receiver.readLine();
                                            if(ServerMain.connections.get(newfriend).receiver.readLine().equals("~$end&")) { //odpowiadamy pierwszemu
                                                transmitter.write("~$instr&");
                                                transmitter.write("\n");
                                                if(response.equals("~$accinv&")) {
                                                    user.addFriend(responsefriend);
                                                    transmitter.write("~$acceptedinv&");
                                                    ServerMain.Monitor(user.username + " >+>> " + responsefriend);
                                                }
                                                else if(response.equals("~$rejinv&")) {
                                                    transmitter.write("~$rejectedinv&");
                                                    ServerMain.Monitor(user.username + " >+>xx " + responsefriend);
                                                }
                                                transmitter.write("\n");
                                                transmitter.write(responsefriend);
                                                transmitter.write("\n");
                                                transmitter.write("~$end&");
                                                transmitter.write("\n");
                                                transmitter.flush();
                                            }
                                        }
                                    }
                                    else { //gdy niezalogowany
                                        transmitter.write("~$instr&");
                                        transmitter.write("\n");
                                        transmitter.write("~$rejectedinv&");
                                        transmitter.write("\n");
                                        transmitter.write(newfriend);
                                        transmitter.write("\n");
                                        transmitter.write("~$end&");
                                        transmitter.write("\n");
                                        transmitter.flush();
                                        ServerMain.Monitor(user.username + " >+<< " + newfriend + " (niezalogowany)");
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            ServerMain.Monitor("Problem z dodawaniem znajomych. (" + user.username + ")");
                        }
                    }

                    if(line.equals("~$delfriend&")) {
                        String deletedfriend = receiver.readLine();
                        if(receiver.readLine().equals("~$end&")) {
                            if(user.friends.contains(deletedfriend)) {
                                user.removeFriend(deletedfriend);
                                sendFriends();
                                ServerMain.Monitor(user.username + " >-- " + deletedfriend);
                            }
                        }
                    }
                }

                if(line.equals("~$message&")) { //prcedura wysyłania wiadomości
                    System.out.println("Uzytkownik " + user.username + " probuje wyslac wiadomosc...");//debugging
                    String recipant = receiver.readLine();
                    System.out.println(recipant); //debugging
                    String content="";
                    line = receiver.readLine();
                    while(!line.equals("~$end&")) {
                        content = content + line + "\n";
                        line = receiver.readLine();
                    }
                    System.out.println(recipant);
                    System.out.println(content);
                    if(line.equals("~$end&")) {
                        System.out.println("Uzytkownik " + user.username + " zassal wiadomosc..."); //debugging
                        sendMessage(recipant, content);
                    }
                }


            } catch (IOException e) { //koniec głównej pętli + zabezpieczenia
                e.printStackTrace();
                ServerMain.Monitor("Uzytkownik " + user.username + " - zerwano polaczenie.");
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

    private void rejpass(String error) { //funkcja do odrzuania logowania
        try {
            transmitter.write("~$instr&");
            transmitter.write("\n");
            transmitter.write("~$rejpass&");
            transmitter.write("\n");
            transmitter.write(error);
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

    private void register() {
        try {
            transmitter.write("~$instr&");
            transmitter.write("\n");
            transmitter.write("~$accpass&");
            transmitter.write("\n");
            transmitter.write(user.username);
            transmitter.write("\n");
            transmitter.write("true");
            transmitter.write("\n");
            transmitter.write("~$end&");
            transmitter.write("\n");
            transmitter.flush();
            ServerMain.connections.put(user.username, ServerMain.connections.get(tempUsername));
            ServerMain.connections.remove(tempUsername);
        } catch (IOException e) {
            e.printStackTrace();
            ServerMain.Monitor("Problem z rejestracja.");
        }

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
                ServerMain.connections.get(target).transmitter.write("~$end&\n");
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

    void logout() {
        try {
            transmitter.write("~$instr&");
            transmitter.write("\n");
            transmitter.write("~$logout&");
            transmitter.write("\n");
            transmitter.write("~$end&");
            transmitter.write("\n");
            transmitter.flush();
            socket.close();
            ServerMain.Monitor("Uzytkownik " + user.username + " wylogowal sie.");
        } catch (IOException ex) {
            ex.printStackTrace();
            ServerMain.Monitor("Problem z zerwaniem polaczenia. (" + user.username + " logout)");
        }
    }

    void sendFriends() {
        try {
            System.out.println("Wywylanie przyjaciol.");
            transmitter.write("~$instr&");
            transmitter.write("\n");
            transmitter.write("~$friends&");
            transmitter.write("\n");
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
            ServerMain.Monitor("Problem z wyslaniem znajomych. (" + user.username + ")");
        }
    }



}
