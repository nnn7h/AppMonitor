package hook.xposed;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.List;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class XRuntime extends XHook {

    private static final String className = "java.lang.Runtime";
    private static List<String> logList = null;
    private static XRuntime classLoadHook;

    public static XRuntime getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XRuntime();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "exec",
                String[].class, String[].class, File.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String[] prog = (String[]) param.args[0];
                        StringBuilder cmd = new StringBuilder();
                        for(int i=0 ;i <prog.length; i++){
                            cmd.append(prog[i]+" ");
                        }
                        String jsonResult;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "exec");
                            JSONObject content = new JSONObject();
                            content.put("cmd", cmd.toString());
                            jsonObj.put("content", content);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        jsonResult = jsonObj.toString();
                        Util.writeLog(packageParam.packageName,jsonResult);
                    }
                });
    }

}
