package org.drathveloper;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parameters {

    public static final String RECEIVER_FLAG = "-t";

    public static final String CC_FLAG = "-cc";

    public static final String SUBJECT_FLAG = "-s";

    public static final String BODY_FLAG = "-b";

    public static final String ATTACHMENT_FLAG = "-a";

    private List<String> receiverContacts;

    private List<String> carbonCopyContacts;

    private String subject;

    private String body;

    private List<String> attachmentsPaths;

    public Parameters(ArgsParser parser){
        this.receiverContacts = parser.getParametersFromOption(RECEIVER_FLAG);
        this.carbonCopyContacts = parser.getParametersFromOption(CC_FLAG);
        this.subject = parser.getParametersFromOption(SUBJECT_FLAG)
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(" "));
        this.body = parser.getParametersFromOption(BODY_FLAG)
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        this.attachmentsPaths = parser.getParametersFromOption(ATTACHMENT_FLAG);
    }

    private String getArrayParameterAsString(List<String> list){
        StringBuilder output = new StringBuilder();
        for(int i=0; i<list.size(); i++){
            output.append(list.get(i));
            if(i < list.size() - 1){
                output.append(", ");
            }
        }
        return output.toString();
    }

    public String getReceiverContacts(){
        return this.getArrayParameterAsString(receiverContacts);
    }

    public String getCarbonCopyContacts(){
        return this.getArrayParameterAsString(carbonCopyContacts);
    }

    public String getSubject(){
        return subject;
    }

    public String getBody(){
        return body;
    }

    public List<DataSource> getAttachmentDataSources(){
        List<DataSource> attachmentDataSources = new ArrayList<>();
        for(String attachment : attachmentsPaths){
            attachmentDataSources.add(new FileDataSource(attachment));
        }
        return attachmentDataSources;
    }

}
