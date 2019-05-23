package server;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerMain {

    public static Vector<Connection> connections = new Vector<Connection>();

    private static JFrame f;
    private static JTextArea textArea;
    private static JScrollPane scrollPane;


    public static void main(String[] args) throws Exception{

        f = new JFrame();
        f.setSize(450,600);
        f.setTitle("Gawarit-Gawarit Server Monitor");
        ImageIcon mainIcon = new ImageIcon(f.getClass().getResource("/logo_mini.png"));
        f.setIconImage(mainIcon.getImage());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(textArea.getFont().deriveFont(16f));
        f.add(scrollPane);
        f.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(44242);
        Monitor("Uruchomiono Serwer.");
        Monitor("IP: " + java.net.InetAddress.getLocalHost().getHostAddress().toString());
        Monitor("Port:  " + serverSocket.getLocalPort());
        //Monitor("\n");
        while (true) {
            Socket socket = serverSocket.accept();
            ServerMain.Monitor("Nowa proba polaczenia...");
            //BufferedReader reciever= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //BufferedWriter transmitter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Connection connect = new Connection(socket, connections);
            Thread thread = new Thread(connect);
            connections.add(connect);
            thread.start();
        }
    }

    static void Monitor(String msg){
        textArea.append(msg);
        textArea.append("\n");
    }

}


