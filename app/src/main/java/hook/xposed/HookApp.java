package hook.xposed;

import android.content.Context;
import java.util.Set;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookApp implements IXposedHookLoadPackage {
    public Set<String> appList;
    public static Context context;

    private Set<String> getHookPkg(){
        XSharedPreferences pkgsPref = new XSharedPreferences("com.android.appmonitor", "pkgs");
        return pkgsPref.getStringSet("pkgs", null);
    }

    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) {
        appList = getHookPkg();
        if (appList == null || !appList.contains(loadPackageParam.packageName)) {
            return;
        }

        hook(XAbstractHttpClient.getInstance(), loadPackageParam);
        //尝试监控插屏广告
//        hook(XActivity.getInstance(), loadPackageParam);
        hook(XActivityManager.getInstance(), loadPackageParam);
        hook(XApplicationPackageManager.getInstance(), loadPackageParam);
        hook(XAudioRecord.getInstance(), loadPackageParam);

        hook(XBroadcastReceiver.getInstance(), loadPackageParam);
        hook(XContentResolver.getInstance(), loadPackageParam);
        hook(XMediaRecorder.getInstance(), loadPackageParam);
        hook(XNotificationManager.getInstance(), loadPackageParam);
        hook(XProcessBuilder.getInstance(), loadPackageParam);
        hook(XRuntime.getInstance(), loadPackageParam);
        hook(XSmsManger.getInstance(), loadPackageParam);
        hook(XTelephoneyManager.getInstance(), loadPackageParam);
        hook(XURL.getInstance(), loadPackageParam);
        hook(XWebView.getInstance(), loadPackageParam);
    }

    public void hook(XHook xhook, LoadPackageParam packageParam) {
        xhook.hook(packageParam);
    }
}