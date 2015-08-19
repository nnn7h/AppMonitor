package hook.xposed;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class XString extends XHook {
	private static final String className = "java.lang.String";
	private static XString classLoadHook;

	public static XString getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XString();
		}
		return classLoadHook;
	}

	@Override
	void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
		XposedHelpers.findAndHookConstructor(className, packageParam.classLoader,
				String.class, new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String str = param.args[0].toString();
						String callRef = Stack.getCallRef();

						Logger.log("[- String -] ");
						Logger.log("[- String -] " + str);
						Logger.log("[- String -] " + callRef);
					}

				});
	}

}
