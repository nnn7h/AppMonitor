package hook.xposed;

import android.telephony.SmsMessage;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XSmsMessage extends XHook {
    private static final String className = "android.telephony.SmsMessage";
    private static XSmsMessage xSmsMessage;

    public static XSmsMessage getInstance() {
        if (xSmsMessage == null) {
            xSmsMessage = new XSmsMessage();
        }
        return xSmsMessage;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "createFromPdu", byte[].class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        SmsMessage smsMessage = (SmsMessage) param.getResult();
                        String addr = smsMessage.getDisplayOriginatingAddress();
                        String body = smsMessage.getDisplayMessageBody();
                        String callRef = Stack.getCallRef();

                        Logger.log("[### Read SMS ###]");
                        Logger.log("[### Read SMS ###] " + addr);
                        Logger.log("[### Read SMS ###] " + body);
                        Logger.log("[### Read SMS ###] " + callRef);

                        StringBuffer logsb = new StringBuffer();
                        logsb.append("time: " + time + '\n')
                                .append("action:receive sms\n")
                                .append("adress:" + addr + '\n')
                                .append("body:" + body + '\n')
                                .append("callRef: " + callRef + '\n');

                        Util.writeLog(packageParam.packageName, logsb.toString());
                    }
                });
    }

}
