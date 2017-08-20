

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

    private void filterObjByTag(JSONObject obj,String tag){
        JSONObject groupObj = obj.getJSONObject(tag);

        Iterator keys = groupObj.keys();
        Map<String,String> addKeys = new HashMap<String, String>();

        while(keys.hasNext()) {
            String key = keys.next().toString();
            String value = groupObj.optString(key);

            for(String tkey : this.configTargetKeys) {
                if (key.indexOf(tkey) != -1) {
                    addKeys.put(key, value);
                    continue;
                }
            }
        }

        obj.put(tag,addKeys);
    }

}
