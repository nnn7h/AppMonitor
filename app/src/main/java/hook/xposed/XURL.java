package hook.xposed;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.FileType;
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
                        String url = param.thisObject.toString();
                        if (!url.contains("http:")){
                            return;
                        }
                        HttpURLConnection connection = (HttpURLConnection) param.getResult();
                        String jsonUrl = handleUrl(url);
                        Util.writeLog(packageParam.packageName, jsonUrl);
                        String jsonResult = handleResult(connection);
                        if (!jsonResult.equals("")){
                            Util.writeLog(packageParam.packageName, jsonUrl);
                        }
                    }
                });
    }

    private String handleUrl(String url){
        String time = Util.getSystemTime();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("time", time);
            jsonObj.put("action", "openConnection");
            JSONObject content = new JSONObject();
            content.put("url", url);
            jsonObj.put("content", content);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    private String handleResult(HttpURLConnection connection){
        int len = 100;
        int off = 0;
        int count = 0;
        byte[] resultbyte = null;
        try {
            resultbyte = new byte[len];
            InputStream is = connection.getInputStream();
            count = is.read(resultbyte);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (count == -1 || resultbyte == null) return "";

        if (count<len) len = count;

        String fileHead;
        byte[] bytesArr = new byte[len];
        System.arraycopy(resultbyte,off,bytesArr,off,len);

        fileHead = Util.bytesToHexString(bytesArr);

        if (fileHead == null) return "";

        JSONObject jsonObj = new JSONObject();
        FileType[] fileTypes = FileType.values();
        for (FileType type : fileTypes){
            if(fileHead.startsWith(type.getValue())){
                try {
                    String time = Util.getSystemTime();
                    jsonObj.put("time", time);
                    jsonObj.put("action", "download");
                    JSONObject content = new JSONObject();
                    content.put("type", type.toString());
                    jsonObj.put("content", content);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return jsonObj.toString();
    }

}
