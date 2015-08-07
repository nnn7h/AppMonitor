package hook.xposed;

import android.os.Bundle;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XIntent extends XHook{
    private static final String className = "android.content.Intent";
    private static XIntent xIntent;

    public static XIntent getInstance() {
        if (xIntent == null) {
            xIntent = new XIntent();
        }
        return xIntent;
    }

    @Override
    void hook(XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "getExtras", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                if (param.getResult() == null){
                    param.setResult(new Bundle());
                }
            }
        });
    }
}
