import java.io.*;
import java.net.*;
import sun.misc.*;
import java.util.*;
public class SendMail {
    public static void main(String[] args) throws Exception{

        String mailContent = "";  //�ʼ�����
        String response = "";     //���Է�������Ӧ��
        String mailServer = "";   //�ʼ�������
        String from = "";         //�����˵�ַ
        String to1 = "";           //�ռ��˵�ַ
        String to2 = "";
        String cc = "";
        //�����ʼ��������������˵�ַ���ռ��˵�ַ
        mailServer = "smtp.163.com";
        from = "hust_wanglin@163.com";
        to1 = "hust_wanglin@163.com";
        to2 = "15888797753@163.com";
        cc = "424435985@qq.com";
        //�����ʼ�����
        mailContent =
                "From: " + from + "\n" +
                "To: " + to1 + to2 + "\n" +
                "Subject: " + "Hello" + "\n" +
                "Content-Type: "+"text/html"+"\n\n" +
                "<div style=\"line-height:1.7;color:#000000;font-size:14px;font-family:Arial\">This is an email to test the draftbox for desktop.<br></div>)\n";
        //�õ�����������
        String hostName = InetAddress.getLocalHost().getHostName();
        //����һ�����ʼ������������ӣ��˿ں�25
        Socket s = new Socket(mailServer,25);
        //��SOCKET���������ӵ������幦�ܵ�
        //������BufferedReader���Ա�һ�ζ�һ ������
        //��������Ӧ����
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
        //��SOCKET��������ӵ������幦�ܵ�
        //�����PrintWriter���Ա�һ�����һ�б��ĵ�������
        PrintWriter outToServer = new PrintWriter(s.getOutputStream() ,true);

        //��ȡ���Է������ĵ�һ��Ӧ�𣬲���ʾ����Ļ��
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        //���û����ʺź�������BASE64��ʽ���б���
        //�Ա���з����������֤
        String encodedUser = Base64.getEncoder().encodeToString("hust_wanglin@163.com".getBytes("utf-8"));
        String encodedPwd = Base64.getEncoder().encodeToString("bestwishes10".getBytes("utf-8"));

        System.out.println("Client:" + "EHLO " + hostName);
        //�ͷ������Ự������EHLO hostname����
        outToServer.println("EHLO " + hostName);
        for(int i = 0; i < 7; i++){
            response = inFromServer.readLine();
            System.out.println("MailServer:" + response);
        }
        System.out.print("\n");
        System.out.print("Client:" + "AUTH LOGIN \n");
        //�ͷ������Ự������AUTH LOGIN������������֤
        outToServer.println("AUTH LOGIN ");

        //�������Է�������Ӧ�𣬲���ʾ����Ļ��
        response = inFromServer.readLine();
        System.out.println("MailServer:" + response);

        System.out.print("Client:" + encodedUser + "\n");
        //������������Լ����ʺ�
        outToServer.println(encodedUser);
        //�������Է�������Ӧ�𣬲���ʾ����Ļ��
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");

        System.out.print("Client:" + encodedPwd + "\n");
        //������������Լ�������
        outToServer.println(encodedPwd);

        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
         //�����������MAIL FROM: �����˵�ַ
        System.out.print("Client:" + "MAIL FROM: " + from + "\n");
        
        outToServer.println("MAIL FROM: <" + from + ">");
        //�������Է�������Ӧ�𣬲���ʾ����Ļ��
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        //�����������RCPT TO: �ռ��˵�ַ
        System.out.print("Client:" + "RCPT TO: " + to1 +"<"+to1+">"+"\n");
        
        outToServer.println("RCPT TO: <" + to1 + ">" +" "+ to1);

        //�������Է�������Ӧ�𣬲���ʾ����Ļ��
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");
        outToServer.println("RCPT TO: <" + to2 + ">" +" " + to2);

        //�������Է�������Ӧ�𣬲���ʾ����Ļ��
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");

        //����
        //outToServer.println("Cc: " + cc );

        //�������Է�������Ӧ�𣬲���ʾ����Ļ��
        //response = inFromServer.readLine();
        //System.out.print("MailServer:" + response + "\n");

        //�������ʼ�����
        outToServer.println("DATA");
        //�������Է�������Ӧ�𣬲���ʾ����Ļ��
        response = inFromServer.readLine();
        System.out.print("MailServer:" + response + "\n");

        //��ʼ�����ʼ�����
        outToServer.println(mailContent);
        //�����ʼ�������־
        outToServer.println(".");
        System.out.print("MailServer:" + response + "\n");

        //�������Է�������Ӧ�𣬲���ʾ����Ļ��
        response = inFromServer.readLine();
        //�ر�SOCKET
        s.close();
    }
}
