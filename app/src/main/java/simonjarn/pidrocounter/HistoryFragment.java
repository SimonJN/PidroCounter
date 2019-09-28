package simonjarn.pidrocounter;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    List<Game> games;

    LinearLayout active_games;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.history_fragment, container, false);
        active_games = root_view.findViewById(R.id.active_games);

        GetTimersAsync gt = new GetTimersAsync();
        gt.execute();
        return root_view;
    }
    public void updateList() {
        for (int i = 0; i < games.size(); i++) {
            Log.d("DATABAJS", games.get(i).name);
            LinearLayout item = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_item, active_games, false);
            active_games.addView(item);
        }
    }

    public class GetTimersAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final GamesDatabase db = Room.databaseBuilder(getContext(),
                    GamesDatabase.class, "Games").build();
            games = db.gamesDAO().getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateList();
        }
    }

}
