package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.net.Uri;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XContentResolver extends XHook {

	private static final String className = "android.content.ContentResolver";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XContentResolver classLoadHook;

	public static XContentResolver getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XContentResolver();
		}
		return classLoadHook;
	}

	@Override
	String getClassName() {
		// TODO Auto-generated method stub
		return className;
	}

	@Override
	void hook(String pkgName, ClassLoader classLoader) {
		// TODO Auto-generated method stub
		localpkgName = pkgName;
		logList = new ArrayList<String>();
		XposedHelpers.findAndHookMethod(className, classLoader, "query",
				Uri.class, String[].class, String.class, String[].class,
				String.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--query database--");
						logList.add("function:query");
						logList.add("Uri:" + param.args[0].toString());
						logList.add("selection:" + param.args[2].toString());
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}
				});
		
		XposedHelpers.findAndHookMethod(className, classLoader, "insert",
				Uri.class, ContentValues.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--add data to database--");
						logList.add("function:insert");
						logList.add("Uri:" + param.args[0].toString());
						logList.add("value:" + param.args[1].toString());
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}
				});
		
		XposedHelpers.findAndHookMethod(className, classLoader, "delete",
				Uri.class, String.class, String[].class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--delete data from database--");
						logList.add("function:delete");
						logList.add("Uri:" + param.args[0].toString());
						logList.add("where:" + param.args[1].toString());
						logList.add("selectionArgs:" + param.args[2].toString());
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}
				});
	}

}
