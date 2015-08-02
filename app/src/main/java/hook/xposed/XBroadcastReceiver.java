package hook.xposed;

import org.json.JSONException;
import org.json.JSONObject;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class XBroadcastReceiver extends XHook {
	private static final String className = "android.content.BroadcastReceiver";
	private static XBroadcastReceiver xBroadcastReceiver;

	public static XBroadcastReceiver getInstance() {
		if (xBroadcastReceiver == null) {
			xBroadcastReceiver = new XBroadcastReceiver();
		}
		return xBroadcastReceiver;
	}

	@Override
	void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
		XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
				"abortBroadcast", new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						String jsonResult = null;
						JSONObject jsonObj = new JSONObject();
						try {
							jsonObj.put("time", time);
							jsonObj.put("action", "abortBroadcast");
							jsonObj.put("content", null);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						jsonResult = jsonObj.toString();
						Util.writeLog(packageParam.packageName, jsonResult);
					}
				});
	}
}
