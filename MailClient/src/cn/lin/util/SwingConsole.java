package cn.lin.util;

/**
 * Created by strawberrylin on 17-4-15.
 */
import javax.swing.*;

public class SwingConsole{
    public static void run(final JFrame f,final int width,final int height){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                f.setTitle(f.getClass().getSimpleName());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(width,height);
                f.setLocation(700,400);
                f.setVisible(true);
            }
        });
    }
}
