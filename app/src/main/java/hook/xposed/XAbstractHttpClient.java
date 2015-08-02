package hook.xposed;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;

public class XAbstractHttpClient extends XHook {
    private static final String className = "org.apache.http.impl.client.AbstractHttpClient";
    private static XAbstractHttpClient xAbstractHttpClient;

    public static XAbstractHttpClient getInstance() {
        if (xAbstractHttpClient == null) {
            xAbstractHttpClient = new XAbstractHttpClient();
        }
        return xAbstractHttpClient;
    }


    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "execute",
                HttpHost.class, HttpRequest.class, HttpContext.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        HttpRequestBase request = (HttpRequestBase) param.args[1];
                        String time = Util.getSystemTime();
                        String jsonResult = null;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "execute");
                            JSONObject content = new JSONObject();
                            content.put("method", request.getMethod());
                            content.put("url", request.getURI().toString());
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
