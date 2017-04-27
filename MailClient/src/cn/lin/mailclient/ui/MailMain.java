package cn.lin.mailclient.ui;

/**
 * Created by strawberrylin on 17-4-15.
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;

import cn.lin.box.*;
import cn.lin.mailclient.object.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class MailMain extends JFrame{
    private User user;
    private JSplitPane mailSplitPane;
    private JSplitPane mailListInfoPane;
    private JSplitPane mailInfoPane;
    private MailListTable mailListTable;
    private JScrollPane tablePane;
    private JScrollPane treePane;
    private JTree tree;
    private JTextArea mailTextArea = new JTextArea(10,80);
    private JScrollPane mailScrollPane;
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
    //写邮件
    private MailFrame mailFrame;
    //系统设置界面对象
    //private SetupFrame setupFrame;
    //邮箱加载对象
    //private MailLoader mailLoader = new MailLoaderImpl();
    //本地中的邮件处理对象
    //private SystemHandler systemHandler = new SystemHandlerImpl();
    //本地中的邮件加载对象
    //private SystemLoader systemLoader = new SystemLoaderImpl();
    //发送邮件对象
    //private MailSender mailSender = new MailSenderImpl();
    //当前打开的文件对象
    private Mail currentMail;
    //接收邮件的间隔, 单位毫秒
    private long receiveInterval = 1000 * 10;
    //事件
    private Action receive = new AbstractAction("刷新") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    private Action send = new AbstractAction("发送") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    private  Action write = new AbstractAction("写邮件") {
        @Override
        public void actionPerformed(ActionEvent e) {
            write();
        }
    };
    private Action reply = new AbstractAction("回复邮件") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    private Action delete = new AbstractAction("删除邮件") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    public MailMain(User user){
        this.user = user;
        System.out.print(user.getUserName() + user.getPassWord());
        this.mailFrame = new MailFrame(this);
        List<String> s = new ArrayList<String>();
        s.add("Hust.wanglin@gmail.com");
        Mail m = new Mail("xml","424435985@qq.com",s,"Test1",new Date(),"100",false,"Hello","unknow");
        Mail d = new Mail("xml","1340295481@qq.com",s,"Test2",new Date(),"100",false,"Hello","unknow");
        this.currentMails = new ArrayList<Mail>();
        this.receiveMails = new ArrayList<Mail>();
        this.deleteMails = new ArrayList<Mail>();
        this.receiveMails.add(m);
        this.deleteMails.add(d);
        System.out.println(m.getSender()+m.getReceiver());
        //this.currentMails = this.receiveMails;
        this.tree = newTree();

        DefaultTableModel tableMode = new DefaultTableModel();
        this.mailListTable = new MailListTable(tableMode);
        tableMode.setDataVector(createDataView(this.currentMails), getListColumn());
        //设置邮件列表的样式
        setTableView();
        this.tablePane = new JScrollPane(this.mailListTable);
        this.tablePane.setBackground(Color.WHITE);
        //邮件附件列表
        this.fileList = new JList();
        this.fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //this.fileList.addMouseListener(new MainListMouseListener());
        this.filePane = new JScrollPane(fileList);
        this.mailTextArea.setLineWrap(true);
        this.mailTextArea.setEditable(false);
        this.mailTextArea.setFont(new Font(null, Font.BOLD, 14));
        //显示邮件内容的JScrollPane
        this.mailScrollPane =  new JScrollPane(this.mailTextArea);
        //邮件的信息
        this.mailInfoPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                this.filePane, this.mailScrollPane);
        this.mailInfoPane.setDividerSize(3);
        this.mailInfoPane.setDividerLocation(80);
        //邮件列表和邮件信息的JSplitPane
        this.mailListInfoPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                this.tablePane, mailInfoPane);
        this.mailListInfoPane.setDividerLocation(400);
        this.mailListInfoPane.setDividerSize(20);

        //树的JScrollPane
        this.treePane = new JScrollPane(this.tree);
        //主整个邮件界面的JSplitPane
        this.mailSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                this.treePane, this.mailListInfoPane);
        this.mailSplitPane.setDividerLocation(150);
        this.mailSplitPane.setDividerSize(3);
        //设置用户邮箱地址的显示
        //this.welcome.setText(this.welcome.getText() + ctx.getUser());
        //创建工具栏
        createToolBar();
        //设置JFrame的各个属性
        this.add(mailSplitPane);
        this.setTitle("邮件收发客户端");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //initListeners();
        //Timer timer = new Timer();
        //timer.schedule(new ReceiveTask(this), 10000, this.receiveInterval);
    }

    private JTree newTree(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.add(new DefaultMutableTreeNode(new WriteBox()));
        root.add(new DefaultMutableTreeNode(new ReceiveBox()));
        root.add(new DefaultMutableTreeNode(new SendBox()));
        root.add(new DefaultMutableTreeNode(new SentBox()));
        root.add(new DefaultMutableTreeNode(new DraftBox()));
        root.add(new DefaultMutableTreeNode(new DeletedBox()));

        JTree tree = new JTree(root);
        //加入鼠标监听器
        tree.addMouseListener(new TreeListener(this));
        //隐藏根节点
        tree.setRootVisible(false);
        //设置节点处理类
        //SailTreeCellRenderer cellRenderer = new SailTreeCellRenderer();
        //tree.setCellRenderer(cellRenderer);
        return tree;
    }

    private void createToolBar() {
        this.toolBar.add(this.receive).setToolTipText("刷新");
        this.toolBar.addSeparator(new Dimension(20, 0));
        this.toolBar.add(this.send).setToolTipText("发送");
        this.toolBar.addSeparator(new Dimension(20, 0));
        this.toolBar.add(this.write).setToolTipText("写邮件");
        this.toolBar.addSeparator(new Dimension(20, 0));
        this.toolBar.add(this.reply).setToolTipText("回复邮件");
        this.toolBar.addSeparator(new Dimension(20, 0));
        //this.toolBar.add(this.transmit).setToolTipText("转发邮件");
        this.toolBar.add(this.delete).setToolTipText("删除邮件");
        //this.toolBar.add(this.realDelete).setToolTipText("彻底删除邮件");
        //this.toolBar.add(this.revert).setToolTipText("还原邮件");
        this.toolBar.addSeparator(new Dimension(20, 0));
        //this.toolBar.add(this.setup).setToolTipText("设置");

        this.toolBar.addSeparator(new Dimension(50, 0));
        //this.toolBar.add(this.welcome);
        this.toolBar.setFloatable(false);//设置工具栏不可移动
        this.toolBar.setMargin(new Insets(5, 10, 5, 5));//设置工具栏的边距
        this.add(this.toolBar, BorderLayout.NORTH);
    }

    private void setTableView(){
        this.mailListTable.getColumn("xmlName").setMinWidth(0);
        this.mailListTable.getColumn("xmlName").setMinWidth(0);
        this.mailListTable.getColumn("xmlName").setMaxWidth(0);
        //this.mailListTable.getColumn("打开").setCellRenderer(new MailTableCellRenderer());
        //this.mailListTable.getColumn("打开").setMaxWidth(40);
        this.mailListTable.getColumn("发件人").setMinWidth(200);
        this.mailListTable.getColumn("主题").setMinWidth(320);
        this.mailListTable.getColumn("日期").setMinWidth(130);
        this.mailListTable.getColumn("大小").setMinWidth(80);
        this.mailListTable.setRowHeight(30);
    }


    //将邮件数据转化为视图
    @SuppressWarnings("unchecked")
    private Vector <Vector> createDataView(List<Mail> mails){
        Vector<Vector> view = new Vector<Vector>();
        if(mails != null){
            for(Mail m : mails){
                Vector v = new Vector();
                v.add(m.getXmlName());
                v.add(m.getSender());
                v.add(m.getSubject());
                v.add(m.getReceiveDate());
                v.add(m.getSize() + "K");
                view.add(v);
            }
        }
        return view;
    }
    //获得邮件列表的列名
    @SuppressWarnings("unchecked")
    private Vector getListColumn() {
        Vector columns = new Vector();
        columns.add("xmlName");
        //columns.add("打开");
        columns.add("发件人");
        columns.add("主题");
        columns.add("日期");
        columns.add("大小");
        return columns;
    }
    private void write() {
        this.mailFrame.setVisible(true);
    }

    public  void mouseSelect(){
        MailBox box;
        TreePath treePath = this.tree.getSelectionPath();
        if(treePath != null){
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            box = (MailBox)treeNode.getUserObject();
            if(box instanceof ReceiveBox){
                this.currentMails = this.receiveMails;
            }else if(box instanceof SendBox){
                this.currentMails = sendMails;
            }else if(box instanceof SentBox){
                this.currentMails = sendedMails;
            }else if(box instanceof DraftBox){
                this.currentMails = draftMails;
            }else if (box instanceof DeletedBox){
                this.currentMails = deleteMails;
            }
            refreshTable();
        }
    }

    public void refreshTable(){
        DefaultTableModel tableModle =(DefaultTableModel)this.mailListTable.getModel();
        tableModle.setDataVector(createDataView(this.currentMails),getListColumn());
        setTableView();
    }
}
