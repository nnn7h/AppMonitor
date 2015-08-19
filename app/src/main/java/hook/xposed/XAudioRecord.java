package hook.xposed;

import android.media.AudioRecord;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XAudioRecord extends XHook {

    private static final String className = AudioRecord.class.getName();
    private static XAudioRecord xAudioRecord;

    public static XAudioRecord getInstance() {
        if (xAudioRecord == null) {
            xAudioRecord = new XAudioRecord();
        }
        return xAudioRecord;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "startRecording", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {

                        String time = Util.getSystemTime();
                        String callRef = Stack.getCallRef();

                        Logger.log("[*** Audio Record ***]");
                        Logger.log("[*** Audio Record ***] " + callRef);

                        StringBuffer logsb = new StringBuffer();
                        logsb.append("time: " + time + '\n')
                                .append("function: startRecordinge\n")
                                .append("callRef: " + callRef + '\n');
                        Util.writeLog(packageParam.packageName, logsb.toString());
                    }
                });
    }

}
