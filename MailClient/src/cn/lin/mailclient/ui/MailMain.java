package cn.lin.mailclient.ui;

/**
 * Created by strawberrylin on 17-4-15.
 */
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.lin.box.*;
import cn.lin.mailclient.object.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;

public class MailMain extends JFrame implements Runnable{
    private User user;
    private String mailServer;
    private String response ;
    private String hostName;
    private Socket s;
    private BufferedReader inFromServer;
    private PrintWriter outToServer;
    private String encoderUser;
    private String encoderPwd;
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
    private  MailBox box;
    //收件箱
    private List<Mail> receiveMails;
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
    //时间格式对象
    private DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    //事件
    private Action receive = new AbstractAction("刷新") {
        @Override
        public void actionPerformed(ActionEvent e) {
            mouseSelect();
        }
    };
    private Action send = new AbstractAction("发送") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(box instanceof DraftBox){
                resend();
            }
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
    private Action setup = new AbstractAction("设置") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    private Action downloadHead = new AbstractAction("下载邮件头部") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    private Action downloadAll = new AbstractAction("下载邮件全部") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    public User getUser() {
        return user;
    }

    public MailMain(User user){
        this.user = user;
        try{
            this.hostName = InetAddress.getLocalHost().getHostName();
        }catch (UnknownHostException e){
            e.printStackTrace();
        }

        //读取来自服务器的第一行应答，并显示在屏幕上
        response = "";
        //将用户的帐号和密码以BASE64格式进行编码
        //以便进行服务器身份验证
        try{
            encoderUser =Base64.getEncoder().encodeToString(user.getUserName().getBytes("utf-8"));
            encoderPwd = Base64.getEncoder().encodeToString(user.getPassWord().getBytes("utf-8"));
        }catch(IOException e){
            e.printStackTrace();
        }

        this.mailServer = "imap.163.com";
        this.response = "";
        System.out.print(user.getUserName() + user.getPassWord());
        this.mailFrame = new MailFrame(this);
        List<String> s = new ArrayList<String>();
        s.add("Hust.wanglin@gmail.com");

        Mail d = new Mail("xml","1340295481@qq.com",s,"Test2","2017-4-23","100",false,"Hello","unknow");
        this.currentMails = new ArrayList<Mail>();
        this.receiveMails = new ArrayList<Mail>();
        this.deleteMails = new ArrayList<Mail>();

        this.deleteMails.add(d);

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
        initListeners();
        //Timer timer = new Timer();
        //timer.schedule(new ReceiveTask(this), 10000, this.receiveInterval);
    }
    private void initListeners() {
        //列表选择监听器
        this.mailListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    if(mailListTable.getSelectedRowCount()!=1) {
                        return;
                    }
                    viewMail();
                }
            }
        });
    }
    private JTree newTree(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.add(new DefaultMutableTreeNode(new WriteBox()));
        root.add(new DefaultMutableTreeNode(new ReceiveBox()));
        root.add(new DefaultMutableTreeNode(new SentBox()));
        root.add(new DefaultMutableTreeNode(new DraftBox()));
        root.add(new DefaultMutableTreeNode(new DeletedBox()));

        JTree tree = new JTree(root);
        //加入鼠标监听器
        tree.addMouseListener(new TreeListener(this));
        //隐藏根节点
        tree.setRootVisible(false);
        //设置节点处理类
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
        this.toolBar.add(this.setup).setToolTipText("设置");
        this.toolBar.addSeparator(new Dimension(20, 0));
        //this.toolBar.add(this.transmit).setToolTipText("转发邮件");
        this.toolBar.add(this.delete).setToolTipText("删除邮件");
        this.toolBar.addSeparator(new Dimension(20, 0));
        this.toolBar.add(this.downloadHead).setToolTipText("下载邮件头部");
        this.toolBar.addSeparator(new Dimension(20, 0));
        this.toolBar.add(this.downloadAll).setToolTipText("下载全部");
        this.toolBar.addSeparator(new Dimension(20, 0));


        this.toolBar.addSeparator(new Dimension(50, 0));
        //this.toolBar.add(this.welcome);
        this.toolBar.setFloatable(false);//设置工具栏不可移动
        this.toolBar.setMargin(new Insets(5, 10, 5, 5));//设置工具栏的边距
        this.add(this.toolBar, BorderLayout.NORTH);
    }

    private void setTableView(){
        this.mailListTable.getColumn("ID").setMinWidth(0);
        this.mailListTable.getColumn("ID").setMinWidth(0);
        this.mailListTable.getColumn("ID").setMaxWidth(0);
        this.mailListTable.getColumn("发件人").setMinWidth(185);
        this.mailListTable.getColumn("收件人").setMinWidth(185);
        this.mailListTable.getColumn("主题").setMinWidth(150);
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
                v.add(m.getID());
                v.add(m.getReceiver());
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
        columns.add("ID");
        columns.add("收件人");
        columns.add("发件人");
        columns.add("主题");
        columns.add("日期");
        columns.add("大小");
        return columns;
    }
    private void write() {
        this.mailFrame.setVisible(true);
        this.mailFrame.setSize(850,600);
        this.mailFrame.setLocation(300,250);
    }

    private void resend() {
        JTextField receiverText = new JTextField(60);
        JTextField csText = new JTextField(60);
        JTextField subjectText = new JTextField(60);
        JTextArea textArea = new JTextArea(20,50);
        String tempt = "";
        Mail mail = getSelectMail();
        for(String t : mail.getReceiver()){
            if(tempt == ""){
                tempt = t;
            }else{
                tempt = tempt + "," + t;
            }
        }
        receiverText.setText(tempt);
        System.out.println(tempt);
        csText.setText("");
        subjectText.setText(mail.getSubject());
        textArea.setText(mail.getContent());
        System.out.println(mail.getContent());
        this.mailFrame.setReceiverText(receiverText);
        this.mailFrame.setCsText(csText);
        this.mailFrame.setSubjectText(subjectText);
        this.mailFrame.setTextArea(textArea);
        this.mailFrame.send();
    }
    private void delete(){

    }

    public  void mouseSelect(){
        TreePath treePath = this.tree.getSelectionPath();
        if(treePath != null){
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            box = (MailBox)treeNode.getUserObject();
            if(box instanceof WriteBox){
                write();
            }
            if(box instanceof ReceiveBox){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        receiveMails = getBoxData("INBOX");
                        currentMails = receiveMails;
                        refreshTable();
                        cleanMailInfo();
                    }
                }).start();
            }else if(box instanceof SentBox){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendedMails = getBoxData("&XfJT0ZAB-");
                        currentMails = sendedMails;
                        refreshTable();
                        cleanMailInfo();
                    }
                }).start();
            }else if(box instanceof DraftBox){
                System.out.println("childThread");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        draftMails = getBoxData("&g0l6P3ux-");
                        currentMails = draftMails;
                        refreshTable();
                        cleanMailInfo();
                    }
                }).start();
            }else if (box instanceof DeletedBox){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deleteMails = getBoxData("&XfJSIJZk-");
                        currentMails = deleteMails;
                        refreshTable();
                        cleanMailInfo();
                    }
                }).start();
            }
        }
    }

    public void refreshTable(){
        DefaultTableModel tableModle =(DefaultTableModel)this.mailListTable.getModel();
        tableModle.setDataVector(createDataView(this.currentMails),getListColumn());
        setTableView();
    }

    public List<Mail> getBoxData(String selectBox){
        String from ="";
        String subject = "";
        String date = "";
        String content = "";
        String id = "";
        String size ="";
        List<Mail> receiveMailsTemp = new ArrayList<>();

        int sumMail = 0;
        int start,end;
        Mail mailR;
        String mailNum;
        try{
            connect();
            outToServer.println("A01 LOGIN " + user.getUserName()+" "+user.getPassWord());
            response = inFromServer.readLine();
            outToServer.println("A02 SELECT " + selectBox);

            for(int i = 0;i < 6;i ++){
                response = inFromServer.readLine();
                if(i == 0){
                    String[] tempS = response.split(" ");
                    System.out.println(tempS[1]);
                    sumMail = Integer.parseInt(tempS[1]);
                }
            }
            for(int i = sumMail;i > 0;i -- ) {
                mailNum = String.valueOf(i);
                outToServer.println("A03 FETCH " + mailNum + " BODY[HEADER.FIELDS (FROM)]");
                from = getFromSub("From");

                outToServer.println("A04 FETCH " + mailNum + " BODY[HEADER.FIELDS (TO)]");
                List<String> to = new ArrayList<String>();
                to = getTo();

                outToServer.println("A05 FETCH " + mailNum + " BODY[HEADER.FIELDS (DATE)]");
                date = getDate("Date");

                outToServer.println("A06 FETCH " + mailNum + " BODY[HEADER.FIELDS (SUBJECT)]");
                subject = getFromSub("Subject");

                outToServer.println("A07 FETCH " + mailNum + " RFC822.SIZE");
                size = getUidSize("RFC822.SIZE");

                outToServer.println("A08 FETCH " + mailNum + " UID");
                id = getUidSize("UID");

                outToServer.println("A09 FETCH " + mailNum + " BODY[1]");
                content = getContent();
                receiveMailsTemp.add(new Mail(id,from,to,subject,date,size,true,content,selectBox));
            }
            s.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return receiveMailsTemp;

    }
    public void connect(){
        try{
            s = new Socket(mailServer,143);
            this.inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream(),"UTF-8"));
            //将SOCKET输出流连接到带缓冲功能的
            //输出流PrintWriter，以便一次输出一行报文到服务器
            this.outToServer = new PrintWriter(s.getOutputStream() ,true);
            this.response = inFromServer.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public String getFromSub(String title) {
        int start, end;
        String s = "";
        try {
            for (int j = 100; j > 0; j--) {
                response = inFromServer.readLine();
                System.out.println(response);
                if (response.contains(title)) {
                    String[] tempF = response.split(" ");
                    if (tempF.length > 2) {
                        start = tempF[2].indexOf("<") + 1;
                        if (tempF[2].lastIndexOf(">") != -1) {
                            end = tempF[2].lastIndexOf(">");
                        } else {
                            end = tempF[2].length();
                        }
                        s = tempF[2].substring(start, end);
                    } else if(tempF.length == 2){
                        start = tempF[1].indexOf("<") + 1;
                        if (tempF[1].lastIndexOf(">") != -1) {
                            end = tempF[1].lastIndexOf(">");
                        } else {
                            end = tempF[1].length();
                        }
                        s = tempF[1].substring(start, end);
                    }else{
                        s = "";
                    }
                }
                if (response.equals("")) {
                    j = 3;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return s;
    }
    private List<String> getTo(){
        List<String> r = new ArrayList<String>();
        int start, end;
        String s = "";
        try {
            for (int j = 100; j > 0; j--) {
                response = inFromServer.readLine();
                System.out.println(response);
                if (response.contains("To")) {
                    String[] tempF = response.split(" ");
                    if(tempF.length>2){
                        for(String x:tempF){
                            if(x.contains("<")&&x.contains(">")){
                                start=x.indexOf("<") + 1;
                                end = x.lastIndexOf(">");
                                s = x.substring(start,end);
                                r.add(s);
                            }
                        }
                    }
                    else{
                        start = tempF[1].indexOf("<") + 1;
                        if (tempF[1].lastIndexOf(">") != -1) {
                            end = tempF[1].lastIndexOf(">");
                        } else {
                            end = tempF[1].length();
                        }
                        r.add(tempF[1].substring(start,end));
                    }
                }
                if (response.equals("")) {
                    j = 3;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return r;
    }
    private String getDate(String title){
        int start, end;
        String s = "";
        try {
            for (int j = 100; j > 0; j--) {
                response = inFromServer.readLine();
                System.out.println(response);
                if (response.contains(title)) {
                    start =response.indexOf(":") + 1;
                    end = response.lastIndexOf("+");
                    s = response.substring(start, end);
                }
                if (response.equals("")) {
                    j = 3;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return s;
    }
    private String getUidSize(String para){
        int start, end;
        String s = "";
        try {
            for(int j = 100;j > 0;j --){
                response = inFromServer.readLine();
                System.out.println(response);
                if(response.contains(para)){
                    String[] temp = response.split(" ");
                    start =0;
                    end = temp[temp.length-1].length()-1;
                    s = temp[temp.length-1].substring(start, end);
                    j = 2;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return s;
    }
    private String getContent(){
        String s = "";
        try{
            for(int j = 0; j > -1;j ++){
                response = inFromServer.readLine();
                if(response.contains("completed")){
                    j = -2;
                    System.out.println("find com");
                }
                else{
                    if(j > 0){
                        s = s + response + "\n";
                    }
                }
            }
            System.out.println("exit loop");
        }catch (IOException e){
            e.printStackTrace();
        }
        return s;
    }

    private Mail getSelectMail(){
        String id;
        int row = this.mailListTable.getSelectedRow();
        int column = this.mailListTable.getColumn("ID").getModelIndex();
        if(row == -1){
            id = null;
        }
        id = (String)this.mailListTable.getValueAt(row,column);
        for(Mail m:this.currentMails){
            if(m.getID().equals(id)){
                return m;
            }
        }
        return null;
    }

    private void viewMail(){
        this.mailTextArea.setText("");
        Mail mail = getSelectMail();
        this.mailTextArea.append("发件人：  " + mail.getSender());
        this.mailTextArea.append("\n");
        //this.mailTextArea.append("抄送：  " + mail.getCsName());
        //this.mailTextArea.append("\n");
        this.mailTextArea.append("收件人:   " + mail.sendToName());
        this.mailTextArea.append("\n");
        this.mailTextArea.append("主题：  " + mail.getSubject());
        this.mailTextArea.append("\n");
        this.mailTextArea.append("接收日期：  " + mail.getReceiveDate());
        this.mailTextArea.append("\n\n");
        this.mailTextArea.append("邮件正文：  ");
        this.mailTextArea.append("\n\n");
        this.mailTextArea.append(mail.getContent());
        this.currentMail = mail;
    }
    //清空当前打开的邮件及对应的界面组件
    public void cleanMailInfo() {
        //设置当前打开的邮件对象为空
        this.currentMail = null;
        this.mailTextArea.setText("");
        //this.fileList.setListData(this.emptyListData);
    }
    public void run(){

    }
}
