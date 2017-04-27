import java.io.*;
import java.net.*;
import sun.misc.*;
import java.util.*;
public class ReceiveMail {
    public static void main(String[] args) throws Exception{

        String mailContent = "";  //邮件报文
        String response = "";     //来自服务器的应答
        String mailServer = "";   //邮件服务器
        String from = "";         //发件人地址
        String to = "";           //收件人地址
        //设置邮件服务器、发件人地址、收件人地址
        mailServer = "imap.163.com";
        //得到本机主机名
        String hostName = InetAddress.getLocalHost().getHostName();
        //建立一个到邮件服务器的连接，端口号25
        Socket s = new Socket(mailServer,143);
        //将SOCKET输入流连接到带缓冲功能的
        //输入流BufferedReader，以便一次读一行来自
        //服务器的应答报文
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream(),"UTF-8"));
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

        System.out.println("Client:" + "A01 LOGIN hust_wanglin bestwishes1010");
        outToServer.println("A01 LOGIN hust_wanglin bestwishes10");
        //读入来自服务器的应答，并显示在屏幕上
        response = inFromServer.readLine();
        System.out.println("MailServer:" + response);

        //测试
        outToServer.println("A01 LIST \"\" * ");

        //读入来自服务器的应答，并显示在屏幕上
        for(int i = 0;i < 9;i ++){
            response = inFromServer.readLine();
            System.out.print("MailServer:" + response + "\n");   
        }
        outToServer.println("A02 SELECT INBOX");
        for(int i = 0;i < 6;i ++){
            response = inFromServer.readLine();
            System.out.print("MailServer:" + response + "\n");   
        }
        outToServer.println("A03 FETCH 19 BODY[HEADER.FIELDS (FROM)]");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");   
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine(); System.out.print("MailServer:" + response + "\n"); 
        outToServer.println("A04 FETCH 19 BODY[HEADER.FIELDS (TO)]");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        outToServer.println("A05 FETCH 19 BODY[HEADER.FIELDS (DATE)]");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        outToServer.println("A06 FETCH 19 BODY[HEADER.FIELDS (SUBJECT)]");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        outToServer.println("A07 FETCH 19 BODY[1]");
        s.shutdownOutput();
        String c ="";
        while((response = inFromServer.readLine())!= null){
            c = c + response;
            System.out.print("MailServer:" + response + "\n");
        }
        System.out.println(c);
        //关闭SOCKET
        s.close();
    }
}
