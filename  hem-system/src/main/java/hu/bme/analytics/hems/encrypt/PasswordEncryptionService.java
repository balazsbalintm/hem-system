package hu.bme.analytics.hems.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordEncryptionService {
	public static boolean authenticateFromLocalFile(String attemptedUn, String attemptedPassword) {
		if(attemptedUn == null || !attemptedUn.equals("admin")) {
			return false;
		}
		
		try {

			File f = new File("appPassword");
			FileInputStream fis = new FileInputStream(f);
			byte[] bin = new byte[(int) f.length()];
			fis.read(bin);
			fis.close();
			return PasswordEncryptionService.authenticate(attemptedPassword, bin);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;

	}

	public static boolean authenticate(String attemptedPassword,
			byte[] encryptedPassword) throws NoSuchAlgorithmException,
			InvalidKeySpecException {

		byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword);

		return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
	}

	public static byte[] getEncryptedPassword(String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		String algorithm = "PBKDF2WithHmacSHA1";
		int derivedKeyLength = 160;
		int iterations = 20000;
		KeySpec spec = new PBEKeySpec(password.toCharArray(),
				"HEMSSALT".getBytes(), iterations, derivedKeyLength);

		SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

		return f.generateSecret(spec).getEncoded();
	}
}
