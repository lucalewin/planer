package dev.lucalewin.planer.preferences.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.dlyt.yanndroid.oneui.preference.Preference;
import de.dlyt.yanndroid.oneui.preference.PreferenceViewHolder;
import dev.lucalewin.planer.R;

public class BadgePreferenceScreen extends Preference {

    public static final int N_BADGE = -1;

    private int badgeNumber = 0;

    public BadgePreferenceScreen(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BadgePreferenceScreen(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BadgePreferenceScreen(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BadgePreferenceScreen(@NonNull Context context) {
        super(context);
    }

    {
        setWidgetLayoutResource(R.layout.badge_layout);
    }

    public void setBadge(int n) {
        badgeNumber = n;
        notifyChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        TextView badgeNumberTextView = (TextView) holder.findViewById(R.id.badge_number);

        badgeNumberTextView.setVisibility(View.GONE);

        if (badgeNumber == N_BADGE) {
            badgeNumberTextView.setText("N");
            badgeNumberTextView.setVisibility(View.VISIBLE);
        } else if (badgeNumber > 0) {
            badgeNumberTextView.setText(String.valueOf(badgeNumber));
            badgeNumberTextView.setVisibility(View.VISIBLE);
        }
    }
}
