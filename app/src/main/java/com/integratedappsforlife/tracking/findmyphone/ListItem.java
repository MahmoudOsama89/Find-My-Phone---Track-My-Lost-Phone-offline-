package com.integratedappsforlife.tracking.findmyphone;


public class ListItem {
    public String Title;
    public String Detals;
    public int ImageURL;
    public ListItem(String Title, String Detals, int ImageURL)
    { this. Title=Title;
        this. Detals=Detals;
        this. ImageURL=ImageURL;
    }

    public String getPhoneNumber() {
        return this.Detals;
    }
}
