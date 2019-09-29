package simonjarn.pidrocounter;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    List<Game> games;

    LinearLayout active_games;

    FragmentManager fragment_manager;
    Fragment counter_fragment;
    BottomNavigationView main_nav;

    public HistoryFragment(FragmentManager fm, Fragment counter, BottomNavigationView nav) {
        // Required empty public constructor
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
    public void updateList() {
        for (Game g : games) {
            LinearLayout item = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_item, active_games, false);
            TextView title = (TextView) item.findViewById(R.id.saved_title);
            TextView last_played = (TextView) item.findViewById(R.id.saved_last_played);
            title.setText((String) g.name);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            last_played.setText(getResources().getString(R.string.last_played) + " " + formatter.format(g.last_played));
            active_games.addView(item);
            //Attach the game data to the item
            item.setTag(g);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Attach the game data to
                    Bundle data = new Bundle();
                    data.putParcelable("game", (Parcelable) view.getTag());
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
