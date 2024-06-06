package isy.mjc.fitcheckapp_1;

import android.graphics.drawable.Drawable;

public class ManageListItem {

    String memName;
    String memID;

    public Drawable getDelete() {
        return delete;
    }

    public void setDelete(Drawable delete) {
        this.delete = delete;
    }

    Drawable delete;

    ManageListItem(String _memName, String _memID) {
        memName = _memName;
        memID = _memID;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }
}
