package ra.md5_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import ra.md5_project.service.serviceIPM.EmailSenderService;

import javax.mail.MessagingException;

@SpringBootApplication
public class Md5ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Md5ProjectApplication.class, args);
	}
//	@Autowired
//	private EmailSenderService emailSenderService;
//	@EventListener(ApplicationReadyEvent.class)
//	public void sendEmail()  {
//		emailSenderService.sendEmail("vanhungkfc190699gmail.com","this is sub","this is body");
//	}

}
