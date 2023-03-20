package uc2.ui;

import android.view.View;

import androidx.databinding.BindingAdapter;


public class Uc2CustomBinding {
    @BindingAdapter("setVisibilityToView")
    public static void setVisibilityToView(View view, boolean connected)
    {
        if (view != null) {
            if (connected)
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);
        }

    }
}
