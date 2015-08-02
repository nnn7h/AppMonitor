package hook.xposed;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;

public class XURL extends XHook {
    private static final String className = "java.net.URL";
    private static XURL classLoadHook;

    public static XURL getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XURL();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "openConnection", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        URL url = (URL)param.thisObject;
                        String jsonResult = null;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "openConnection");
                            JSONObject content = new JSONObject();
                            content.put("url", url.toString());
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
