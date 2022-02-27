package dev.lucalewin.planer.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import de.dlyt.yanndroid.oneui.widget.RoundFrameLayout;
import dev.lucalewin.planer.R;

public class TipsCardView extends RoundFrameLayout {

//    private final Context context;

    private String title = "";
    private String message = "";

    private TextView titleTextView;
    private TextView messageTextView;
    private ImageButton closeImageButton;

    public TipsCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public TipsCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TipsCardView(Context context) {
        super(context);
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        inflate(context, R.layout.tips_card_layout, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TipsCardView, defStyle, 0);

        titleTextView = findViewById(R.id.tips_card_title_textview);
        messageTextView = findViewById(R.id.tips_card_message_textview);
        closeImageButton = findViewById(R.id.tips_card_close_imagebutton);

        titleTextView.setText(typedArray.getText(R.styleable.TipsCardView_title));
        messageTextView.setText(typedArray.getText(R.styleable.TipsCardView_message));

        closeImageButton.setOnClickListener(view -> setVisibility(GONE));

        setBackgroundColor(typedArray.getColor(R.styleable.TipsCardView_backgroundColor, getResources().getColor(de.dlyt.yanndroid.oneui.R.color.primary_color)));

        typedArray.recycle();
    }

    public void setTitle(String title) {
        this.title = title;
        titleTextView.setText(title);
    }

    public void setMessage(String message) {
        this.message = message;
        messageTextView.setText(message);
    }

    public void setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
        closeImageButton.setOnClickListener(onCloseClickListener);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
    }

}
