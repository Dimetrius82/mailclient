package cn.lin.mailclient.ui;

/**
 * Created by strawberrylin on 17-4-15.
 */
import cn.lin.mailclient.object.User;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import sun.misc.*;
import java.util.*;
import java.util.logging.SocketHandler;

import static cn.lin.util.SwingConsole.*;

public class LoginModule extends JFrame{
    private User user;
    private String mailServer;
    private String response ;
    private String hostName;
    private BufferedReader inFromServer;
    private PrintWriter outToServer;
    private String encoderUser;
    private String encoderPwd;
    private ImageIcon image = new ImageIcon("./images/envelop-close.gif");
    private JLabel pictureLabel = new JLabel(image);
    //用户名
    private JLabel userLabel = new JLabel("Username:");
    private JTextField userText = new JTextField(40);
    //密码
    private JLabel passwordLabel = new JLabel("Password:");
    private JPasswordField passwordText = new JPasswordField(40);
    //确认和取消
    private JButton confirmBtn = new JButton("Confirm");
    private JButton cancleBtn = new JButton("Cancle");
    //添加Box容器组件
    private Box pictureBox = Box.createVerticalBox();
    private Box userBox = Box.createHorizontalBox();
    private Box paswBox = Box.createHorizontalBox();
    private Box btnBox =  Box.createHorizontalBox();
    private Box mainBox = Box.createVerticalBox();


    public  LoginModule(){
        try{
            this.hostName = InetAddress.getLocalHost().getHostName();
        }catch(UnknownHostException e){
            e.printStackTrace();
        }

        this.mailServer = "smtp.163.com";
        this.response = "";
        this.setLayout(new FlowLayout());
        this.pictureBox.add(Box.createHorizontalStrut(30));
        this.pictureBox.add(pictureLabel);
        this.pictureBox.add(Box.createHorizontalStrut(30));
        //用户名
        this.userBox.add(Box.createHorizontalStrut(30));
        this.userBox.add(userLabel);
        this.userBox.add(Box.createHorizontalStrut(20));
        this.userBox.add(userText);
        this.userBox.add(Box.createHorizontalStrut(30));
        this.userBox.add(Box.createVerticalStrut(30));
        //密码
        this.paswBox.add(Box.createHorizontalStrut(30));
        this.paswBox.add(passwordLabel);
        this.paswBox.add(Box.createHorizontalStrut(20));
        this.paswBox.add(passwordText);
        this.paswBox.add(Box.createHorizontalStrut(30));
        this.paswBox.add(Box.createVerticalStrut(30));
        //按钮
        this.btnBox.add(Box.createHorizontalStrut(30));
        this.btnBox.add(this.confirmBtn);
        this.btnBox.add(Box.createHorizontalStrut(20));
        this.btnBox.add(this.cancleBtn);
        this.btnBox.add(Box.createHorizontalStrut(30));
        //主界面
        this.mainBox.add(Box.createVerticalStrut(30));
        this.mainBox.add(this.pictureBox);
        this.mainBox.add(Box.createVerticalStrut(30));
        this.mainBox.add(this.userBox);
        this.mainBox.add(Box.createVerticalStrut(30));
        this.mainBox.add(this.paswBox);
        this.mainBox.add(Box.createVerticalStrut(30));
        this.mainBox.add(this.btnBox);
        this.mainBox.add(Box.createVerticalStrut(50));
        this.add(mainBox);
        this.setLocation(300,200);
        this.pack();

        initialListener();
    }
    private void initialListener(){
        this.cancleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirm();
            }
        });
    }
    private void confirm(){
        user = new User(this.userText.getText(),this.passwordText.getText());
        if(user.getUserName().equals("")){
            JOptionPane.showConfirmDialog(this,"请输入用户名","警告",JOptionPane.OK_CANCEL_OPTION);
            return;
        }
        try{
            Socket s = new Socket(mailServer,25);
            inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
            outToServer = new PrintWriter(s.getOutputStream(),true);
            this.response =inFromServer.readLine();
            encoderUser =Base64.getEncoder().encodeToString(user.getUserName().getBytes("utf-8"));
            encoderPwd = Base64.getEncoder().encodeToString(user.getPassWord().getBytes("utf-8"));
            outToServer.println("EHLO " + hostName);
            for(int i = 0; i < 7; i++){
                response = inFromServer.readLine();
            }
            outToServer.println("AUTH LOGIN ");
            //读入来自服务器的应答，并显示在屏幕上
            response = inFromServer.readLine();
            outToServer.println(encoderUser);
            //读入来自服务器的应答，并显示在屏幕上
            response = inFromServer.readLine();
            outToServer.println(encoderPwd);
            response = inFromServer.readLine();
            if(response.contains("235 Authentication successful")){
                File file = new File("data"+File.separator+user.getUserName());
                if(!file.exists()){
                    file.mkdir();
                }
                String fileName = user.getUserName()+".log";
                File configFile = new File(file.getAbsolutePath()+File.separator+fileName);
                try{
                    if(!configFile.exists()){
                        configFile.createNewFile();
                    }
                    String data = String.valueOf(new Date());
                    String user = this.user.getUserName();
                    String pass = this.user.getPassWord();
                    System.out.println(configFile.getAbsoluteFile());
                    FileWriter fileWrite = new FileWriter(configFile.getAbsoluteFile(),true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);
                    System.out.println(data + "\n"+user+"\n"+pass);
                    bufferedWriter.write(data + "\n"+user+"\n"+pass);
                    bufferedWriter.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

                this.setVisible(false);
                s.close();
                run(new MailMain(user),425,300);
            }
            else{
                JOptionPane.showConfirmDialog(this,"登陆失败","错误",JOptionPane.OK_CANCEL_OPTION);
                this.userText.setText("");
                this.passwordText.setText("");
                return;
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
