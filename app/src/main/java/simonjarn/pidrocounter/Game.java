package simonjarn.pidrocounter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Game{
    public Game() {

    }

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="date")
    public String date;

    @ColumnInfo(name="blue_score")
    public Integer blue_score;

    @ColumnInfo(name="red_score")
    public Integer red_score;

    @ColumnInfo(name="selected_team")
    public Integer selected_team;

    @ColumnInfo(name="selected_action")
    public Integer selected_action;

    @ColumnInfo(name="last_changed")
    public Integer last_changed;

    @ColumnInfo(name="blue_name")
    public String blue_name;

    @ColumnInfo(name="red_name")
    public String red_name;

    @ColumnInfo(name="blue_moves")
    public String blue_moves;

    @ColumnInfo(name="red_moves")
    public String red_moves;

    @ColumnInfo(name="last_played")
    public Long last_played;
}
