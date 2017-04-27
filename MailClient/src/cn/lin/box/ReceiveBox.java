package cn.lin.box;

import javax.swing.*;

/**
 * Created by strawberrylin on 17-4-19.
 */
public class ReceiveBox extends AbstractBox{
    public String getText() {
        return "收件箱";
    }
    public ImageIcon getImageIcon() {
        return super.getImageIcon("images/envelop-close.gif");
    }
}
