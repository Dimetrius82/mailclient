package cn.lin.mailclient.ui;

/**
 * Created by strawberrylin on 17-4-15.
 */
import javax.swing.*;
import java.awt.*;

public class LoginModule extends JFrame{
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
        this.mainBox.add(this.mainBox.createVerticalStrut(30));
        this.mainBox.add(this.pictureBox);
        this.mainBox.add(this.mainBox.createVerticalStrut(30));
        this.mainBox.add(this.userBox);
        this.mainBox.add(this.mainBox.createVerticalStrut(30));
        this.mainBox.add(this.paswBox);
        this.mainBox.add(this.mainBox.createVerticalStrut(30));
        this.mainBox.add(this.btnBox);
        this.mainBox.add(this.mainBox.createVerticalStrut(50));
        this.add(mainBox);
    }
}
