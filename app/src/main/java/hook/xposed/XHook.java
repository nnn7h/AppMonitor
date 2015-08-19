package hook.xposed;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public abstract class XHook {
    abstract void hook(XC_LoadPackage.LoadPackageParam packageParam);
}