package hook.xposed;

import android.app.PendingIntent;
import android.telephony.SmsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;

public class XSmsManger extends XHook {
    private static final String className = SmsManager.class.getName();
    private static XSmsManger xSmsManger;

    public static XSmsManger getInstance() {
        if (xSmsManger == null) {
            xSmsManger = new XSmsManger();

        }
        return xSmsManger;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "sendTextMessage", String.class, String.class, String.class,
                PendingIntent.class, PendingIntent.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {

                        String time = Util.getSystemTime();
                        String jsonResult;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "sendTextMessage");
                            JSONObject content = new JSONObject();
                            content.put("adress", param.args[0].toString());
                            content.put("body", param.args[2].toString());
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
                "sendMultipartTextMessage", String.class, String.class,
                ArrayList.class, ArrayList.class, ArrayList.class,
                new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String jsonResult;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "sendMultipartTextMessage");
                            JSONObject content = new JSONObject();
                            content.put("adress", param.args[0].toString());
                            content.put("body", param.args[2].toString());
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
                "sendDataMessage", String.class, String.class, short.class,
                byte[].class, PendingIntent.class, PendingIntent.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String jsonResult;
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("time", time);
                            jsonObj.put("action", "sendDataMessage");
                            JSONObject content = new JSONObject();
                            content.put("adress", param.args[0].toString());
                            content.put("body", param.args[3].toString());
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
