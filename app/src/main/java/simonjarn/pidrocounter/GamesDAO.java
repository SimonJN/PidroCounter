package simonjarn.pidrocounter;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GamesDAO {
    @Query("SELECT * FROM game")
    List<Game> getAll();

    @Insert
    void insertGame(Game game);
}
