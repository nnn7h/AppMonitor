package hook.xposed;

import android.content.ContentValues;
import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;

public class XContentResolver extends XHook {

    private static final String className = "android.content.ContentResolver";
    private static final String[] privacyUris = {
            "content://com.android.contacts",
            "content://sms",
            "content://mms-sms",
            "content://contacts/",
            "content://call_log",
            "content://telephony",
            "content://browser/bookmarks"};

    private static XContentResolver xContentResolver;

    public static XContentResolver getInstance() {
        if (xContentResolver == null) {
            xContentResolver = new XContentResolver();
        }
        return xContentResolver;
    }

    private boolean isUriAvailable(String uri) {
        String url = uri.toLowerCase();
        for (String listUri : privacyUris) {
            if (url.contains(listUri)) {
                return true;
            }
        }
        return false;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "query",
                Uri.class, String[].class, String.class, String[].class,
                String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String uri = param.args[0].toString();
                        String selection = param.args[2].toString();
                        String time = Util.getSystemTime();
                        if (isUriAvailable(uri)) {
                            String jsonResult;
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("time", time);
                                jsonObj.put("action", "query");
                                JSONObject content = new JSONObject();
                                content.put("Uri", uri);
                                content.put("selection", selection);
                                jsonObj.put("content", content);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonResult = jsonObj.toString();
                            Util.writeLog(packageParam.packageName, jsonResult);
                        }
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "insert",
                Uri.class, ContentValues.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String uri = param.args[0].toString();
                        String value = param.args[1].toString();
                        String time = Util.getSystemTime();
                        if (isUriAvailable(uri)) {
                            String jsonResult;
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("time", time);
                                jsonObj.put("action", "insert");
                                JSONObject content = new JSONObject();
                                content.put("Uri", uri);
                                content.put("value", value);
                                jsonObj.put("content", content);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonResult = jsonObj.toString();
                            Util.writeLog(packageParam.packageName, jsonResult);
                        }
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "delete",
                Uri.class, String.class, String[].class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String uri = param.args[0].toString();
                        String where = param.args[1].toString();
                        String selection = param.args[2].toString();
                        String time = Util.getSystemTime();
                        if (isUriAvailable(uri)) {
                            String jsonResult;
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("time", time);
                                jsonObj.put("action", "delete");
                                JSONObject content = new JSONObject();
                                content.put("Uri", uri);
                                content.put("where", where);
                                content.put("selectionArgs", selection);
                                jsonObj.put("content", content);
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
