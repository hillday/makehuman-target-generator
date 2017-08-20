
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TargetBinFilter {
    private String targetsMeta;
    private List<Target> targets;
    private int shortSize = 0;
    private List<String> targetsKeys;

    public TargetBinFilter(String targetsMeta, List<Target> targets) {
        this.targets = targets;
        this.targetsMeta = targetsMeta;
        targetsKeys = new ArrayList<String>();

        init();
    }

    private void init() {
        JSONObject obj = JSONObject.fromObject(this.targetsMeta);
        JSONObject groupObj = obj.getJSONObject("targets");

        Iterator keys = groupObj.keys();

        int i = 0;
        while (keys.hasNext()) {
            String key = keys.next().toString();
            targetsKeys.add(key);
            i++;
        }

        this.shortSize = i * 19158 * 3;
    }

    public short[] filter() {
        short[] targetShorts = new short[this.shortSize];
        int si = 0;
        for (int i = 0; i < this.targets.size(); i++){
            if(this.targetsKeys.contains(this.targets.get(i).getPath())){
               for(int j = 0; j < this.targets.get(i).getTargetData().length; j++){
                   targetShorts[si] = this.targets.get(i).getTargetData()[j];
                   si++;
               }
            }
        }

        return targetShorts;
    }


}
