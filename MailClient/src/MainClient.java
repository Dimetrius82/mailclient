/**
 * Created by strawberrylin on 17-4-15.
 */
import cn.lin.mailclient.ui.LoginModule;
import cn.lin.mailclient.ui.MailFrame;
import cn.lin.mailclient.ui.MailMain;

import static cn.lin.util.SwingConsole.*;
public class MainClient {
    //private static MailMain x = new MailMain();
    public static void main(String[] args){
        run(new LoginModule(),425,300);
    }
}
