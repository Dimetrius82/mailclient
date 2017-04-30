package cn.lin.mailclient.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
}
