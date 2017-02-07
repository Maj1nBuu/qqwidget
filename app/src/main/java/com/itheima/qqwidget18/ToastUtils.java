package com.itheima.qqwidget18;

import android.content.Context;
import android.widget.Toast;

/**
 * 作者： itheima
 * 时间：2016-12-13 17:21
 * 网址：http://www.itheima.com
 */

public class ToastUtils {
    private static Toast sToast;

    public static void showToast(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        sToast.setText(msg);
        sToast.show();
    }
}
