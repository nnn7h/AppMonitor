package hook.xposed;

import java.net.URL;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
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
                        URL url = (URL) param.thisObject;
                        String callRef = Stack.getCallRef();
                        boolean flag = Logger.isFeeUrl(url);

                        if(!url.toString().contains("http:")){
                            return;
                        }
                        
                        if (flag) {
                            Logger.log("[### URL openConnection ###]");
                            Logger.log("[### URL openConnection ###] " + url);
                            Logger.log("[### URL openConnection ###] " + callRef);
                        } else {
                            Logger.log("[=== URL openConnection ===]");
                            Logger.log("[=== URL openConnection ===] " + url);
                            Logger.log("[=== URL openConnection ===] " + callRef);
                        }

                        StringBuffer logsb = new StringBuffer();
                        logsb.append("time:" + time)
                                .append("function: openConnection\n")
                                .append("url:" + url)
                                .append("is fee url: " + flag + '\n')
                                .append("callRef: " + callRef + '\n');

                        Util.writeLog(packageParam.packageName, logsb.toString());
                    }
                });
    }

}
