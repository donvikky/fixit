package com.fixit.web.model;

import com.fixit.web.entity.Craft;

public class ProfileSearch {

    private Craft craft;

    public ProfileSearch() {
    }

    public ProfileSearch(Craft craft) {
        this.craft = craft;
    }

    public Craft getCraft() {
        return craft;
    }

    public void setCraft(Craft craft) {
        this.craft = craft;
    }

    @Override
    public String toString() {
        return "ProfileSearch{" +
                "craft=" + craft +
                '}';
    }
}
