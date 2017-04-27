package cn.lin.box;

import javax.swing.*;

/**
 * Created by strawberrylin on 17-4-19.
 */
public class DraftBox extends AbstractBox {

    @Override
    public String getText() {
        return "草稿箱";
    }

    public ImageIcon getImageIcon() {
        return super.getImageIcon("images/envelop-close.gif");
    }
}