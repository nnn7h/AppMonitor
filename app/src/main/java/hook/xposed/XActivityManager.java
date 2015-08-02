package hook.xposed;

import android.app.ActivityManager;

import org.json.JSONException;
import org.json.JSONObject;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;

public class XActivityManager extends XHook {

    private static final String className = ActivityManager.class.getName();
    private static XActivityManager classLoadHook;

    public static XActivityManager getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XActivityManager();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "killBackgroundProcesses", String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String jsonResult ;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "killBackgroundProcesses");
                            JSONObject content = new JSONObject();
                            content.put("processes", param.args[0].toString());
                            jsonObj.put("content", content);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        jsonResult = jsonObj.toString();
                        Util.writeLog(packageParam.packageName,jsonResult);
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "forceStopPackage", String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String jsonResult ;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "forceStopPackage");
                            JSONObject content = new JSONObject();
                            content.put("package", param.args[0].toString());
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
