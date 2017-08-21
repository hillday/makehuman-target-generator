
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
    
    private Target getTargetByPath(String path){
    	Target target = null;
    	
    	for(int i = 0; i < this.targets.size(); i++){
    		if(this.targets.get(i).getPath().equalsIgnoreCase(path)){
    			target = this.targets.get(i);
    			break;
    		}
    			
    	}
    	
    	return target;
    }

    public short[] filter() {
        short[] targetShorts = new short[this.shortSize];
        int si = 0;
        
        for(int i = 0; i < this.targetsKeys.size(); i++){
        	Target target = this.getTargetByPath(this.targetsKeys.get(i));
        	if(target == null)
        		continue;
        	for(int j = 0; j < target.getTargetData().length; j++){
                targetShorts[si] = target.getTargetData()[j];
                si++;
            }
        }

        return targetShorts;
    }


}
