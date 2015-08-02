package hook.xposed;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;

public class XProcessBuilder extends XHook {

    private static final String className = "java.lang.ProcessBuilder";
    private static XProcessBuilder classLoadHook;

    public static XProcessBuilder getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XProcessBuilder();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "start",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        ProcessBuilder pb = (ProcessBuilder) param.thisObject;
                        List<String> cmds = pb.command();
                        StringBuilder sb = new StringBuilder();
                        for(int i=0 ;i <cmds.size(); i++){
                            sb.append(cmds.get(i)+" ");
                        }
                        String jsonResult;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "startCMD");
                            JSONObject content = new JSONObject();
                            content.put("cmd", sb.toString());
                            jsonObj.put("content", content);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        jsonResult = jsonObj.toString();
                        Util.writeLog(packageParam.packageName, jsonResult);
                    }
                });
    }

}
