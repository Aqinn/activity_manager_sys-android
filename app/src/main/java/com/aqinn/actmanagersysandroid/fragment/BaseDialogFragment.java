package com.aqinn.actmanagersysandroid.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author Aqinn
 * @date 2021/1/16 6:05 PM
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected Dialog mDialog;

    protected void initDialog() {
        mDialog = getDialog();
        Window win = getDialog().getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = win.getAttributes();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        win.setAttributes(params);
    }

}
