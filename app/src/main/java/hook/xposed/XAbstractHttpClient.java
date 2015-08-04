package hook.xposed;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;
import util.FileType;

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
                        BasicHttpResponse respone = (BasicHttpResponse) param.getResult();
                        String jsonRequest = handleRequest(request);
                        String jsonResult = handleResult(respone);
                        Util.writeLog(packageParam.packageName,jsonRequest);
                        if (!jsonResult.equals("")){
                            Util.writeLog(packageParam.packageName,jsonResult);
                        }
                    }
                });

    }

    private String handleRequest(HttpRequestBase request){
        JSONObject jsonObj = new JSONObject();
        try {
            String time = Util.getSystemTime();
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
        return jsonObj.toString();
    }

    private String handleResult(BasicHttpResponse respone){
        int len = 100;
        int off = 0;
        int count = 0;
        byte[] resultbyte = null;
        try {
            resultbyte = new byte[len];
            InputStream is = respone.getEntity().getContent();
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
