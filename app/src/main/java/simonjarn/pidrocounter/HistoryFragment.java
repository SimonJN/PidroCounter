package simonjarn.pidrocounter;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    private List<Game> games;

    private LinearLayout active_games;

    private FragmentManager fragment_manager;
    private Fragment counter_fragment;
    private BottomNavigationView main_nav;

    public HistoryFragment(FragmentManager fm, Fragment counter, BottomNavigationView nav) {
        // Store these for use in transitions
        fragment_manager = fm;
        counter_fragment = counter;
        main_nav = nav;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.history_fragment, container, false);
        active_games = root_view.findViewById(R.id.active_games);

        GetGamesAsync gt = new GetGamesAsync();
        gt.execute();
        return root_view;
    }

    private void updateList() {
        for (Game g : games) {
            LinearLayout item = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_item, active_games, false);
            TextView title = (TextView) item.findViewById(R.id.saved_title);
            TextView last_played = (TextView) item.findViewById(R.id.saved_last_played);
            title.setText((String) g.name);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            last_played.setText(getResources().getString(R.string.last_played) + " " + formatter.format(g.last_played));
            active_games.addView(item);
            //Attach the game id data to the item
            item.setTag(g.id);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Attach the game data to the fragment
                    Bundle data = new Bundle();
                    data.putLong("game_id",(Long) view.getTag());
                    counter_fragment.setArguments(data);
                    fragment_manager.beginTransaction().replace(R.id.main_container, counter_fragment).addToBackStack(null).commit();
                    main_nav.setSelectedItemId(R.id.action_counter);
                }
            });
        }
    }

    public class GetGamesAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final GamesDatabase db = Room.databaseBuilder(getContext(),
                    GamesDatabase.class, "Games").fallbackToDestructiveMigration().build();
            games = db.gamesDAO().getAll();
            db.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateList();
        }
    }

}
