package client;

import javax.swing.JFrame;

public class myFrame {//klasa przechowująca okienko i czy jest widoczne	
	JFrame frame;
	boolean visibility;
	
	public myFrame(JFrame frame, boolean visibility) {//konstruktor
		this.frame = frame;
		this.visibility = visibility;
	}
	
	public JFrame getFrame() {return frame;}
	public boolean getVisibility() {return visibility;}
	public void setFrame(JFrame frame) {this.frame = frame;}
	public void setVisibility(boolean visibility) {this.visibility = visibility;}
}
