
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ModifierFilter {
    protected String config;
    protected String sourceFilter;
    protected JSONArray configJsonObjs;
    protected String targetFile;
    protected List<String> configModifierGroups;
    protected List<String> configModifiers;
    protected List<String> configTargetKeys;
    protected String targetContent;

    public String getTargetContent() {
        return targetContent;
    }

    public void setTargetContent(String targetContent) {
        this.targetContent = targetContent;
    }

    public ModifierFilter(String config, String sourceFilter, String targetFile){
        this.config = FileUtil.readFile(config);
        this.sourceFilter = FileUtil.readFile(sourceFilter);
        this.targetFile = targetFile;
        this.configJsonObjs = JSONArray.fromObject(this.config);

        configModifierGroups = new ArrayList<String>();
        configModifiers = new ArrayList<String>();
        configTargetKeys = new ArrayList<String>();
        init();
    }

    private void init(){
        Iterator<Object> it = configJsonObjs.iterator();

        while (it.hasNext()) {
            JSONObject ob = (JSONObject) it.next();
            String group = ob.getString("group");
            configModifierGroups.add(group);

            JSONArray modifierObjs = ob.getJSONArray("modifiers");
            Iterator<Object> mdIts = modifierObjs.iterator();

            while (mdIts.hasNext()) {
                JSONObject mdObj = (JSONObject) mdIts.next();
                String target = mdObj.getString("target");
                String targetMin =  "";
                String targetMax = "";
                if(mdObj.has("min"))
                     targetMin = mdObj.getString("min");
                if(mdObj.has("max"))
                     targetMax = mdObj.getString("max");
                String key = group+"/"+ target;
                if(!targetMin.equals("") && !targetMax.equals("")){
                    key = key + "-" + targetMin + "|" + targetMax;
                }

                String targetKey = target;
                configModifiers.add(key);
                configTargetKeys.add(targetKey);
            }
        }
    }

    protected JSONArray getSourceObjs(){
        JSONArray objs = JSONArray.fromObject(this.sourceFilter);

        return objs;
    }

    protected JSONObject getSourceObj(){
        JSONObject obj = JSONObject.fromObject(this.sourceFilter);

        return obj;
    }


    public void write() {
        FileUtil.fileWriter(this.targetFile,this.targetContent);
    }

    public abstract void  filter();
}
