package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {

    private static ServerSocket serverSocket;
    private static Socket socket;


    public static void main(String[] args) throws Exception{
        //User test = new User("Ivan42");
        ExecutorService executorService = Executors.newFixedThreadPool(42);
        serverSocket = new ServerSocket(38);
        System.out.println("Uruchomiono serwer");
        //test.PrintInfo();
        while (true) {
            socket = serverSocket.accept();
            Runnable connection = new Runnable() {
                @Override
                public void run() {
                    try{
                        BufferedReader czytacz = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        BufferedWriter pisacz = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        pisacz.write("Kto smie pukas do bram Serwera?!");
                        pisacz.write("\n");
                        pisacz.flush();
                        System.out.println("Nawiazano polaczenie z uzytkownikiem");
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
            };
            executorService.submit(connection);
        }
    }
}
