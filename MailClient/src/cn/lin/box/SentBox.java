package cn.lin.box;

import javax.swing.*;

/**
 * Created by strawberrylin on 17-4-19.
 */
public class SentBox extends AbstractBox {

    @Override
    public String getText() {
        return "已发送";
    }

    public ImageIcon getImageIcon() {
        return super.getImageIcon("images/envelop-close.gif");
    }
}
