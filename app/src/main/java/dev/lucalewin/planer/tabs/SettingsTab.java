package dev.lucalewin.planer.tabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.dlyt.yanndroid.oneui.widget.SwipeRefreshLayout;
import dev.lucalewin.planer.R;

public class SettingsTab extends Fragment {

    private View mRootView;
    private Context mContext;

    public SettingsTab() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.fragment_settings_tab, container, false);
//        return mRootView;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        SwipeRefreshLayout swipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh);
//        swipeRefreshLayout.seslSetRefreshOnce(true);
//        swipeRefreshLayout.setOnRefreshListener(() -> Toast.makeText(mContext, "onRefresh called", Toast.LENGTH_SHORT).show());

    }

}
