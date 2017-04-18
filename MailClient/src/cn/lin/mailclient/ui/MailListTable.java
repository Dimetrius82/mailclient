package cn.lin.mailclient.ui;

/**
 * Created by strawberrylin on 17-4-15.
 * 邮件列表对象
 */
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.ListSelectionModel;
public class MailListTable extends JTable{
    public MailListTable(TableModel table){
        super(table);
        //只选则一行
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //去掉表格的线
        setShowHorizontalLines(false);
        setShowVerticalLines(false);
    }
    //使表不可编辑
    public boolean isCellEditable(int row,int column){
        return false;
    }
}
