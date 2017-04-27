package cn.lin.box;

import javax.swing.*;

/**
 * Created by strawberrylin on 17-4-19.
 */
public class DeletedBox extends AbstractBox {

    private ImageIcon icon;

    public String getText() {
        return "垃圾箱";
    }

    public ImageIcon getImageIcon() {
        return super.getImageIcon("images/envelop-close.gif");
    }

}
