package org.drathveloper;

import org.simplejavamail.email.AttachmentResource;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import javax.activation.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailClient {

    private static final String SMTP_TLS_STRATEGY = "SMTP_TLS";

    private static final String SMTPS_STRATEGY = "SMTPS";

    private static final String SMTP_STRATEGY = "SMTP";

    private String sender;

    private Email email;

    private Mailer mailer;

    public MailClient(String path) throws IOException, NumberFormatException {
        Properties props = new Properties();
        props.load(new FileInputStream(path + "\\mailer.properties"));
        this.sender = props.getProperty("simplejavamail.smtp.username");
        this.buildMailer(props);
    }

    private void buildMailer(Properties props) throws NumberFormatException {
        String server = props.getProperty("simplejavamail.smtp.host");
        Integer port = Integer.parseInt(props.getProperty("simplejavamail.smtp.port"));
        String user = props.getProperty("simplejavamail.smtp.username");
        String password = props.getProperty("simplejavamail.smtp.password");
        String transportStrategy = props.getProperty("simplejavamail.transportstrategy");
        boolean opportunisticTLS = this.checkOpportunisticTLS(props.getProperty("simplejavamail.opportunistic.tls"));
        TransportStrategy strategy = this.selectTransportStrategy(transportStrategy, opportunisticTLS);
        mailer = MailerBuilder
                .withSMTPServer(server, port, user, password)
                .withTransportStrategy(strategy)
                .buildMailer();
    }

    private boolean checkOpportunisticTLS(String opportunisticTLS){
        return opportunisticTLS.equals("true");
    }

    private TransportStrategy selectTransportStrategy(String strategy, boolean opportunisticTLS) throws IllegalArgumentException{
        switch (strategy) {
            case SMTP_TLS_STRATEGY:
                TransportStrategy.SMTP_TLS.setOpportunisticTLS(opportunisticTLS);
                return TransportStrategy.SMTP_TLS;
            case SMTPS_STRATEGY:
                TransportStrategy.SMTPS.setOpportunisticTLS(opportunisticTLS);
                return TransportStrategy.SMTPS;
            case SMTP_STRATEGY:
                TransportStrategy.SMTP.setOpportunisticTLS(opportunisticTLS);
                return TransportStrategy.SMTP;
            default:
                throw new IllegalArgumentException("Bad simplejavamail.transportstrategy value");
        }
    }

    public void sendMail(Parameters params) {
        this.buildBasicEmail(params);
        this.addCCToEmail(params);
        this.addAttachmentsToEmail(params);
        mailer.sendMail(email);
    }

    private void buildBasicEmail(Parameters params){
        email = EmailBuilder
                .startingBlank()
                .from(sender)
                .to(params.getReceiverContacts())
                .withSubject(params.getSubject())
                .withHTMLText(params.getBody())
                .buildEmail();
    }

    private void addAttachmentsToEmail(Parameters params){
        List<AttachmentResource> attachments = this.buildAttachmentResourceList(params.getAttachmentDataSources());
        if(attachments.size() > 0){
            email = EmailBuilder
                    .copying(email)
                    .withAttachments(attachments)
                    .buildEmail();
        }
    }

    private void addCCToEmail(Parameters params){
        if(!params.getCarbonCopyContacts().equals("")){
            email = EmailBuilder
                    .copying(email)
                    .cc(params.getCarbonCopyContacts())
                    .buildEmail();
        }
    }

    private List<AttachmentResource> buildAttachmentResourceList(List<DataSource> attachmentsList){
        List<AttachmentResource> attachments = new ArrayList<>();
        for(DataSource ds : attachmentsList){
            attachments.add(new AttachmentResource(ds.getName(), ds));
        }
        return attachments;
    }
}
