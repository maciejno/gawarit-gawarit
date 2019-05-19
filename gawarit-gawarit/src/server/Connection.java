package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection implements Runnable {

    public User user = null; //na razie null bo nie wiadomo kto to
    String line = null;

    private final BufferedReader reciever;
    private final BufferedWriter transmitter;
    Socket socket;


    public Connection(Socket sckt, BufferedReader recv, BufferedWriter trans) {
        socket = sckt;
        reciever = recv;
        transmitter = trans;
    }

    @Override
    public void run() {
        //procedura logowania
        System.out.println("Polaczenie nawiazane.");
        try {
            line = reciever.readLine();
            if(!line.equals("~$instr&")){ //procedura logowania
                System.out.println("Niepoprawny protokol. Koniec polaczenia.");
                socket.close();
                return;
            }
            line = reciever.readLine();
            if(!line.equals("~$login&")) {
                System.out.println("Niepoprawny protokol. Koniec polaczenia.");
                socket.close();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(3>2) {
            try {
                transmitter.write("Odpowiadam: ");
                line = reciever.readLine();
                transmitter.write(line);
                transmitter.write("\n");
                transmitter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
    }

}
