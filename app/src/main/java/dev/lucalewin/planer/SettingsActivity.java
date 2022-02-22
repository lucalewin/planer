package dev.lucalewin.planer;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import de.dlyt.yanndroid.oneui.sesl.utils.SeslRoundedCorner;
import de.dlyt.yanndroid.oneui.view.RecyclerView;
import dev.lucalewin.planer.base.BaseThemeActivity;
import dev.lucalewin.planer.settings.fragments.MainSettingsFragment;

public class SettingsActivity extends BaseThemeActivity {

//    private String[] menuTitles = {
//            "Iserv",
//            "Notification",
//            "About Planer"
//    };

    private static class SettingsOption {

        public String title;
        public String subtitle;
        public Integer icon;
        public View.OnClickListener onClickListener;

        public SettingsOption(String title, String subtitle, Integer icon, View.OnClickListener onClickListener) {
            this.title = title;
            this.subtitle = subtitle;
            this.icon = icon;
            this.onClickListener = onClickListener;
        }
    }

    private final SettingsOption[] options = {
            new SettingsOption(
                    "Preferences",
                    "Theme * ...",
                    de.dlyt.yanndroid.oneui.R.drawable.ic_samsung_squircle,
                    view -> startActivity(new Intent().setClass(this, null))),
            new SettingsOption(
                    "Iserv Account",
                    "Iserv-URL * Username * Password",
                    de.dlyt.yanndroid.oneui.R.drawable.ic_samsung_squircle,
                    view -> startActivity(new Intent().setClass(this, IservAccountSettingsActivity.class))),
            new SettingsOption(
                    "About Planer",
                    null,
                    de.dlyt.yanndroid.oneui.R.drawable.sesl_btn_about,
                    view -> startActivity(new Intent().setClass(this, AboutActivity.class)))
    };

    private RecyclerView listView;
    private SettingsListViewAdapter listViewAdapter;

    private ToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbarLayout = findViewById(R.id.settings_toolbar_layout);
        toolbarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());

        TypedValue divider = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.listDivider, divider, true);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainSettingsFragment()).commit();

//        listView = findViewById(R.id.settings_list_view);
//        listView.setLayoutManager(new LinearLayoutManager(this));
//        listViewAdapter = new SettingsListViewAdapter();
//        listView.setAdapter(listViewAdapter);
//
//        ItemDecoration decoration = new ItemDecoration();
//        listView.addItemDecoration(decoration);
//        decoration.setDivider(this.getDrawable(divider.resourceId));
//
//        listView.setItemAnimator(null);
//        listView.seslSetFillBottomEnabled(true);
//        listView.seslSetGoToTopEnabled(true);
//        listView.seslSetLastRoundedCorner(false);


    }

    public class SettingsListViewAdapter extends RecyclerView.Adapter<SettingsListViewAdapter.ViewHolder> {

        @NonNull
        @Override
        public SettingsListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_listview_item, parent, false);
            return new SettingsListViewAdapter.ViewHolder(view, i);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (holder.isItem) {
                holder.imageView.setImageResource(de.dlyt.yanndroid.oneui.R.drawable.ic_samsung_squircle);
                holder.textView.setText(options[position].title);

                holder.parentView.setOnClickListener(options[position].onClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return options.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            boolean isItem;

            RelativeLayout parentView;
            ImageView imageView;
            TextView textView;

            ViewHolder(View itemView, int viewType) {
                super(itemView);

                isItem = viewType == 0;

                if (isItem) {
                    parentView = (RelativeLayout) itemView;
                    imageView = parentView.findViewById(R.id.settings_tab_item_image);
                    textView = parentView.findViewById(R.id.settings_tab_item_text);
                }
            }
        }

    }

    private boolean isRTL() {
        return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    private Context getContext() {
        return this;
    }

    public class ItemDecoration extends RecyclerView.ItemDecoration {
        private final SeslRoundedCorner mSeslRoundedCornerTop;
        private final SeslRoundedCorner mSeslRoundedCornerBottom;
        private Drawable mDivider;
        private int mDividerHeight;

        public ItemDecoration() {
            mSeslRoundedCornerTop = new SeslRoundedCorner(getContext(), true);
            mSeslRoundedCornerTop.setRoundedCorners(3);
            mSeslRoundedCornerBottom = new SeslRoundedCorner(getContext(), true);
            mSeslRoundedCornerBottom.setRoundedCorners(12);
        }

        @Override
        public void seslOnDispatchDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
            super.seslOnDispatchDraw(canvas, recyclerView, state);

            int childCount = recyclerView.getChildCount();
            int width = recyclerView.getWidth();

            // draw divider for each item
            for (int i = 0; i < childCount; i++) {
                View childAt = recyclerView.getChildAt(i);
                SettingsListViewAdapter.ViewHolder viewHolder = (SettingsListViewAdapter.ViewHolder) recyclerView.getChildViewHolder(childAt);
                int y = ((int) childAt.getY()) + childAt.getHeight();

                boolean shallDrawDivider;

                if (recyclerView.getChildAt(i + 1) != null)
                    shallDrawDivider = ((SettingsListViewAdapter.ViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i + 1))).isItem;
                else
                    shallDrawDivider = false;

                if (mDivider != null && viewHolder.isItem && shallDrawDivider) {
                    int moveRTL = isRTL() ? 130 : 0;
                    mDivider.setBounds(130 - moveRTL, y, width - moveRTL, mDividerHeight + y);
                    mDivider.draw(canvas);
                }

                if (!viewHolder.isItem) {
                    if (recyclerView.getChildAt(i + 1) != null)
                        mSeslRoundedCornerTop.drawRoundedCorner(recyclerView.getChildAt(i + 1), canvas);
                    if (recyclerView.getChildAt(i - 1) != null)
                        mSeslRoundedCornerBottom.drawRoundedCorner(recyclerView.getChildAt(i - 1), canvas);
                }
            }

            mSeslRoundedCornerTop.drawRoundedCorner(canvas);
        }

        public void setDivider(Drawable d) {
            mDivider = d;
            mDividerHeight = d.getIntrinsicHeight();
            listView.invalidateItemDecorations();
        }
    }

}