

import net.sf.json.JSONObject;

import java.util.*;

public class TargerListFilter extends ModifierFilter {
    public TargerListFilter(String config, String sourceFilter, String targetFile) {
        super(config, sourceFilter, targetFile);
    }

    public void filter() {
        JSONObject sourceObj = this.getSourceObj();

        Iterator keys = sourceObj.keys();

        while(keys.hasNext()) {
            String key = keys.next().toString();

            filterObjByTag(sourceObj,key);
        }


        this.targetContent =  sourceObj.toString();
        this.write();
    }

    private int getCharCountInString(String s,String d){
        int count = 0;

        for(int i=0;i<=s.length()-1;i++) {
            String subString=s.substring(i,i+1);
            if(subString.equals(d)){
                count++;
            }
        }

        return count;
    }

    private void filterObjByTag(JSONObject obj,String tag){
        JSONObject groupObj = obj.getJSONObject(tag);

        Iterator keys = groupObj.keys();
        Map<String,String> addKeys = new HashMap<String, String>();

        while(keys.hasNext()) {
            String key = keys.next().toString();
            String value = groupObj.optString(key);

            for(String configTarget : this.configTargetKeys) {
                if (key.indexOf(configTarget) != -1) {
                    addKeys.put(key, value);
                    continue;
                }
            }
        }

        obj.put(tag,addKeys);
    }

}
