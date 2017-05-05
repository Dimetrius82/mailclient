import java.io.*;
import java.net.*;
import sun.misc.*;
import java.util.*;
public class SendMail {
    public static void main(String[] args) throws Exception{

        String mailContent = "";  //邮件报文
        String response = "";     //来自服务器的应答
        String mailServer = "";   //邮件服务器
        String from = "";         //发件人地址
        String to1 = "";           //收件人地址
        String to2 = "";
        String cc = "";
        //设置邮件服务器、发件人地址、收件人地址
        mailServer = "smtp.163.com";
        from = "hust_wanglin@163.com";
        to1 = "hust_wanglin@163.com";
        to2 = "15888797753@163.com";
        cc = "424435985@qq.com";
        //设置邮件正文
        mailContent =
                "From: " + from + "\n" +
                "To: " + to1 + to2 + "\n" +
                "Subject: " + "Hello" + "\n" +
                "Content-Type: "+"text/html"+"\n\n" +
                "<div style=\"line-height:1.7;color:#000000;font-size:14px;font-family:Arial\">This is an email to test the draftbox for desktop.<br></div>)\n";
        //得到本机主机名
        String hostName = InetAddress.getLocalHost().getHostName();
        //建立一个到邮件服务器的连接，端口号25
        Socket s = new Socket(mailServer,25);
        //将SOCKET输入流连接到带缓冲功能的
        //输入流BufferedReader，以便一次读一 行来自
        //服务器的应答报文
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
        //将SOCKET输出流连接到带缓冲功能的
        //输出流PrintWriter，以便一次输出一行报文到服务器
        PrintWriter outToServer = new PrintWriter(s.getOutputStream() ,true);

        //读取来自服务器的第一行应答，并显示在屏幕上
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        //将用户的帐号和密码以BASE64格式进行编码
        //以便进行服务器身份验证
        String encodedUser = Base64.getEncoder().encodeToString("hust_wanglin@163.com".getBytes("utf-8"));
        String encodedPwd = Base64.getEncoder().encodeToString("bestwishes10".getBytes("utf-8"));

        System.out.println("Client:" + "EHLO " + hostName);
        //和服务器会话，发送EHLO hostname命令
        outToServer.println("EHLO " + hostName);
        for(int i = 0; i < 7; i++){
            response = inFromServer.readLine();
            System.out.println("MailServer:" + response);
        }
        System.out.print("\n");
        System.out.print("Client:" + "AUTH LOGIN \n");
        //和服务器会话，发送AUTH LOGIN命令，请求身份验证
        outToServer.println("AUTH LOGIN ");

        //读入来自服务器的应答，并显示在屏幕上
        response = inFromServer.readLine();
        System.out.println("MailServer:" + response);

        System.out.print("Client:" + encodedUser + "\n");
        //向服务器发送自己的帐号
        outToServer.println(encodedUser);
        //读入来自服务器的应答，并显示在屏幕上
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");

        System.out.print("Client:" + encodedPwd + "\n");
        //向服务器发送自己的密码
        outToServer.println(encodedPwd);

        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
         //向服务器发送MAIL FROM: 发件人地址
        System.out.print("Client:" + "MAIL FROM: " + from + "\n");
        
        outToServer.println("MAIL FROM: <" + from + ">");
        //读入来自服务器的应答，并显示在屏幕上
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        //向服务器发送RCPT TO: 收件人地址
        System.out.print("Client:" + "RCPT TO: " + to1 +"<"+to1+">"+"\n");
        
        outToServer.println("RCPT TO: <" + to1 + ">" +" "+ to1);

        //读入来自服务器的应答，并显示在屏幕上
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        outToServer.println("RCPT TO: <" + to2 + ">" +" " + to2);

        //读入来自服务器的应答，并显示在屏幕上
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");

        //抄送
        //outToServer.println("Cc: " + cc );

        //读入来自服务器的应答，并显示在屏幕上
        //response = inFromServer.readLine();
        //System.out.print("MailServer:" + response + "\n");

        //请求发送邮件正文
        outToServer.println("DATA");
        //读入来自服务器的应答，并显示在屏幕上
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");

        //开始发送邮件正文
        outToServer.println(mailContent);
        //发送邮件结束标志
        outToServer.println(".");
        System.out.print("MailServer:" + response + "\n");

        //读入来自服务器的应答，并显示在屏幕上
        response = inFromServer.readLine();
        //关闭SOCKET
        s.close();
    }
}
