package ra.md5_project.service.serviceIPM;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("vanhungkfc190699@gmail.com");
//        message.setTo(toEmail);
//        message.setText(body);
//        message.setSubject(subject);
//
//        mailSender.send(message);
//        System.out.println("Mail send successfully");

//
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("vanhungkfc190699@gmail.com");
        helper.setSubject(subject);
        helper.setTo(toEmail);
        boolean html = true;
        helper.setText(body,html);

        mailSender.send(message);
        System.out.println("Mail send successfully");
    }


}
