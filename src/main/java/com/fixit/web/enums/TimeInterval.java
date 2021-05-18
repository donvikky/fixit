package com.fixit.web.enums;

public enum TimeInterval {

    SECONDS(1, "Second(s)"),
    MINUTES(2, "Minute(s)"),
    HOURS(3, "Hour(s)"),
    DAYS(4, "Day(s)"),
    YEARS(5, "Year(s)");

    public final int id;
    public final String label;

    private TimeInterval(int id, String label){
        this.id = id;
        this.label = label;
    }

    public static String valueOfId(int id) {
        for (TimeInterval e : values()) {
            if (e.id == id) {
                return e.label;
            }
        }
        return null;
    }
}
