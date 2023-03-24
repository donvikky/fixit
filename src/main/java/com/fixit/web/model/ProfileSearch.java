package com.fixit.web.model;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.State;

public class ProfileSearch {

    private Craft craft;

    private State state;

    public ProfileSearch() {
    }

    public ProfileSearch(Craft craft, State state) {
        this.craft = craft;
        this.state = state;
    }

    public Craft getCraft() {
        return craft;
    }

    public void setCraft(Craft craft) {
        this.craft = craft;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ProfileSearch{" +
                "craft=" + craft +
                ", state=" + state +
                '}';
    }
}
