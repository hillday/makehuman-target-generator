
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.File;
import java.util.*;

/**
 * makehuman https://github.com/makehuman-js/makehuman-js tool for gen bin
 */
public class App {
    //data dir
    public static String DATA_PATH = "D:/project/intelliJ_workskpace/makehuman-git/makehuman-target-generator/data/";

    //source target data
    public static String SOURCE_TARGET_LIST = DATA_PATH + "source/target-list.json";
    public static String SOURCE_TARGET_BIN = DATA_PATH + "source/targets.bin";
    public static String SOURCE_TARGET_LIST_MAPPING_BIN = DATA_PATH + "source/target-list-mapping-bin.json";

    //source modifier data
    public static String SOURCE_MODIFIER_MODING = DATA_PATH + "source/modeling_modifiers.json";
    public static String SOURCE_MODIFIER_MEASUREMENT = DATA_PATH + "source/measurement_modifiers.json";

    //source slider data
    public static String SOURCE_SLIDERS_MODING = DATA_PATH + "source/modeling_sliders.json";
    public static String SOURCE_SLIDERS_MEASUREMENT = DATA_PATH + "source/measurement_sliders.json";

    //target data file
    public static String TARGET_TARGET_LIST = DATA_PATH + "target/target-list.json";
    public static String TARGET_TARGET_BIN = DATA_PATH + "target/targets.bin";

    //target modifier data file
    public static String TARGET_MODIFIER_MODING = DATA_PATH + "target/modeling_modifiers.json";
    public static String TARGET_MODIFIER_MEASUREMENT = DATA_PATH + "target/measurement_modifiers.json";

    //target slider data file
    public static String TARGET_SLIDERS_MODING = DATA_PATH + "target/modeling_sliders.json";
    public static String TARGET_SLIDERS_MEASUREMENT = DATA_PATH + "target/measurement_sliders.json";

    //configure file with to customize modifier
    public static String CONFIG_MODIFIER_FILE = DATA_PATH + "config/conf-modifiers.json";
    public static String SOURCE_MODIFIER_OBJ_FILE = DATA_PATH + "source/metaTargets.dt";

    public static Map<String,Integer> targetLlistBinMap = new HashMap<String,Integer>();


    /**
     * splice target use index
     *
     * @param shorts bin array
     * @param index  0-1257 target numbers
     * @return
     */
    public static short[] getTargetShorts(short[] shorts, int index) {
        int len = 19158 * 3;
        short[] tgshorts = new short[19158 * 3];

        int k = 0;
        for (int i = index * len; i < (index * len) + len; i++) {
            tgshorts[k] = shorts[i];
            k++;
        }
        return tgshorts;
    }



    /**
     * parse to target object
     *
     * @param shorts
     * @param targetsFile
     * @return
     */
    public static List<Target> parseToTargets(short[] shorts, String targetsFile) {
        List<Target> targets = new ArrayList<Target>();
        JSONObject targetsObj = JSONObject.fromObject(FileUtil.readFile(targetsFile));
        JSONObject targetsItems = targetsObj.getJSONObject("targets");
        Iterator keys = targetsItems.keys();

        while (keys.hasNext()) {
            try {

                String key = keys.next().toString();
                //System.out.println("idex"+targetIdx+"  "+key);
                short[] tgtarget = getTargetShorts(shorts, targetLlistBinMap.get(key).intValue());

                Target target = new Target(key, tgtarget);
                targets.add(target);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return targets;
    }

    public static void initTargetListMaping(){
        String jsonMap = FileUtil.readFile(SOURCE_TARGET_LIST_MAPPING_BIN);
        JSONObject targetsObj = JSONObject.fromObject(jsonMap);

        Iterator keys = targetsObj.keys();

        while (keys.hasNext()) {
            String key = keys.next().toString();
            Integer val = targetsObj.optInt(key);

            targetLlistBinMap.put(key,val);
        }
    }

    public static void main(String[] args) {
        initTargetListMaping();

        //parse target bin to short array
        byte[] inBinBytes = FileUtil.toByteArray(SOURCE_TARGET_BIN);
        short[] shorts = new short[inBinBytes.length / 2];

        int i = 0;
        while (i < inBinBytes.length / 2) {
            byte[] tmpBytes = {inBinBytes[i * 2], inBinBytes[(i * 2) + 1]};
            shorts[i] = StreamTool.byteToShort(tmpBytes);

            i++;
        }


        System.out.println("begin gen target meta and customize bin...");
        List<Target> targets = parseToTargets(shorts, SOURCE_TARGET_LIST);
        Collections.sort(targets);
        ModifierFilter targetMetaFilter = new TargerListFilter(CONFIG_MODIFIER_FILE, SOURCE_TARGET_LIST, TARGET_TARGET_LIST);
        targetMetaFilter.filter();

        //List<Target> targets = FileUtil.readObjectForList(new File(SOURCE_MODIFIER_OBJ_FILE));
        TargetBinFilter targetBinFilter = new TargetBinFilter(targetMetaFilter.getTargetContent(), targets);
        short[] outputTargetShorts = targetBinFilter.filter();

        /*for(int kk = 0; kk < outputTargetShorts.length; kk ++){
            if(outputTargetShorts[kk] != 0){
                short rd = 10;
                outputTargetShorts[kk] = (short)(outputTargetShorts[kk] * rd);

                if(kk < 19158 * 3)
                    System.out.println("index "+kk+ " "+ outputTargetShorts[kk]);
            }
        }*/
        FileUtil.writeBytesToFile(TARGET_TARGET_BIN, StreamTool.shotsToBytes(outputTargetShorts));

        System.out.println("begin gen new modifier files...");
        ModifierFilter modingModifierFilter = new ModelModiferFilter(CONFIG_MODIFIER_FILE, SOURCE_MODIFIER_MODING, TARGET_MODIFIER_MODING);
        modingModifierFilter.filter();

        ModifierFilter measurementModifierFilter = new ModelModiferFilter(CONFIG_MODIFIER_FILE, SOURCE_MODIFIER_MEASUREMENT, TARGET_MODIFIER_MEASUREMENT);
        measurementModifierFilter.filter();

        System.out.println("begin gen new slider files...");
        ModifierFilter modingSliderFilter = new ModelSliderFilter(CONFIG_MODIFIER_FILE, SOURCE_SLIDERS_MODING, TARGET_SLIDERS_MODING);
        modingSliderFilter.filter();

        ModifierFilter measurementSliderFilter = new ModelSliderFilter(CONFIG_MODIFIER_FILE, SOURCE_SLIDERS_MEASUREMENT, TARGET_SLIDERS_MEASUREMENT);
        measurementSliderFilter.filter();

        System.out.println("success");

    }
}
