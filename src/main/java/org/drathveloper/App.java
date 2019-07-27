package org.drathveloper;

import java.io.File;
import java.io.IOException;

public class App
{

    private static final String[] requiredOptions = {Parameters.RECEIVER_FLAG, Parameters.SUBJECT_FLAG, Parameters.BODY_FLAG};

    public static final String[] optionalOptions = {Parameters.CC_FLAG, Parameters.ATTACHMENT_FLAG};

    public static void main( String[] args ) {
        try {
            File jarPath = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String executingPath = jarPath.getParentFile().getAbsolutePath();
            ArgsParser parser = new ArgsParser(args, requiredOptions, optionalOptions);
            Parameters parameters = new Parameters(parser);
            MailClient client = new MailClient(executingPath);
            client.sendMail(parameters);
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (NullPointerException ex){
            System.out.println(ex.getMessage());
        }
    }
}
