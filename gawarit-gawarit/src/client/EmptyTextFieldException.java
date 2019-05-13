package client;

import javax.swing.JTextField;

//wyjatek - jesli pole tekstowe jest puste
	public class EmptyTextFieldException extends Exception{	
		
		private static final long serialVersionUID = 1L;
		EmptyTextFieldException(JTextField triedTextField){			
			triedTextField.setText("");
		}		
		public String toString(){
			return "Wyjatek! Pole tekstowe jest puste - wpisuje zero.";
		}
	}
