package hook.xposed;

import android.content.ComponentName;
import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;

public class XApplicationPackageManager extends XHook {

    private static final String className = "android.app.ApplicationPackageManager";
    private static XApplicationPackageManager classLoadHook;

    public static XApplicationPackageManager getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XApplicationPackageManager();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "setComponentEnabledSetting", ComponentName.class, int.class,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        int state = (Integer) param.args[1];
                        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                            String jsonResult ;
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("time", time);
                                jsonObj.put("action", "setComponentEnabledSetting");
                                jsonObj.put("content", state);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonResult = jsonObj.toString();
                            Util.writeLog(packageParam.packageName, jsonResult);
                        }
                    }
                });

    }
}
