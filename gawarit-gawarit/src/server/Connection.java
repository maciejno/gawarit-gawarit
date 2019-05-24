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
                ServerMain.Monitor("Wysyapil problem z zerwaniem polaczenia.");
            }
            ServerMain.connections.remove(user.username);
            ServerMain.Monitor("Wysyapil problem z polaczeniem.");
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


            } catch (IOException e) {
                e.printStackTrace();
                ServerMain.Monitor("Zerwano polaczenie.");
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ServerMain.Monitor("Problem z zerwaniem polaczenia. :O");
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
            ServerMain.Monitor("Problem z wyslaniem.");
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
        ServerMain.Monitor("Problem z wyslaniem.");
        return;
        }
    }

    private void register() {

    }

}
