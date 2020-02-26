package com.chen91apps.echos.utils.pairs;

public class TabIconPair {
    private String text;
    private String tag;
    private int iconSelected;
    private int iconUnselected;

    public TabIconPair(String text, String tag, int iconSelected, int iconUnselected)
    {
        this.text = text;
        this.tag = tag;
        this.iconSelected = iconSelected;
        this.iconUnselected = iconUnselected;
    }

    public String getText()
    {
        return text;
    }

    public String getTag()
    {
        return tag;
    }

    public int getIconSelected()
    {
        return iconSelected;
    }

    public int getIconUnselected()
    {
        return iconUnselected;
    }
}
