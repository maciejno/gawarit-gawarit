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
    public void run() {
        //procedura logowania
        ServerMain.Monitor("Polaczenie nawiazane...");
        try {
            if(!reciever.readLine().equals("~$instr&")){ //procedura logowania
                ServerMain.Monitor("Niepoprawny protokol. Koniec polaczenia. (~$instr& login)");
                socket.close();
                ServerMain.connections.remove(user.username);
                return;
            }
            line = reciever.readLine();
            if((!line.equals("~$login&"))&&(!line.equals("~$register&"))) {
                ServerMain.Monitor("Niepoprawny protokol. Koniec polaczenia. (~$login& / ~$register&)");
                socket.close();
                ServerMain.connections.remove(user.username);
                return;
            }
            ServerMain.Monitor("Zapytanie poprawne...");
            String login = reciever.readLine();
            String pass = reciever.readLine();
            if(!reciever.readLine().equals("~$end&")) {
                ServerMain.Monitor("Niepoprawny protokol. Koniec polaczenia. (endless)");
                socket.close();
                ServerMain.connections.remove(user.username);
                return;
            }
            ServerMain.Monitor("Nowy uzytkownik identyfikuje sie jako: " + login);

            if(line.equals("~$login&")) {
               if(user.login(login,pass)) {
                   accpass();
                   ServerMain.Monitor("Uzytkownik " + user.username + " zalogowal sie na serwer.");
               }
                else{
                    rejpass();
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
            if(line.equals("~$register&")) {
                user.register(login, pass);
                ServerMain.Monitor("Zarejestrowano nowego uzytkownika " + user);
            }


        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                ServerMain.Monitor("Wysyapil problem z zerwaniem polaczenia. (login procedure)");
            }
            ServerMain.connections.remove(user.username);
            ServerMain.Monitor("Wysyapil problem z polaczeniem. (login procedure)");
        }



        while(true) {
            try {
                line = reciever.readLine();

                if(line.equals("~$instr&")) {
                    line = reciever.readLine();



                    if(line.equals("~$logout&")) {
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
                            break;
                        }
                    }
                }

                if(line.equals("~$message&")) {
                    String recipant = reciever.readLine();
                    String content = reciever.readLine();
                    if (reciever.readLine().equals("~$end&"))
                        sendMessage(recipant,content);
                }


            } catch (IOException e) {
                e.printStackTrace();
                ServerMain.Monitor("Uzytkownik " + user.username + " zerwal polaczenie.");
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ServerMain.Monitor("Problem z zerwaniem polaczenia. (" + user.username + ")");
                }
                ServerMain.connections.remove(user.username);
                break;
            }
        }

    }

    private void rejpass() {
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

    private void accpass() {
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

    }

    void sendMessage(String target, String message) {
        try {
            if (ServerMain.connections.containsKey(target)) {
                ServerMain.connections.get(target).transmitter.write("~$message&");
                ServerMain.connections.get(target).transmitter.write("\n");
                ServerMain.connections.get(target).transmitter.write(user.username);
                ServerMain.connections.get(target).transmitter.write("\n");
                ServerMain.connections.get(target).transmitter.write(message);
                ServerMain.connections.get(target).transmitter.write("\n");
                ServerMain.connections.get(target).transmitter.write("~$end&");
                ServerMain.connections.get(target).transmitter.flush();
                ServerMain.Monitor("Wiadomosc: " + user.username + " > " + target);
            } else {
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
