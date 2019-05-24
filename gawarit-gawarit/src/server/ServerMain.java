package server;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;



public class ServerMain {

    public static HashMap<String, Connection> connections = new HashMap<String, Connection>();

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
        Monitor("");
        Integer tempUsername=0;
        while (true) {
            Monitor(connections.size() + " podlaczonych uzytkownikow.");
            Socket socket = serverSocket.accept();
            Monitor("Nowa proba polaczenia...");
            Connection connect = new Connection(tempUsername, socket);
            Thread thread = new Thread(connect);
            connections.put(tempUsername.toString(), connect);
            tempUsername++;
            thread.start();
        }
    }

    static void Monitor(String msg){
        textArea.append(msg);
        textArea.append("\n");
    }

}


