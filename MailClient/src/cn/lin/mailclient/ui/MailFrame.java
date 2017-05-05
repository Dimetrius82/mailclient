package cn.lin.mailclient.ui;

import cn.lin.mailclient.object.Mail;
import cn.lin.mailclient.object.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;


/*邮件编写界面
 * Created by strawberrylin on 17-4-18.
 */
public class MailFrame extends JFrame{
    private boolean label = false;
    private String mailContent = "";  //邮件报文
    private String response = "";     //来自服务器的应答
    private String mailServer = "";   //邮件服务器
    private String mailText ="";          //正文
    private String from = "";         //发件人地址
    private List<String> to ;        //收件人地址
    private List<String> csto ;        //抄送人地址
    private List<String> bcsto ;        //密送地址
    private String subject = "";      //邮件主题
    private Socket s;
    private BufferedReader inFromServer;
    private PrintWriter outToServer;
    private JLabel receiverLabel = new JLabel("收件人：");
    private JTextField receiverText = new JTextField(60);
    private JLabel csLabel = new JLabel("抄送： ");
    private JTextField csText = new JTextField(60);
    private JLabel bcsLabel = new JLabel("密送： ");
    private JTextField bcsText = new JTextField(60);
    private JLabel subjectLabel = new JLabel("主题： ");
    private JTextField subjectText = new JTextField(60);
    private JScrollPane textScrollPane;
    private JTextArea textArea;
    private JSplitPane vSplit;
    private JSplitPane hSplit;
    private JScrollPane fileScrollPane;
    private JList fileList;
    private Box receiverBox = Box.createHorizontalBox();
    private Box csBox = Box.createHorizontalBox();
    private Box bcsBox = Box.createHorizontalBox();
    private Box subjectBox = Box.createHorizontalBox();
    private Box mainBox = Box.createVerticalBox();
    private JToolBar toolBar = new JToolBar();
    private MailMain mailMain;

    //private SystemHandler systemHander;

    //private MailSender mailSender;
    //事件
    private Action send = new AbstractAction("发送RTF") {
        @Override
        public void actionPerformed(ActionEvent e) {
            label = false;
            send();
        }
    };
    private Action sendHtml = new AbstractAction("发送HTML") {
        @Override
        public void actionPerformed(ActionEvent e) {
            label = true;
            send();
        }
    };
    private Action saveDraft = new AbstractAction("保存至草稿箱") {
        @Override
        public void actionPerformed(ActionEvent e) {
            save();
        }
    };
    private Action upFile = new AbstractAction("上传附件") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    private Action deleteFile = new AbstractAction("删除附件") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    public MailFrame(MailMain mailMain){

        this.fileList = new JList();
        textArea = new JTextArea(20,50);
        this.mailMain = mailMain;

        //this.systemHandler = mailMain.getSystemHandler();
        //this.mailSender = mailMain.getMailSender();
        this.toolBar.add(this.send).setToolTipText("发送RTF");
        this.toolBar.addSeparator();
        this.toolBar.add(this.sendHtml).setToolTipText("发送HTML");
        this.toolBar.addSeparator();
        this.toolBar.add(this.saveDraft).setToolTipText("保存至草稿箱");
        this.toolBar.addSeparator();
        this.toolBar.add(this.upFile).setToolTipText("上传附件");
        this.toolBar.addSeparator();
        this.toolBar.add(this.deleteFile).setToolTipText("删除附件");
        this.toolBar.setMargin(new Insets(5,10,5,5));
        this.toolBar.setFloatable(false);
        //this.fileList.addMouseListener(new SendListMouseListener());
        //BOX布局
        //收件人
        this.receiverBox.add(Box.createHorizontalStrut(10));
        this.receiverBox.add(receiverLabel);
        this.receiverBox.add(Box.createHorizontalStrut(5));
        this.receiverBox.add(receiverText);
        this.receiverBox.add(Box.createHorizontalStrut(10));
        //抄送
        this.csBox.add(Box.createHorizontalStrut(10));
        this.csBox.add(csLabel);
        this.csBox.add(Box.createHorizontalStrut(12));
        this.csBox.add(csText);
        this.csBox.add(Box.createHorizontalStrut(10));
        //密送
        this.bcsBox.add(Box.createHorizontalStrut(10));
        this.bcsBox.add(bcsLabel);
        this.bcsBox.add(Box.createHorizontalStrut(12));
        this.bcsBox.add(bcsText);
        this.bcsBox.add(Box.createHorizontalStrut(10));
        //主题
        this.subjectBox.add(Box.createHorizontalStrut(10));
        this.subjectBox.add(subjectLabel);
        this.subjectBox.add(Box.createHorizontalStrut(12));
        this.subjectBox.add(subjectText);
        this.subjectBox.add(Box.createHorizontalStrut(10));
        //邮件头部填写的总Box
        this.mainBox.add(Box.createVerticalStrut(10));
        this.mainBox.add(this.receiverBox);
        this.mainBox.add(Box.createVerticalStrut(5));
        this.mainBox.add(this.csBox);
        this.mainBox.add(Box.createVerticalStrut(5));
        this.mainBox.add(this.bcsBox);
        this.mainBox.add(Box.createVerticalStrut(5));
        this.mainBox.add(this.subjectBox);
        this.mainBox.add(Box.createVerticalStrut(10));
        this.add(mainBox);

        //正文编辑区
        this.textArea.setLineWrap(true);;
        this.textScrollPane = new JScrollPane(this.textArea);
        //附件
        this.fileScrollPane = new JScrollPane(this.fileList);
        this.hSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,this.fileScrollPane,this.textScrollPane);
        this.hSplit.setDividerSize(5);
        this.hSplit.setDividerLocation(100);
        this.vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,this.mainBox,this.hSplit);
        this.vSplit.setDividerSize(5);
        this.add(this.toolBar,BorderLayout.SOUTH);
        this.add(vSplit);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //clean();
            }
        });
        this.setLocation(140,80);
    }

    public void setReceiverText(JTextField receiverText) {
        this.receiverText.setText(receiverText.getText());
    }

    public void setCsText(JTextField csText) {
        this.csText.setText(subjectText.getText());
    }

    public void setSubjectText(JTextField subjectText) {
        this.subjectText.setText(subjectText.getText());
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea.setText(textArea.getText());
    }

    public void send(){
        setMail("smtp.163.com");
        try{
            //得到本机主机名
            String hostName = InetAddress.getLocalHost().getHostName();
            //建立一个到邮件服务器的连接，端口号25
            this.connect(25);
            //将用户的帐号和密码以BASE64格式进行编码
            //以便进行服务器身份验证
            String encodedUser = Base64.getEncoder().encodeToString(mailMain.getUser().getUserName().getBytes("utf-8"));
            String encodedPwd = Base64.getEncoder().encodeToString(mailMain.getUser().getPassWord().getBytes("utf-8"));

            //和服务器会话，发送EHLO hostname命令
            outToServer.println("EHLO " + hostName);
            for(int i = 0; i < 7; i++){
                response = inFromServer.readLine();
            }
            //和服务器会话，发送AUTH LOGIN命令，请求身份验证
            outToServer.println("AUTH LOGIN ");

            //读入来自服务器的应答，并显示在屏幕上
            response = inFromServer.readLine();
            //向服务器发送自己的帐号
            outToServer.println(encodedUser);
            //读入来自服务器的应答，并显示在屏幕上
            response = inFromServer.readLine();
            //向服务器发送自己的密码
            outToServer.println(encodedPwd);

            response = inFromServer.readLine();
            outToServer.println("MAIL FROM: <" + from + ">");
            //读入来自服务器的应答，并显示在屏幕上
            response = inFromServer.readLine();
            //向服务器发送RCPT TO: 收件人地址
            for(String t : to){
                if(!t.equals("")){
                    outToServer.println("RCPT TO: <" + t + ">" + " " +t);
                    //读入来自服务器的应答，并显示在屏幕上
                    response = inFromServer.readLine();
                    System.out.println(response);
                }
            }

            //向服务器发送RCPT TO: 收件人地址
            for(String t : csto){
                if(!t.equals("")){
                    outToServer.println("RCPT TO: <" + t + ">" + " " +t);
                    //读入来自服务器的应答，并显示在屏幕上
                    response = inFromServer.readLine();
                    System.out.println(response);
                }
            }

            //向服务器发送RCPT TO: 收件人地址

            for(String t : bcsto){
                if(!t.equals("")){
                    outToServer.println("RCPT TO: <" + t + ">" + " " +t);
                    //读入来自服务器的应答，并显示在屏幕上
                    response = inFromServer.readLine();
                    System.out.println(response);
                }
            }

            //请求发送邮件正文
            outToServer.println("DATA");
            //读入来自服务器的应答，并显示在屏幕上
            response = inFromServer.readLine();
            //开始发送邮件正文
            outToServer.println(mailContent);
            //发送邮件结束标志
            outToServer.println(".");

            //读入来自服务器的应答，并显示在屏幕上
            response = inFromServer.readLine();
            if(response.contains("250 Mail OK")){
                this.setVisible(false);
                refreshWindow();
            }
            else{
                JOptionPane.showConfirmDialog(this,"发送失败，已保存到草稿箱","警告",JOptionPane.OK_CANCEL_OPTION);
                this.setVisible(false);
                refreshWindow();
            }
            //关闭SOCKET
            s.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void save(){
        setMail("imap.163.com");
        this.connect(143);
        try{
            outToServer.println("A01 LOGIN " + mailMain.getUser().getUserName()+" "+mailMain.getUser().getPassWord());
            response = inFromServer.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        mailMain.getCurrentMails().add(new Mail("xx",from,to,subject,"date","100",csto,mailContent,"Dra"));
        mailMain.refreshTable();
        mailMain.cleanMailInfo();
    }
    public void setMail(String mailServer){
        String tempr = "";
        String temps = "";
        String tempt = "";
        this.to = new ArrayList<String>();
        this.csto = new ArrayList<String>();
        this.bcsto = new ArrayList<String>();
        //设置邮件服务器、发件人地址、收件人地址
        this.mailServer = mailServer;
        this.from = mailMain.getUser().getUserName();
        String[] tempR =this.receiverText.getText().split(",");
        for(String t: tempR){
            this.to.add(t);
        }
        for(String t:to){
            tempr = "<"+t+">" +" " + tempr;
        }
        String[] tempS =this.csText.getText().split(",");
        for(String t: tempS){
            this.csto.add(t);
        }
        for(String t:csto){
            temps = "<"+t+">" +" " + temps;
        }
        String[] tempT =this.bcsText.getText().split(",");
        for(String t: tempT){
            this.bcsto.add(t);
        }
        for(String t:bcsto){
            tempt = "<"+t+">" +" " + tempt;
        }
        this.subject = this.subjectText.getText();
        this.mailText = this.textArea.getText();
        //设置邮件数据
        if(label){
            System.out.println("HTML");
            mailContent = "From: " + from + "\n" +
                    "To: " + tempr + "\n" +
                    "cc: " + temps + "\n"+
                    "Bcc: " + tempt + "\n"+
                    "Subject: " + subject +"\n"+
                    "Content-Type: "+"Text/html"+"\n\n" +
                    "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>hello</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    mailText+"\n" +
                    "</body>\n" +
                    "</html>" + "\n";
        }
        else{
            System.out.println("Text");
            mailContent = "From: " + from + "\n" +
                    "To: " + tempr + "\n" +
                    "cc: " + temps + "\n"+
                    "Bcc: " + tempt + "\n"+
                    "Subject: " + subject + "\n\n" +
                    mailText + "\n";
        }
    }

    private void refreshWindow(){
        this.receiverText.setText("");
        this.csText.setText("");
        this.subjectText.setText("");
        this.textArea.setText("");
    }


    public void connect(int port){
        try{
            s = new Socket(mailServer,port);
            this.inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream(),"UTF-8"));
            //将SOCKET输出流连接到带缓冲功能的
            //输出流PrintWriter，以便一次输出一行报文到服务器
            this.outToServer = new PrintWriter(s.getOutputStream() ,true);
            this.response = inFromServer.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
