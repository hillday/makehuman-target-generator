import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelModiferFilter extends ModifierFilter {

    public ModelModiferFilter(String config, String sourceFilter, String targetFile) {
        super(config, sourceFilter, targetFile);
    }

    public void filter() {
        JSONArray sourceJsonObjs = this.getSourceObjs();

        List<JSONObject> removeObjs = new ArrayList<JSONObject>();

        for(int i = 0; i < sourceJsonObjs.size(); i++){
           JSONObject ob = sourceJsonObjs.getJSONObject(i);
           String group = ob.getString("group");

            if (!this.configModifierGroups.contains(group)) {
                removeObjs.add(ob);
                continue;
            }

           JSONArray modifierObjs = ob.getJSONArray("modifiers");
            Iterator<Object> mdIts = modifierObjs.iterator();

            while (mdIts.hasNext()) {
                JSONObject mdObj = (JSONObject) mdIts.next();
                String target = mdObj.getString("target");
                String targetMin = "";
                String targetMax = "";
                if (mdObj.has("min"))
                    targetMin = mdObj.getString("min");
                if (mdObj.has("max"))
                    targetMax = mdObj.getString("max");
                String key = group + "/" + target;
                if (!targetMin.equals("") && !targetMax.equals("")) {
                    key = key + "-" + targetMin + "|" + targetMax;
                }

                if (!this.configModifiers.contains(key)) {
                    modifierObjs.remove(mdObj);
                }
            }

        }

        sourceJsonObjs.removeAll(removeObjs);

        this.targetContent = sourceJsonObjs.toString();
        this.write();
    }
}
