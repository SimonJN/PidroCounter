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
public class Game implements Parcelable {
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

    protected Game(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        name = in.readString();
        date = in.readString();
        blue_score = in.readByte() == 0x00 ? null : in.readInt();
        red_score = in.readByte() == 0x00 ? null : in.readInt();
        selected_team = in.readByte() == 0x00 ? null : in.readInt();
        selected_action = in.readByte() == 0x00 ? null : in.readInt();
        last_changed = in.readByte() == 0x00 ? null : in.readInt();
        blue_name = in.readString();
        red_name = in.readString();
        blue_moves = in.readString();
        red_moves = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(date);
        if (blue_score == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(blue_score);
        }
        if (red_score == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(red_score);
        }
        if (selected_team == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(selected_team);
        }
        if (selected_action == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(selected_action);
        }
        if (last_changed == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(last_changed);
        }
        dest.writeString(blue_name);
        dest.writeString(red_name);
        dest.writeString(blue_moves);
        dest.writeString(red_moves);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
