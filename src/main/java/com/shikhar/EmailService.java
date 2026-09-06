package com.shikhar;

import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {

	public static void sendResetEmail(String toEmail, String resetLink) {

		final String username = System.getenv("BREVO_SMTP_LOGIN");
		final String password = System.getenv("BREVO_SMTP_KEY");
		final String senderEmail = System.getenv("BREVO_SENDER_EMAIL");

		Properties properties = new Properties();

		properties.put("mail.smtp.host", "smtp-relay.brevo.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(senderEmail, "FinTrack"));

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

			message.setSubject("FinTrack - Reset Your Password");

			String emailContent = "Hello,\n\n" + "You requested to reset your FinTrack password.\n\n"
					+ "Click the link below to reset your password:\n\n" + resetLink + "\n\n"
					+ "This link will expire in 30 minutes.\n\n"
					+ "If you did not request a password reset, please ignore this email.\n\n" + "Regards,\n"
					+ "FinTrack Team";

			message.setText(emailContent);

			Transport.send(message);

			System.out.println("Reset email sent successfully to: " + toEmail);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

}