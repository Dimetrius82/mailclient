package cn.lin.mailclient.object;

/*附件对象
 * Created by strawberrylin on 17-4-18.
 */
import java.io.File;
public class FileObject {
    private String name;
    private File file;

    public FileObject(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileObject{" +
                "name='" + name + '\'' +
                '}';
    }
}
