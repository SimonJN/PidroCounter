package simonjarn.pidrocounter;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GamesDAO {
    @Query("SELECT * FROM game ORDER BY last_played DESC")
    List<Game> getAll();

    @Insert
    long insertGame(Game game);

    @Query("DELETE from game where id=:id")
    void deleteGame(long id);
}
