package dev.lucalewin.planer.settings.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.dlyt.yanndroid.oneui.R;
import de.dlyt.yanndroid.oneui.layout.PreferenceFragment;

public class SettingsBaseFragment extends PreferenceFragment {

    @Override
    public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String s) { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setBackgroundColor(requireContext().getColor(R.color.item_background_color));
    }
}
