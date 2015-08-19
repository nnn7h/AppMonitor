package hook.xposed;

import android.media.MediaRecorder;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XMediaRecorder extends XHook {

    private static final String className = MediaRecorder.class.getName();
    private static XMediaRecorder xMediaRecorder;

    public static XMediaRecorder getInstance() {
        if (xMediaRecorder == null) {
            xMediaRecorder = new XMediaRecorder();
        }
        return xMediaRecorder;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "start", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                String time = Util.getSystemTime();
                String callRef = Stack.getCallRef();
                Logger.log("[*** Media Recorder ***]");
                Logger.log("[*** Media Recorder ***] " + callRef);

                StringBuffer logsb = new StringBuffer();
                logsb.append("time: " + time + '\n')
                        .append("function: MediaRecorder.start\n")
                        .append("callRef: " + callRef);

                Util.writeLog(packageParam.packageName, logsb.toString());
            }
        });
    }
}
