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
    private static String line=null;


    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(42);
        serverSocket = new ServerSocket(38);
        System.out.println("Uruchomiono Serwer.");
        while (true) {
            socket = serverSocket.accept();
            System.out.println("Nowa proba polaczenia...");
            Runnable connection = new Runnable() {
                @Override
                public void run() {
                    try{
                        BufferedReader reciever= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        BufferedWriter transmitter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        System.out.println("Nawiazano polaczenie.");

                        if(!reciever.readLine().equals("~$instr&")) { //procedura logowania
                            System.out.println("Niepoprawny protokol. Koniec polaczenia.");
                            socket.close();
                            return;
                        }
                        if(!reciever.readLine().equals("~$login&")) {
                            System.out.println("Niepoprawny protokol. Koniec polaczenia.");
                            socket.close();
                            return;
                        }
                        //User user = new User();
                        while (true){

                            //pisacz.write("Odpowiadam: ");
                            //pisacz.write(line);
                            //pisacz.write("\n");
                            //pisacz.flush();
                            //line = czytacz.readLine();
                        }
                        //socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Polaczenie nie powiodlo sie.");
                    }
                }
            };
            executorService.submit(connection);
        }
    }
}
