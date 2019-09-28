package simonjarn.pidrocounter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Game.class}, version = 1)
public abstract class GamesDatabase extends RoomDatabase {
    public abstract GamesDAO gamesDAO();
}
