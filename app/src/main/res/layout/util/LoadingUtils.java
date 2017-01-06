package util;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by D5050 on 2016/10/11.
 */

public class LoadingUtils {
    private static KProgressHUD hud;
    public static void load_animo()
    {
        hud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("  请稍等...  ")
                .setCancellable(true);
        hud.show();
    }
    public static void load_animo_dismiss()
    {
        hud.dismiss();
    }

}
