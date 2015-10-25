package hu.bme.analytics.hems.encrypt;

import java.awt.GridLayout;
import java.awt.TextField;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PasswordChangeApplication {
	private final static Logger LOGGER = Logger
			.getLogger(PasswordChangeApplication.class.getName());

	private static JFrame mainFrame = new JFrame();
	private static GridLayout grid_main = new GridLayout(4, 2);

	private static JLabel lbl_oldPw = new JLabel("Old password:");
	private static JLabel lbl_newPw = new JLabel("New password:");
	private static JLabel lbl_newPwAgain = new JLabel("New password again:");

	private static TextField tf_oldPw = new TextField();
	private static TextField tf_newPw = new TextField();
	private static TextField tf_newPwAgain = new TextField();

	private static JButton btn_submit = new JButton("Update!");

	public static void main(String[] args) {
		LOGGER.log(Level.INFO,
				"Change password application is being displayed!");

		mainFrame.setLayout(grid_main);

		mainFrame.add(lbl_oldPw);
		mainFrame.add(tf_oldPw);

		mainFrame.add(lbl_newPw);
		mainFrame.add(tf_newPw);

		mainFrame.add(lbl_newPwAgain);
		mainFrame.add(tf_newPwAgain);

		mainFrame.setSize(900, 600);
		mainFrame.setVisible(true);

		mainFrame.add(btn_submit);

		btn_submit.addActionListener(new PasswordModifierListener(mainFrame,
				tf_oldPw, tf_newPw, tf_newPwAgain));
	}


}