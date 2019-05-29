package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GuiMessageFrame extends JPanel implements ActionListener{

    private static final long serialVersionUID = 1L;
	GuiMessageFrame gui;
    MessageFrame frame;
    
    String nick = null;
    String history = "";
    String message = "здравствуйте!";
    JButton sendButton;
    JTextField  nickLabel;
    JEditorPane textPane,historyPane; //tu jest historia konwersacji i wiadomosc do wyslania
    JScrollPane historyScrollPane,scrollPane;
    JPanel panelDown;

    public GuiMessageFrame(MessageFrame frame) throws IOException {
        gui = this;
        this.frame = frame;
        this.nick = frame.getUsername();
        panelDown = new JPanel();
        historyScrollPane = new JScrollPane();
        nickLabel = new JTextField("Chat z " + nick);
        nickLabel.setHorizontalAlignment(JTextField.CENTER);
        nickLabel.setEditable(false);
        nickLabel.setForeground(Color.WHITE);
        nickLabel.setBackground(Client.galubyj);
        textPane = new JEditorPane();
        historyPane = new JEditorPane();
        scrollPane = new JScrollPane(textPane, 
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(0, 40));;
        historyScrollPane = new JScrollPane(historyPane,
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        historyPane.setText(history);
        textPane.setText(message);//ustawia wiadomosc powitalna
        sendButton = new JButton("Wyślij");
        sendButton.setBackground(Client.krasnyj);
        historyPane.setEditable(false); //zeby historii  nie edytowac
        
        panelDown.setLayout(new GridLayout(2,1));
        panelDown.add(scrollPane);
        panelDown.add(sendButton);
        
        gui.setLayout(new BorderLayout());
        gui.add(nickLabel, BorderLayout.NORTH);
        gui.add(historyScrollPane, BorderLayout.CENTER);
        gui.add(panelDown, BorderLayout.SOUTH);
        
        sendButton.addActionListener(this);
        sendButton.setActionCommand("send"); 
        
        historyScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
        });
        
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
        });
    }

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("send") && !textPane.getText().equals("")) {
			
			//if(textPane.getText())
			message = Client.createMessage("~$message&",nick + "\r\n" + textPane.getText());			
			String newHistory = historyPane.getText() + "Ja:\r\n" + textPane.getText() + "\r\n\r\n";//dopisuje do obecnego tekstu nową wiadomość
			historyPane.setText(newHistory);// ustawia na nowo tekst w oknie historii wiadomości
			historyScrollPane.getVerticalScrollBar().setValue(historyScrollPane.getVerticalScrollBar().getMaximum());//scrolluje na dół
			textPane.setText("");
			try {
				Client.send(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	public JEditorPane getHistoryPane() {return historyPane;}
	public JScrollPane getHistoryScrollPane() {return historyScrollPane;}
}
