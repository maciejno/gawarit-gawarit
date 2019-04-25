package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Conversation implements Runnable{

    private static ServerSocket serverSocket;
    private static Socket socket;


    public static void main(String[] args) throws Exception{

        serverSocket = new ServerSocket(38);
        while (true) {
            socket = serverSocket.accept();
            new Thread(new Conversation()).start();
        }
    }

    @Override
    public void run() {
        try{
            BufferedReader czytacz = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter pisacz = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            pisacz.write("Kto śmie pukać do bram Serwera?!");
            pisacz.write("\n");
            pisacz.flush();
            String line = czytacz.readLine();
            while (!line.contains(">q")){ //trzeba ustalić protokół komunikacji pomiędzy serwerem i klientami
                pisacz.write("Odpowiadam: ");
                pisacz.write(line);
                pisacz.write("\n");
                pisacz.flush();
                line = czytacz.readLine();
            }
            socket.close();
            } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
