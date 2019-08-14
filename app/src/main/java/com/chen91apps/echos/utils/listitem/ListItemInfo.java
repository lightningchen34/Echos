package com.chen91apps.echos.utils.listitem;

public class ListItemInfo {
    private String title;
    private String subtitle;
    private String imageURL;

    public ListItemInfo(String title, String subtitle, String imageURL)
    {
        this.title = title;
        this.subtitle = subtitle;
        this.imageURL = imageURL;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }
}
