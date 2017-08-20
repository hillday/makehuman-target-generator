
public class Target {
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
}
