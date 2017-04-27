package cn.lin.box;

import javax.swing.*;

/**
 * Created by strawberrylin on 17-4-19.
 */
public class WriteBox extends AbstractBox {

    @Override
    public String getText() {
        return "写邮件";
    }

    public ImageIcon getImageIcon() {
        return super.getImageIcon("images/envelop-close.gif");
    }
}
