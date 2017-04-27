package cn.lin.mailclient.ui;

import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by strawberrylin on 17-4-22.
 */
public class TreeListener extends MouseAdapter{
    private MailMain mailMain;
    public TreeListener(MailMain mailMail){
        this.mailMain = mailMail;
    }
    public void mousePressed(MouseEvent e){
        mailMain.mouseSelect();
    }
}
