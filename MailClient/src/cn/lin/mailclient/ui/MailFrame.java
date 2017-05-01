package cn.lin.mailclient.ui;

import cn.lin.mailclient.object.Mail;

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
    private JLabel receiverLabel = new JLabel("收件人：");
    private JTextField receiverText = new JTextField(60);
    private JLabel csLabel = new JLabel("抄送： ");
    private JTextField csText = new JTextField(60);
    private JLabel subjectLabel = new JLabel("主题： ");
    private JTextField subjectText = new JTextField(60);
    private JScrollPane textScrollPane;
    private JTextArea textArea = new JTextArea(20,50);
    private JSplitPane vSplit;
    private JSplitPane hSplit;
    private JScrollPane fileScrollPane;
    private JList fileList;
    private Box receiverBox = Box.createHorizontalBox();
    private Box csBox = Box.createHorizontalBox();
    private Box subjectBox = Box.createHorizontalBox();
    private Box mainBox = Box.createVerticalBox();
    private JToolBar toolBar = new JToolBar();
    private MailMain mailMain;

    //private SystemHandler systemHander;

    //private MailSender mailSender;
    //事件
    private Action send = new AbstractAction("发送") {
        @Override
        public void actionPerformed(ActionEvent e) {
            send();
        }
    };

    private Action saveOut = new AbstractAction("保存至发件箱") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    private Action saveDraft = new AbstractAction("保存至草稿箱") {
        @Override
        public void actionPerformed(ActionEvent e) {

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
        this.mailMain = mailMain;
        //this.systemHandler = mailMain.getSystemHandler();
        //this.mailSender = mailMain.getMailSender();
        this.toolBar.add(this.send).setToolTipText("发送");
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
        this.receiverText = receiverText;
    }

    public void setCsText(JTextField csText) {
        this.csText = csText;
    }

    public void setSubjectText(JTextField subjectText) {
        this.subjectText = subjectText;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void send(){
        String mailContent = "";  //邮件报文
        String response = "";     //来自服务器的应答
        String mailServer = "";   //邮件服务器
        String mailText ="";          //正文
        String from = "";         //发件人地址
        String[] to ;        //收件人地址
        String subject = "";      //邮件主题
        String tempt = "";

        //设置邮件服务器、发件人地址、收件人地址
        mailServer = "smtp.163.com";
        from = mailMain.getUser().getUserName();
        to = this.receiverText.getText().split(",");
        for(String t:to){
            tempt = "<"+t+">" +" " + tempt;
        }
        subject = this.subjectText.getText();
        mailText = this.textArea.getText();
        //设置邮件数据
        String pattern = "<.*?>";
        System.out.println(Pattern.matches(pattern,mailText));
        System.out.println(mailText);
        if(Pattern.matches(pattern,mailText)){
            System.out.println("HTML");
            mailContent = "From: " + from + "\n" +
                          "To: " + tempt + "\n" +
                          "Subject: " + subject +"\n"+
                          "Content-Type: "+"Text/html"+"\n\n" +
                           mailText + "\n";
        }
        else{
            System.out.println("Text");
            mailContent = "From: " + from + "\n" +
                          "To: " + tempt + "\n" +
                          "Subject: " + subject + "\n\n" +
                          mailText + "\n";
        }

        try{
            //得到本机主机名
            String hostName = InetAddress.getLocalHost().getHostName();
            //建立一个到邮件服务器的连接，端口号25
            Socket s = new Socket(mailServer,25);
            //将SOCKET输入流连接到带缓冲功能的
            //输入流BufferedReader，以便一次读一行来自
            //服务器的应答报文
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //将SOCKET输出流连接到带缓冲功能的
            //输出流PrintWriter，以便一次输出一行报文到服务器
            PrintWriter outToServer = new PrintWriter(s.getOutputStream() ,true);

            //读取来自服务器的第一行应答，并显示在屏幕上
            response = inFromServer.readLine();
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
                outToServer.println("RCPT TO: <" + t + ">" + " " +t);
                //读入来自服务器的应答，并显示在屏幕上
                response = inFromServer.readLine();
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
    private void refreshWindow(){
        this.receiverText.setText("");
        this.csText.setText("");
        this.subjectText.setText("");
        this.textArea.setText("");
    }
}
