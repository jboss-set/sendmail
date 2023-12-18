package org.jboss.set.sendmail;

import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "java -jar sendmail.jar", sortOptions = false, sortSynopsis = false)
public class Main implements Callable<Integer> {

    @CommandLine.Option(names = "--smtp-server", required = true, order = 6,
            description = "SMTP server address")
    private String smtpServer;

    @CommandLine.Option(names = "--smtp-port", defaultValue = "587", order = 7,
            description = "SMTP server port, default is 587")
    private Integer smtpPort;

    @CommandLine.Option(names = "--from", required = true, order = 1,
            description = "The sender email address")
    private String fromAddress;

    @CommandLine.Option(names = "--to", required = true, order = 2,
            description = "The recipient email address")
    private String toAddresses;

    @CommandLine.Option(names = "--subject", required = true, order = 3,
            description = "Email subject")
    private String subject;

    @CommandLine.Option(names = "--content", required = true, order = 4,
            description = "Path to a file containing email body")
    private Path contentFile;

    @CommandLine.Option(names = "--content-type", defaultValue = "text/plain; charset=utf-8", order = 5,
            description = "Email body content type, default is \"text/plain\"")
    private String contentType;

    @CommandLine.Option(names = { "-h", "--help"}, usageHelp = true, order = 0,
            description = "Prints the usage message")
    private boolean help;

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new Main());
        commandLine.execute(args);
    }

    @Override
    public Integer call() throws Exception {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", false);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", smtpServer);
        prop.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(prop);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(toAddresses));
        message.setSubject(subject);

        String msg = Files.readString(contentFile);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, contentType);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);

        return CommandLine.ExitCode.OK;
    }
}