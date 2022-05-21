package dev.lucalewin.lucraftui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class FormLayout extends LinearLayout {

    public FormLayout(Context context) {
        super(context, null, R.style.LucraftUI_Layouts_FormLayout);
    }

    public FormLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, R.style.LucraftUI_Layouts_FormLayout);
    }

    public FormLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.style.LucraftUI_Layouts_FormLayout);
    }

    public FormLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, R.style.LucraftUI_Layouts_FormLayout);
    }

}
