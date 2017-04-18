package cn.lin.mailclient.ui;

/**
 * Created by strawberrylin on 17-4-15.
 */
import javax.swing.*;
import java.awt.*;
import cn.lin.mailclient.object.*;
public class MailMain {
    private JSplitPane mailSplitPane;
    private JSplitPane mailListPane;
    private JSplitPane mainInfo;
    private MailListTable mailListTable;
    private JScrollPane tablePane;
    private JScrollPane treePane;
    private JTree tree;
    private JTextArea mailTextArea = new JTextArea(10,80);
    private JScrollPane mailSrollPane;
    private JScrollPane filePane;
    private JList fileList;
    private JToolBar toolBar = new JToolBar();
    //收件箱
    private List<Mail> receiveMails;
    //发件箱
    private List<Mail> sendMails;
    //已发送邮件
    private List<Mail> sendedMails;
    //草稿箱
    private List<Mail> draftMails;
    //已删除邮件
    private List<Mail> deleteMails;
    //当前显示邮件
    private List<Mail> currentMails;

    //
}
