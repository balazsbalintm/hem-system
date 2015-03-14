package hu.bme.analytics.hems.encrypt;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PasswordModifierListener implements ActionListener {
	private final static Logger LOGGER = Logger.getLogger(PasswordModifierListener.class.getName());	
	
	private JFrame mainFrame;
	private TextField tf_oldPw;
	private TextField tf_newPw;
	private TextField tf_newPwAgain;
	
	public PasswordModifierListener(JFrame mainFrame, TextField tf_oldPw, TextField tf_newPw, TextField tf_newPwAgain) {
		this.mainFrame = mainFrame;
		this.tf_oldPw = tf_oldPw;
		this.tf_newPw = tf_newPw;
		this.tf_newPwAgain = tf_newPwAgain;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		LOGGER.log(Level.INFO, "Submit button was pressed!");
		try {
			File f = new File("appPassword");
			FileInputStream fis = new FileInputStream(f);
			byte[] bin = new byte[(int)f.length()];
			fis.read(bin);
			fis.close();
							
			Boolean result = PasswordEncryptionService.authenticate(tf_oldPw.getText(), bin);
			
			if(result) {
				if(tf_newPw.getText() != null && tf_newPw.getText().equals(tf_newPwAgain.getText())) {
					byte[] pw = PasswordEncryptionService.getEncryptedPassword(tf_newPw.getText());
					FileOutputStream fos = new FileOutputStream("appPassword");
					fos.write(pw);
					fos.close();
					
					JOptionPane.showMessageDialog(mainFrame, "Jelszo modositva!", "", JOptionPane.PLAIN_MESSAGE);
					LOGGER.log(Level.INFO, "Password was successfully modified!");
					
				} else {
					JOptionPane.showMessageDialog(mainFrame, "Az uj jelszo helytelen (elter a ket mezoben)!", "", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.INFO, "New passwords were different in the new password fields!");
				}
			} else {
				JOptionPane.showMessageDialog(mainFrame, "Helytelen jelszo!", "", JOptionPane.ERROR_MESSAGE);
				LOGGER.log(Level.INFO, "The initial password was incorrect!");
			} 							

		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			LOGGER.log(Level.SEVERE, "Error during changing the password: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
