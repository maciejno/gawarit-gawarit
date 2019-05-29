package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
	
public class LoginFrame extends JFrame implements Runnable{

		private static final long serialVersionUID = 1L;
		GuiLoginFrame userInterface;
		LoginFrame mainFrame;
		
		JPanel panel;
		String text = "";
		String [] envelope = new String [2];
		
		static JFrame f = new JFrame();//do option pane
		
		public LoginFrame() throws LineUnavailableException, IOException{	
			this.mainFrame = this;
			
			this.setSize(300,300);
			this.setResizable(true);//zeby rozmiar okna byl staly
			this.setMinimumSize(new Dimension(300,300));//ustawia minimalny rozmiar okna
			this.setTitle("Gawarit-Gawarit");
			ImageIcon mainIcon = new ImageIcon(this.getClass().getResource("/logo_mini.png"));
			this.setIconImage(mainIcon.getImage());
			
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setLayout(new BorderLayout());
			
			userInterface = new GuiLoginFrame(this);
			this.add(userInterface, BorderLayout.CENTER);
			
			
			//USTAWIENIE LOOK AND FEEL
			try{          
	        	UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");            
	        }catch (Exception e1){
	            e1.printStackTrace();
	            System.err.println("Blad podczas ustawiania LookAndFeel");
	        }		
		}

		public GuiLoginFrame getGui() {return this.userInterface;}	
		
		@Override
		public void run() {
			
			while(true) {
			String username = null;
			String message = "";
				try {
					System.out.println("czekam aż coś przyjdzie");//DEBUGGING
		            String line = Client.reader.readLine();
		            System.out.println("czytam");//DEBUGGING
		            
		            //JEŚLI DOSTAŁ WIADOMOŚĆ
		            if(line.equals("~$message&")) {
		            	System.out.println("jakaś wiadomość");//DEBUGGING
		                line = Client.reader.readLine();   
		                username = line;
		                line = Client.reader.readLine();
		                while(!line.equals("~$end&")) {
		                	System.out.println(line);//DEBUGGING
		                	message = message + line + "\r\n";
		                	line = Client.reader.readLine();
		                }      		  
						if(Client.messageFrames.get(username)!=null) {//jesli juz jest to okienko
							//to sprawdza czy jest widoczne, a jak nie to uwidacznia
							if(!Client.messageFrames.get(username).isVisible())
								Client.messageFrames.get(username).setVisible(true);

							//System.out.println("wypiszę:" + Client.messageFrames.get(username).getGui().getHistoryPane().getText());
							String oldHistory = Client.messageFrames.get(username).getGui().getHistoryPane().getText();				
							String newHistory = oldHistory + username + ":\r\n" + message;//dopisuje do obecnego tekstu nową wiadomość
							Client.messageFrames.get(username).getGui().getHistoryPane().setText(newHistory);// ustawia na nowo tekst w oknie historii wiadomości
						}else {//jesli jeszcze nie ma okienka to dodaje a dalej to samo
							try {
								Client.messageFrames.put(username, new MessageFrame(username,Client.mainFrame.getMyName()));
								String newHistory = username + ":\r\n" + message;//dopisuje do obecnego tekstu nową wiadomość
								Client.messageFrames.get(username).getGui().getHistoryPane().setText(newHistory);// ustawia na nowo tekst w oknie historii wiadomości
							} catch (LineUnavailableException | IOException e) {
								e.printStackTrace();
							}
						}
						
						//JEŚLI DOSTAŁ INSTRUKCJĘ
		            }else if (line.equals("~$instr&")) {
		            	
		            	System.out.println("jakaś instrukcja");//DEBUGGING
		            	//WCZYTUJE CAŁĄ INSTRUKCJĘ
		            	while(!line.equals("~$end&")) {
			                	System.out.println(line);//DEBUGGING
			                	message = message + line + "\r\n";
			                	line = Client.reader.readLine();
		            	}
		            	//DZIELI JĄ NA LINIE
		            	String [] lines = message.split(System.getProperty("line.separator"));   
		            	
		            	//SPRAWDZA CO JEST W LINII PIERWSZEJ
		            	if(lines[1].equals("~$accpass&")) { //jeśli udało się zalogować lub zarejestrować
							//ustawia widoczność okienek
		            		System.out.println("Udało się zalogować");//DEBUGGING
							Client.framesMap.put(Client.loginFrame, false);
							try {
								Client.mainFrame = new MainFrame(Client.loginFrame.getGui().getLogin());
							} catch (LineUnavailableException | IOException e) {
								e.printStackTrace();
							}	
							Client.framesMap.put(Client.mainFrame, true);			
							Client.setVisibleFrames();
							Client.updateFriendsMap(lines);	              
						}else if(lines[1].equals("~$rejpass&")) {
							JOptionPane.showMessageDialog(null,lines[2], null, JOptionPane.INFORMATION_MESSAGE);
							System.out.println("Błędne dane logowania");
							Client.restartSocket();
						}else if(lines[1].equals("~$logout&")) {
							//ustawia widoczność okienek
							Client.framesMap.put(Client.loginFrame, true);
							Client.framesMap.put(Client.mainFrame, false);
							Client.setVisibleFrames();
							Client.restartSocket();
						}else if(lines[1].equals("~$rejectedinv&")) {
							String friend = lines[2];					
							JOptionPane.showMessageDialog(null, "Nie udało się dodać znajomego " + friend 
									+ ". Upewnij się, że istnieje i chce z tobą rozmawiać :)", null, JOptionPane.INFORMATION_MESSAGE);
						}else if(lines[1].equals("~$acceptedinv&")){
							String friend = lines[2];
							Client.friendsMap.put(friend, new Boolean("true")); //dodaje nowego znajomego do mapy
							JOptionPane.showMessageDialog(null, "Dodano znajomego " + friend ,
									 null, JOptionPane.INFORMATION_MESSAGE);
							Client.updateFriendsBox();						
						}else if(lines[1].equals("~$friends&")) {
							Client.updateFriendsMap(lines); //po usunieciu albo po prostu updateuje mape znajomych
						}else if(lines[1].equals("~$newinv&")) {// przyszło zaproszenie
							String friend = lines[2];
							int n = JOptionPane.showConfirmDialog( null, "Czy chcesz dodać " + friend + " do grona swoich znajomych?",
		                            "Nowe zaproszenie", JOptionPane.YES_NO_OPTION);		 
							if (n == JOptionPane.YES_OPTION) {//jak akceptuje
								message = Client.createMessage("~$instr&","~$accinv&",friend);
							}else {//jak nie akceptuje albo zamyka okienko
								message = Client.createMessage("~$instr&","~$rejinv&",friend);
			                }
							//wysyła
							try {
								Client.send(message);
							} catch (Exception e) {
								e.printStackTrace();
							}
							//aktualizuje listę znajomych
							Client.friendsMap.put(friend, new Boolean("true")); //dodaje nowego znajomego do mapy
							Client.updateFriendsBox();
						}else {
							JOptionPane.showMessageDialog(null,"Coś poszło nie tak", null, JOptionPane.INFORMATION_MESSAGE);
							System.out.println("Coś poszło nie tak");
							Client.restartSocket();
						}
		            }
		        }catch (IOException e) {
		            e.printStackTrace();
		        }				
			}
		}		
	}

