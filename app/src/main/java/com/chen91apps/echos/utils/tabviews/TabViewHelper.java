package com.chen91apps.echos.utils.tabviews;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chen91apps.echos.R;
import com.chen91apps.echos.utils.ThemeColors;
import com.chen91apps.echos.utils.pairs.ListInfoPair;
import com.chen91apps.echos.utils.pairs.TabIconPair;
import com.google.android.material.tabs.TabLayout;

public class TabViewHelper {

    public static void setIconTabActive(TabLayout.Tab tab, TabIconPair info, boolean active)
    {
        View view = tab.getCustomView();
        if (view == null)
        {
            tab.setCustomView(R.layout.tabicon_item);
            view = tab.getCustomView();
        }
        TextView textView = (TextView) view.findViewById(R.id.tabicon_item_text);
        ImageView imageView = (ImageView) view.findViewById(R.id.tabicon_item_image);
        textView.setText(info.getText());
        if (active) {
            textView.setTextColor(ThemeColors.getColor(view.getContext(), R.attr.highlight_color));
            imageView.setImageResource(info.getIconSelected());
        } else
        {
            textView.setTextColor(ThemeColors.getColor(view.getContext(), R.attr.undertint_color));
            imageView.setImageResource(info.getIconUnselected());
        }
    }

    public static void setTextTabActive(TabLayout.Tab tab, ListInfoPair info, boolean active)
    {
        View view = tab.getCustomView();
        if (view == null)
        {
            tab.setCustomView(R.layout.texticon_item);
            view = tab.getCustomView();
        }
        TextView textView = (TextView) view.findViewById(R.id.texticon_item_text);
        textView.setText(info.title.toUpperCase());
        if (active) {
            textView.setTextColor(ThemeColors.getColor(view.getContext(), R.attr.text_color));
            textView.setTextSize(16);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else
        {
            textView.setTextColor(ThemeColors.getColor(view.getContext(), R.attr.undertint_color));
            textView.setTextSize(12);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }
}
