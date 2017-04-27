package cn.lin.mailclient.object;

/*邮件对象
 * Created by strawberrylin on 17-4-15.
 */
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
public class Mail {
    private String xmlName;
    private String sender;
    private List<String>receiver;
    private String subject;
    private Date receiveDate;
    private String size;
    private boolean hasRead;
    private String content;
    private List<String>ccs;
    private List<FileObject> files;
    private String from;

    public Mail(String xmlName, String sender, List<String> receiver, String subject, Date receiveDate, String size, boolean hasRead, String content, String from) {
        this.xmlName = xmlName;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.receiveDate = receiveDate;
        this.size = size;
        this.hasRead = hasRead;
        this.content = content;
        this.from = from;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<String> receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getCcs() {
        return ccs;
    }

    public void setCcs(List<String> ccs) {
        this.ccs = ccs;
    }

    public List<FileObject> getFiles() {
        return files;
    }

    public void setFiles(List<FileObject> files) {
        this.files = files;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String handleString(StringBuffer buf){
        String result = buf.toString();
        if("".equals(result)) return result;
        return result.substring(0,result.lastIndexOf(","));
    }

    public String getCsName(){
        StringBuffer csString = new StringBuffer();
        for(String cs : this.ccs){
            csString.append(cs + ",");
        }
        return handleString(csString);
    }

    public String sendToName(){
        StringBuffer toName = new StringBuffer();
        for(String to : this.receiver){
            toName.append(to + ",");
        }
        return handleString(toName);
    }

    public static String getSize(int size){
        double temp = Double.valueOf(size);
        double result = temp / 1024;
        return (new java.text.DecimalFormat("#.##")).format(result);
    }
}
