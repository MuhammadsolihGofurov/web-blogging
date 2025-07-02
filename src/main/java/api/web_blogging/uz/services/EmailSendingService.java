package api.web_blogging.uz.services;


import api.web_blogging.uz.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingService {

    @Value("${spring.mail.username}")
    private String fromAccount;

    @Value("${server.domain}")
    private String serverDomain;


    @Autowired
    private JavaMailSender mailSender;


    public void sendRegistrationEmail(String email, Integer profileId) {
        String subject = "Complete Registration";
        String link = serverDomain + "/auth/register/verification/" + JwtUtil.encodeEmail(profileId);
        String body = "Welcome, to our website. Please click the link for complete your registration" + " " + link + " !";
        sendEmail(email, subject, body);
    }

    private void sendEmail(String email, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);

//        return "Mail was send";

    }

}
