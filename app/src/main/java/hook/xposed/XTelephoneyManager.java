package hook.xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XTelephoneyManager extends XHook {
    private static final String className = "android.telephony.TelephonyManager";
    private static XTelephoneyManager xTelephoneyManager;

    public static XTelephoneyManager getInstance() {
        if (xTelephoneyManager == null) {
            xTelephoneyManager = new XTelephoneyManager();
        }
        return xTelephoneyManager;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "getDeviceId",
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param){
                        param.setResult("356357045618430");
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "getLine1Number", new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        param.setResult("13826290651");
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "getSubscriberId", new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param){
                        param.setResult("460006203280876");
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "getNetworkOperatorName", new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        param.setResult("46001");
                    }

                });
    }

}
