package hook.xposed;

import android.app.Notification;
import android.app.NotificationManager;

import org.json.JSONException;
import org.json.JSONObject;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;

public class XNotificationManager extends XHook {

    private static final String className = NotificationManager.class.getName();
    private static XNotificationManager classLoadHook;

    public static XNotificationManager getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XNotificationManager();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "notify",
                int.class, Notification.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String jsonResult ;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "notify");
                            JSONObject content = new JSONObject();
                            content.put("Notification", param.args[1].toString());
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
