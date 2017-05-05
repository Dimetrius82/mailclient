package cn.lin.mailclient.ui;

import cn.lin.mailclient.object.Mail;
import cn.lin.mailclient.object.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Base64;

import static cn.lin.util.SwingConsole.*;
import static com.sun.javafx.geom.BaseBounds.BoundsType.BOX;

/**
 * Created by strawberrylin on 17-5-4.
 */
public class Setup extends JFrame {
    private MailMain mailMain;
    //用户名
    private JLabel loginLabel ;
    private JTextField loginText ;
    //密码
    private JLabel passwordLabel ;
    private JPasswordField passwordText ;
    private JLabel userLable ;
    private JLabel pactrolLabel1 ;
    private JLabel pactrolLabel2 ;
    private Choice userC;
    private Choice pactrolC1;
    private Choice pactrolC2;
    private Box mainBox = Box.createVerticalBox();
    private Box userBox = Box.createHorizontalBox();
    private Box pactrolBox1 = Box.createHorizontalBox();
    private Box pactrolBox2 = Box.createHorizontalBox();
    //确认和取消
    private JButton confirmBtn = new JButton("Confirm");
    private JButton cancleBtn = new JButton("Cancle");
    private Box btnBox =  Box.createHorizontalBox();
    private Box loginBox = Box.createHorizontalBox();
    private Box paswBox = Box.createHorizontalBox();

    private BufferedReader inFromServer;
    private PrintWriter outToServer;
    private String response ;
    private String encoderUser;
    private String encoderPwd;
    private String hostName;

    public Setup(MailMain mailMain){
        this.setLayout(new FlowLayout());
        loginLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField(40);
        loginText = new JTextField(40);
        userLable = new JLabel("帐号");
        pactrolLabel1 = new JLabel("接收");
        pactrolLabel2 = new JLabel("发送");
        userC = new Choice();
        pactrolC1 = new Choice();
        pactrolC2 = new Choice();
        pactrolC1.add("IMAP");
        pactrolC1.add("POP3");
        pactrolC2.add("SMTP");

        this.mailMain = mailMain;

        this.userBox.add(Box.createHorizontalStrut(10));
        this.userBox.add(userLable);
        this.userBox.add(Box.createHorizontalStrut(5));
        this.userBox.add(userC);
        this.userBox.add(Box.createHorizontalStrut(10));

        this.pactrolBox1.add(Box.createHorizontalStrut(10));
        this.pactrolBox1.add(pactrolLabel1);
        this.pactrolBox1.add(Box.createHorizontalStrut(5));
        this.pactrolBox1.add(pactrolC1);
        this.pactrolBox1.add(Box.createHorizontalStrut(10));

        this.pactrolBox2.add(Box.createHorizontalStrut(10));
        this.pactrolBox2.add(pactrolLabel2);
        this.pactrolBox2.add(Box.createHorizontalStrut(5));
        this.pactrolBox2.add(pactrolC2);
        this.pactrolBox2.add(Box.createHorizontalStrut(10));

        //用户名
        this.loginBox.add(Box.createHorizontalStrut(30));
        this.loginBox.add(loginLabel);
        this.loginBox.add(Box.createHorizontalStrut(20));
        this.loginBox.add(loginText);
        this.loginBox.add(Box.createHorizontalStrut(30));
        //密码
        this.paswBox.add(Box.createHorizontalStrut(30));
        this.paswBox.add(passwordLabel);
        this.paswBox.add(Box.createHorizontalStrut(20));
        this.paswBox.add(passwordText);
        this.paswBox.add(Box.createHorizontalStrut(30));
        //按钮
        this.btnBox.add(Box.createHorizontalStrut(30));
        this.btnBox.add(this.confirmBtn);
        this.btnBox.add(Box.createHorizontalStrut(20));
        this.btnBox.add(this.cancleBtn);
        this.btnBox.add(Box.createHorizontalStrut(30));

        this.mainBox.add(Box.createVerticalStrut(50));
        this.mainBox.add(this.userBox);
        this.mainBox.add(Box.createVerticalStrut(30));
        this.mainBox.add(this.loginBox);
        this.mainBox.add(Box.createVerticalStrut(30));
        this.mainBox.add(this.paswBox);
        this.mainBox.add(Box.createVerticalStrut(30));
        this.mainBox.add(this.pactrolBox1);
        this.mainBox.add(Box.createVerticalStrut(30));
        this.mainBox.add(this.pactrolBox2);
        this.mainBox.add(Box.createVerticalStrut(30));

        this.mainBox.add(this.btnBox);
        this.mainBox.add(Box.createVerticalStrut(50));
        this.add(mainBox);
        this.setSize(400,400);
        getChoise();
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
        if(loginText.getText().equals("")){
            String folder = userC.getSelectedItem();
            String path = "data"+ File.separator+folder+File.separator+folder+".log";
            readFile(path);
        }else{
            login();
        }
        System.out.println(pactrolC1.getSelectedItem());
        System.out.println(pactrolC2.getSelectedItem());
        this.setVisible(false);
    }
    private void readFile(String filePath){
        try{
            StringBuffer sb = new StringBuffer("");
            FileReader reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            while((str = br.readLine())!=null){
                if(str.contains("password")){
                    String[] temp = str.split(" ");
                    this.mailMain.getUser().setUserName(this.userC.getSelectedItem());
                    this.mailMain .getUser().setPassWord(temp[1]);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void login(){
        User user = new User(this.loginText.getText(),this.passwordText.getText());
        if(user.getUserName().equals("")){
            JOptionPane.showConfirmDialog(this,"请输入用户名","警告",JOptionPane.OK_CANCEL_OPTION);
            return;
        }
        try{
            Socket s = new Socket(this.pactrolC2.getSelectedItem()+".163.com",25);
            inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
            outToServer = new PrintWriter(s.getOutputStream(),true);
            this.response =inFromServer.readLine();
            encoderUser = Base64.getEncoder().encodeToString(user.getUserName().getBytes("utf-8"));
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
            System.out.println(response);
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
                    String userName = user.getUserName();
                    String passWord = user.getPassWord();
                    System.out.println(configFile.getAbsoluteFile());
                    FileWriter fileWrite = new FileWriter(configFile.getAbsoluteFile());
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);
                    System.out.println("user: "+userName+"\n"+passWord);
                    bufferedWriter.write("user: "+userName+"\n"+"password: "+passWord+"\n");
                    bufferedWriter.close();
                    this.mailMain.getUser().setUserName(userName);
                    this.mailMain.getUser().setPassWord(passWord);
                }catch (IOException e){
                    e.printStackTrace();
                }

                this.setVisible(false);
                s.close();
            }
            else{
                JOptionPane.showConfirmDialog(this,"登陆失败","错误",JOptionPane.OK_CANCEL_OPTION);
                this.loginText.setText("");
                this.passwordText.setText("");
                return;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void getChoise(){
        File file = new File("data");
        if(file.isDirectory()){
            String[] fileList = file.list();
            for(String s : fileList){
                userC.add(s);
            }
        }
    }
}
