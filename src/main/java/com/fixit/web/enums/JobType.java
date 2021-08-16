package com.fixit.web.enums;

public enum JobType {
    LUMP_SUM(1, "Lump Sum"),
    HOURLY(2, "Hourly");

    public final int id;
    public final String label;

    private JobType(int id, String label){
        this.id = id;
        this.label = label;
    }

    public static String valueOfId(int id) {
        for (JobType e : values()) {
            if (e.id == id) {
                return e.label;
            }
        }
        return null;
    }
}
