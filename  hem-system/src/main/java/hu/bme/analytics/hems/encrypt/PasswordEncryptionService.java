package hu.bme.analytics.hems.encrypt;

import java.awt.GridLayout;
import java.awt.TextField;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PasswordEncryptionService {
	private final static Logger LOGGER = Logger.getLogger(PasswordEncryptionService.class.getName());
	
	private static JFrame mainFrame = new JFrame();
	private static GridLayout grid_main = new GridLayout(4,2);
	
	private static JLabel lbl_oldPw = new JLabel("Old password:");
	private static JLabel lbl_newPw = new JLabel("New password:");
	private static JLabel lbl_newPwAgain = new JLabel("New password again:");
	
	private static TextField tf_oldPw = new TextField();
	private static TextField tf_newPw = new TextField();
	private static TextField tf_newPwAgain = new TextField();
	
	private static JButton btn_submit = new JButton("Update!");
	
	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "Change password application is being displayed!");
		
		mainFrame.setLayout(grid_main);
		
		mainFrame.add(lbl_oldPw);
		mainFrame.add(tf_oldPw);
		
		mainFrame.add(lbl_newPw);
		mainFrame.add(tf_newPw);
		
		mainFrame.add(lbl_newPwAgain);
		mainFrame.add(tf_newPwAgain);
		
		mainFrame.setSize(900,600);
		mainFrame.setVisible(true);
		
		mainFrame.add(btn_submit);
		
		btn_submit.addActionListener(new PasswordModifierListener(mainFrame, tf_oldPw, tf_newPw, tf_newPwAgain));
	}
	
	public static boolean authenticate(String attemptedPassword, byte[] encryptedPassword)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		LOGGER.log(Level.INFO, "Authentication");
		
		byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword);

		return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
	}

	public static byte[] getEncryptedPassword(String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		LOGGER.log(Level.INFO, "Encrypt text");
		
		String algorithm = "PBKDF2WithHmacSHA1";
		int derivedKeyLength = 160;
		int iterations = 20000;
		KeySpec spec = new PBEKeySpec(password.toCharArray(), "HEMSSALT".getBytes(), iterations,
				derivedKeyLength);

		SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

		return f.generateSecret(spec).getEncoded();
	}
}