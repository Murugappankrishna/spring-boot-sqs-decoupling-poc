package space.murugappan.userverificationservice.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class MailUtils {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public MailUtils(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    public void sendVerificationEmail(String to, String name, String verificationCode) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("verificationCode", verificationCode);
        String htmlBody = templateEngine.process("verification-email", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,  true,"UTF-8");
        helper.setTo(to);
        helper.setSubject("Verify Your Email Address");
        helper.setText(htmlBody, true);
        mailSender.send(mimeMessage);
    }
}
