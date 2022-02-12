package dev.lucalewin.planer.tabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.dlyt.yanndroid.oneui.sesl.viewpager2.adapter.FragmentStateAdapter;

public class TabsAdapter extends FragmentStateAdapter {

    public TabsAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeTab();
                break;
            case 1:
                fragment = new SettingsTab();
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
