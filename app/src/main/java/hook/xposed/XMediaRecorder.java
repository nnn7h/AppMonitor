package hook.xposed;

import android.media.MediaRecorder;

import org.json.JSONException;
import org.json.JSONObject;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;

public class XMediaRecorder extends XHook {

    private static final String className = MediaRecorder.class.getName();
    private static XMediaRecorder xMediaRecorder;

    public static XMediaRecorder getInstance() {
        if (xMediaRecorder == null) {
            xMediaRecorder = new XMediaRecorder();
        }
        return xMediaRecorder;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "start", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                String time = Util.getSystemTime();
                String jsonResult;
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("time", time);
                    jsonObj.put("action", "start");
                    jsonObj.put("content", null);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonResult = jsonObj.toString();
                Util.writeLog(packageParam.packageName, jsonResult);
            }
        });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "stop", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                String time = Util.getSystemTime();
                String jsonResult;
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("time", time);
                    jsonObj.put("action", "stop");
                    jsonObj.put("content", null);
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
