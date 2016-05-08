package com.eu.habbo.habbohotel.rooms;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("NullableProblems")
public class RoomCategory implements Comparable<RoomCategory> {

    private int id;
    private int minRank;
    private String caption;
    private boolean canTrade;

    public RoomCategory(ResultSet set) throws SQLException
    {
        this.id = set.getInt("id");
        this.minRank = set.getInt("min_rank");
        this.caption = set.getString("caption");
        this.canTrade = set.getBoolean("can_trade");
    }

    public int getId() {
        return this.id;
    }

    public int getMinRank() {
        return this.minRank;
    }

    public String getCaption() {
        return this.caption;
    }

    public boolean isCanTrade() {
        return this.canTrade;
    }

    @Override
    public int compareTo(RoomCategory o) {
        return o.getId() - this.getId();
    }
}
