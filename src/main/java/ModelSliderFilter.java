import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelSliderFilter extends ModifierFilter {
    public ModelSliderFilter(String config, String sourceFilter, String targetFile) {
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
        JSONObject modifierObj = groupObj.getJSONObject("modifiers");

        Iterator keys = modifierObj.keys();

        while(keys.hasNext()) {
            try {

                String key = keys.next().toString();
                JSONArray value = modifierObj.optJSONArray(key);

                List<JSONObject> rmMdObjs = new ArrayList<JSONObject>();

                for(int i = 0; i < value.size(); i++){
                    JSONObject ob = value.getJSONObject(i);
                    String mod = ob.getString("mod");

                    if(!this.configModifiers.contains(mod)){
                        rmMdObjs.add(ob);
                        continue;
                    }
                }

                value.removeAll(rmMdObjs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
