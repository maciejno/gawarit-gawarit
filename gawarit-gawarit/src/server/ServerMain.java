package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerMain {

    public static HashMap<String, Connection> connections = new HashMap<String, Connection>();

    private static JFrame f;
    private static JTextArea textArea;
    private static JTextArea numberArea;
    private static JScrollPane scrollPane;


    public static void main(String[] args) throws Exception{

        f = new JFrame();
        f.setSize(450,600);
        f.setTitle("Gawarit-Gawarit Server Monitor");
        ImageIcon mainIcon = new ImageIcon(f.getClass().getResource("/logo_mini.png"));
        f.setIconImage(mainIcon.getImage());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        numberArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(textArea.getFont().deriveFont(16f));
        numberArea.setEditable(false);
        f.add(scrollPane, BorderLayout.CENTER);
        f.add(numberArea, BorderLayout.PAGE_END);
        f.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(44242);
        Monitor("Uruchomiono Serwer.");
        StatusUpdate();
        Monitor("IP: " + java.net.InetAddress.getLocalHost().getHostAddress().toString());
        Monitor("Port:  " + serverSocket.getLocalPort());
        Monitor("");
        Integer tempUsername=0;
        
        //ŻEBY SIĘ SAMO SCROLLOWAŁO - #sabotaż
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
        });
        //--------------------------------
        while (true) {
            StatusUpdate();
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

    static void StatusUpdate() {
        numberArea.setText("podlaczonych uzytkownikow: " + connections.size());
    }

}


