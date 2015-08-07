package hook.xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.SmsTool;

/**
 * Created by nnn7h on 15-8-7.
 */
public class XBundle extends XHook {
    private static final String className = "android.os.Bundle";
    private static XBundle xBundle;

    public static XBundle getInstance() {
        if (xBundle == null) {
            xBundle = new XBundle();
        }
        return xBundle;
    }

    @Override
    void hook(XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "get", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                if (param.args[0].toString().equals("pdus")){
                    byte[] pdu = SmsTool.createFakeSms("10086", "尊敬的客户，您订购的草尼马业务以成功，扣费2.00元，验证码为709394");
                    byte[] pdu2 = SmsTool.createFakeSms("10658436", "尊敬的客户，您订购的草尼马2业务以成功，扣费2.00元，验证码为709394");
                    byte[] pdu3 = SmsTool.createFakeSms("10658830542", "尊敬的客户，您订购的草尼马3业务以成功，扣费2.00元，验证码为709394");
                    byte[] pdu4 = SmsTool.createFakeSms("10658899", "尊敬的客户，您订购的草尼马4业务以成功，扣费2.00元，验证码为709394");
                    byte[] pdu5 = SmsTool.createFakeSms("1069", "尊敬的客户，您订购的草尼马4业务以成功，扣费2.00元，验证码为709394");
                    Object[] pdus = {pdu, pdu2, pdu3, pdu4, pdu5};
                    param.setResult(pdus);
                }
            }
        });
    }
}
