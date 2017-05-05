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
        from = "hust_wanglin@163.com";
        to = "hust_wanglin@163.com";
        mailContent = "\n"+
                      "Hello Joe, do you think we can meet at 3:30 tomorrow?"+"\n\n"+
                      ")"+"\n\n"+
                      "."+"\n\n"+
                      ".";
        //建立一个到邮件服务器的连接，端口号143
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

        System.out.println("Client : " + "A02 append &g0l6P3ux- (\\draft) {310}");
        outToServer.println("A02 append &g0l6P3ux- (\\draft) {310}");
                            //"Date: Mon, 7 Feb 1994 21:52:25 -0800 (PST)"+"\n"+
                            //"From: hust_wanglin <hust_wanglin@163.com>" + "\n"+
                            //"Subject: afternoon meeting"+"\n"+
                            //"To: hust_wanglin@163.com"+"\n"+
                            //"Message-Id: B27397-0100000@163.com"+"\n"+
                            //"MIME-Version: 1.0" + "\n" +
                            //"Content-Type: TEXT/PLAIN; CHARSET=US-ASCII" + "\n" +
                            //mailContent
                            //);
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");   
        //outToServer.println("Date: Mon, 7 Feb 1994 21:52:25 -0800 (PST)");
        //outToServer.println("From: hust_wanglin <hust_wanglin@163.com>");
        //outToServer.println("Subject: afternoon meeting");
        //outToServer.println("To: hust_wanglin@163.com");
        //outToServer.println("Message-Id: B27397-0100000@163.com");
        //outToServer.println("MIME-Version: 1.0");
        //outToServer.println("Content-Type: TEXT/PLAIN; CHARSET=US-ASCII");
        //outToServer.println(mailContent);
        response = inFromServer.readLine();
        System.out.println("MailServer:" + response);
        //response = inFromServer.readLine();
        //System.out.println("MailServer:" + response);
        //关闭SOCKET
        s.close();
    }
}
