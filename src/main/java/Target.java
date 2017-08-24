import java.io.Serializable;

public class Target implements Serializable,Comparable{
    private static final  long  serialVersionUID = 1L;

    private String path;
    private short[] targetData;

    public Target(){

    }

    public Target(String path,short[] targetData){
        this.path = path;
        this.targetData = targetData;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public short[] getTargetData() {
        return targetData;
    }

    public void setTargetData(short[] targetData) {
        this.targetData = targetData;
    }

    @Override
    public int compareTo(Object o) {

        Target targetObj = (Target)o;
        String otherPath = targetObj.getPath();

        return this.path.compareTo(otherPath);
    }
}
