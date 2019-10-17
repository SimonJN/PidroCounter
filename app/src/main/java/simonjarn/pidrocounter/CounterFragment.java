package simonjarn.pidrocounter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;


/**
 * A simple {@link Fragment} subclass.
 */
public class CounterFragment extends Fragment {
    GridLayout blue_moves_grid;
    private long currently_loaded = -1; //-1=unset
    private View root_view;
    private int blue_score = 0;
    private int red_score = 0;
    private List<Integer> blue_moves = new ArrayList<Integer>();
    private List<Integer> red_moves = new ArrayList<Integer>();
    private int selected_team = 0; //0=red , 1=blue
    private int selected_action = 0; //0=win, 1=fail
    private int last_changed = 0; //0=red, 1=blue
    private Button fail_button;
    private Button win_button;
    private TextView red_score_text;
    private TextView blue_score_text;
    private GridLayout red_moves_grid;

    private TextView name_edit;
    private TextView blue_name_edit;
    private TextView red_name_edit;

    private String name;
    private String blue_name;
    private String red_name;

    private GridLayout number_grid;

    private ImageButton restart_button;
    private ImageButton undo_button;
    private Button new_game_button;


    public CounterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Date d = new Date();
        name = formatter.format(d);
        red_name = getResources().getString(R.string.we);
        blue_name = getResources().getString(R.string.they);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d("fail", "Currently loaded: " + Long.toString(currently_loaded));
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.counter_fragment, container, false);
        //Get elements
        number_grid = root_view.findViewById(R.id.number_grid);
        red_score_text = root_view.findViewById(R.id.redTeamScore);
        blue_score_text = root_view.findViewById(R.id.blueTeamScore);
        blue_moves_grid = root_view.findViewById(R.id.blueTeamMoves);
        red_moves_grid = root_view.findViewById(R.id.redTeamMoves);
        win_button = root_view.findViewById(R.id.winButton);
        fail_button = root_view.findViewById(R.id.failButton);
        restart_button = root_view.findViewById(R.id.restart);
        undo_button = root_view.findViewById(R.id.undo);
        new_game_button = root_view.findViewById(R.id.new_game);

        name_edit = root_view.findViewById(R.id.title);
        blue_name_edit = root_view.findViewById(R.id.blueTeamName);
        red_name_edit = root_view.findViewById(R.id.redTeamName);

        //Setup most of the click listeners
        name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog d = ChangeName((TextView) view, 0);
                d.show();
            }
        });

        blue_name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog d = ChangeName((TextView) view, 1);
                d.show();
            }
        });

        red_name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog d = ChangeName((TextView) view, 2);
                d.show();
            }
        });

        win_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAction(0);
                SaveGameAsync s = new SaveGameAsync();
                s.execute();
            }
        });

        fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAction(1);
                SaveGameAsync s = new SaveGameAsync();
                s.execute();
            }
        });

        undo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (last_changed == 1) {
                    if (blue_moves.size() != 0) {
                        int last_child = blue_moves.size() - 1;
                        int last_move_text = blue_moves_grid.getChildCount() - 1;
                        int last_change = blue_moves.get(last_child);
                        blue_score -= last_change;
                        blue_score_text.setText(Integer.toString(blue_score));
                        blue_moves.remove(last_child);
                        blue_moves_grid.removeViewAt(last_move_text);
                        changeTeam(1);
                        last_changed = Math.abs(last_changed - 1);
                        if (blue_moves.size() > 4) {
                            TextView move = (TextView) inflater.inflate(R.layout.team_move_text, blue_moves_grid, false);
                            move.setText(Integer.toString(blue_moves.get(blue_moves.size() - 1 - 4)));
                            blue_moves_grid.addView(move, 0);
                        }
                    }
                } else if (last_changed == 0) {
                    if (red_moves.size() != 0) {
                        int last_child = red_moves.size() - 1;
                        int last_move_text = red_moves_grid.getChildCount() - 1;
                        int last_change = red_moves.get(last_child);
                        red_score -= last_change;
                        red_score_text.setText(Integer.toString(red_score));
                        red_moves.remove(last_child);
                        red_moves_grid.removeViewAt(last_move_text);
                        changeTeam(0);
                        last_changed = Math.abs(last_changed - 1);
                        if (red_moves.size() > 4) {
                            TextView move = (TextView) inflater.inflate(R.layout.team_move_text, red_moves_grid, false);
                            move.setText(Integer.toString(red_moves.get(red_moves.size() - 1 - 4)));
                            red_moves_grid.addView(move, 0);
                        }
                    }
                }
                SaveGameAsync s = new SaveGameAsync();
                s.execute();
            }
        });

        restart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog ad = AskDelete();
                ad.show();
            }
        });

        new_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        //Generate the number buttons programmatically and set their clicklisteners
        for (int i = 0; i < 15; i++) {
            Button bt = (Button) inflater.inflate(R.layout.number_button, number_grid, false);
            bt.setText(Integer.toString(i));

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int points = Integer.parseInt(((Button) view).getText().toString());
                    if (selected_action == 1) {
                        points = points * -1;
                    }
                    if (selected_team == 1) {
                        blue_score += points;
                        blue_score_text.setText(Integer.toString(blue_score));
                        TextView move = (TextView) inflater.inflate(R.layout.team_move_text, blue_moves_grid, false);
                        move.setText(Integer.toString(points));
                        blue_moves_grid.addView(move);
                        blue_moves.add(points);
                        changeTeam(Math.abs(selected_team - 1));
                        last_changed = 1;
                        if (blue_moves_grid.getChildCount() > 5) {
                            blue_moves_grid.removeViewAt(0);
                        }
                    } else {
                        red_score += points;
                        red_score_text.setText(Integer.toString(red_score));
                        TextView move = (TextView) inflater.inflate(R.layout.team_move_text, red_moves_grid, false);
                        move.setText(Integer.toString(points));
                        red_moves_grid.addView(move);
                        red_moves.add(points);
                        changeTeam(Math.abs(selected_team - 1));
                        last_changed = 0;
                        if (red_moves_grid.getChildCount() > 5) {
                            red_moves_grid.removeViewAt(0);
                        }
                    }
                    if (selected_action == 1) {
                        changeAction(0);
                    }
                    SaveGameAsync s = new SaveGameAsync();
                    s.execute();
                }
            });
            number_grid.addView(bt);
        }

        //If the fragment was initialized with information from the initializer, try to load from that game
        long load_id = currently_loaded;
        try {
            load_id = (long) getArguments().get("game_id");
            //Log.d("fail", "BUNDLED DATA EXISTS");
            getArguments().clear();
        } catch (Exception e) {
            //Log.d("fail", "no bundled data");
        }
        //Log.d("fail", "Load id: " + load_id);
        if (load_id != currently_loaded) {
            LoadGameAsync l = new LoadGameAsync();
            l.execute(load_id);
        } else {
            //Log.d("fail", "did not load game");
            //If no game was attempting to load, setup normally
            setupUI();
        }

        return root_view;
    }

    private void changeTeam(int team) {
        selected_team = team;
        if (selected_action == 0) {
            if (selected_team == 0) {
                win_button.setBackground(getActivity().getResources().getDrawable(R.drawable.win_fail_button_background_red));
            } else {
                win_button.setBackground(getActivity().getResources().getDrawable(R.drawable.win_fail_button_background_blue));
            }
        } else {
            if (selected_team == 0) {
                fail_button.setBackground(getActivity().getResources().getDrawable(R.drawable.win_fail_button_background_red));
            } else {
                fail_button.setBackground(getActivity().getResources().getDrawable(R.drawable.win_fail_button_background_blue));
            }
        }
    }

    private void changeAction(int action) {
        selected_action = action;
        if (selected_action == 1) {
            win_button.setBackground(getActivity().getResources().getDrawable(R.drawable.button_outline));
            if (selected_team == 0) {
                fail_button.setBackground(getActivity().getResources().getDrawable(R.drawable.win_fail_button_background_red));
            } else {
                fail_button.setBackground(getActivity().getResources().getDrawable(R.drawable.win_fail_button_background_blue));
            }
        } else {
            fail_button.setBackground(getActivity().getResources().getDrawable(R.drawable.button_outline));
            if (selected_team == 0) {
                win_button.setBackground(getActivity().getResources().getDrawable(R.drawable.win_fail_button_background_red));
            } else {
                win_button.setBackground(getActivity().getResources().getDrawable(R.drawable.win_fail_button_background_blue));
            }
        }
    }

    private void setupUI() {
        //Reset to the latest state when loaded
        LayoutInflater inflater = LayoutInflater.from(getContext());

        blue_moves_grid.removeAllViews();
        for (int move : blue_moves) {
            TextView move_text = (TextView) inflater.inflate(R.layout.team_move_text, blue_moves_grid, false);
            move_text.setText(Integer.toString(move));
            blue_moves_grid.addView(move_text);
            if (blue_moves_grid.getChildCount() > 5) {
                blue_moves_grid.removeViewAt(0);
            }
        }

        red_moves_grid.removeAllViews();
        for (int move : red_moves) {
            TextView move_text = (TextView) inflater.inflate(R.layout.team_move_text, red_moves_grid, false);
            move_text.setText(Integer.toString(move));
            red_moves_grid.addView(move_text);
            if (red_moves_grid.getChildCount() > 5) {
                red_moves_grid.removeViewAt(0);
            }
        }

        changeAction(selected_action);
        changeTeam(selected_team);

        blue_score_text.setText(Integer.toString(blue_score));
        red_score_text.setText(Integer.toString(red_score));

        name_edit.setText((String) name);
        blue_name_edit.setText((String) blue_name);
        red_name_edit.setText((String) red_name);
    }

    private void reset() {
        blue_score = 0;
        red_score = 0;
        last_changed = 0;
        selected_action = 0;
        selected_team = 0;
        //Set the default name_edit, the date
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Date d = new Date();
        name = formatter.format(d);
        red_name = getResources().getString(R.string.we);
        blue_name = getResources().getString(R.string.they);

        blue_moves.clear();

        red_moves.clear();

        currently_loaded = -1;

        setupUI();
    }

    private void loadFromGame(Game g) {
        blue_score = g.blue_score;
        red_score = g.red_score;
        last_changed = g.last_changed;
        selected_action = g.selected_action;
        selected_team = g.selected_team;
        name = g.name;
        red_name = g.red_name;
        blue_name = g.blue_name;

        blue_moves.clear();
        String blue_move_string = g.blue_moves;
        if (!blue_move_string.isEmpty()) {
            String[] blue_moves_split = blue_move_string.split(",");
            //DONT Remove the last element since the string ends with a ",", it skips it!
            for (int i = 0; i < blue_moves_split.length; i++) {
                String move = blue_moves_split[i];
                blue_moves.add(Integer.parseInt(move));
            }
        }

        red_moves.clear();
        String red_move_string = g.red_moves;
        if (!red_move_string.isEmpty()) {
            String[] red_moves_split = red_move_string.split(",");
            //DONT Remove the last element since the string ends with a ",", it skips it!
            for (int i = 0; i < red_moves_split.length; i++) {
                String move = red_moves_split[i];
                red_moves.add(Integer.parseInt(move));
            }
        }

        currently_loaded = g.id;

        setupUI();
    }

    private AlertDialog AskDelete() {
        AlertDialog delete_dialog = new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                .setTitle(getResources().getString(R.string.delete))
                .setMessage(getResources().getString(R.string.ask_delete))

                .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which_button) {
                        //Only delete if something is loaded
                        if (currently_loaded != -1) {
                            DeleteGameAsync dga = new DeleteGameAsync();
                            dga.execute(currently_loaded);
                            reset();
                        }
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which_button) {
                        dialog.dismiss();
                    }
                })
                .create();
        return delete_dialog;
    }

    private AlertDialog ChangeName(TextView text, final int edit_index) {
        final TextView t = text;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.edit_name_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edit = (EditText) dialogView.findViewById(R.id.edit_text);
        edit.setText(text.getText());
        if (edit_index == 0) {
            edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        }

        dialogBuilder.setTitle(getResources().getString(R.string.change_name));
        dialogBuilder.setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                t.setText(edit.getText());
                switch (edit_index) {
                    case 0:
                        name = edit.getText().toString().trim();
                        break;
                    case 1:
                        blue_name = edit.getText().toString().trim();
                        break;
                    case 2:
                        red_name = edit.getText().toString().trim();
                        break;
                }
                SaveGameAsync s = new SaveGameAsync();
                s.execute();
            }
        });
        dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Pass
            }
        });
        return dialogBuilder.create();
    }

    public class SaveGameAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final GamesDatabase db = Room.databaseBuilder(getContext(),
                    GamesDatabase.class, "Games").fallbackToDestructiveMigration().build();
            Game g = new Game();
            g.blue_score = blue_score;
            g.red_score = red_score;
            g.last_changed = last_changed;
            g.selected_action = selected_action;
            g.selected_team = selected_team;
            g.name = name;
            g.red_name = red_name;
            g.blue_name = blue_name;
            g.last_played = new Date().getTime();

            String blue_move_string = "";
            if (blue_moves.size() > 30) {
                for (int move : blue_moves.subList(blue_moves.size() -31, blue_moves.size()-1)) {
                    blue_move_string += Integer.toString(move) + ",";
                }
            } else {
                for (int move : blue_moves) {
                    blue_move_string += Integer.toString(move) + ",";
                }
            }
            //Remove the last comma, it messes stuff up
            if (blue_move_string.length() > 0) {
                blue_move_string = blue_move_string.substring(0, blue_move_string.length()-1);
            }

            String red_move_string = "";
            if (red_moves.size() > 30) {
                for (int move : red_moves.subList(red_moves.size() -31, red_moves.size()-1)) {
                    red_move_string += Integer.toString(move) + ",";
                }
            } else {
                for (int move : red_moves) {
                    red_move_string += Integer.toString(move) + ",";
                }
            }

            //Remove the last comma, it messes stuff up
            if (red_move_string.length() > 0) {
                red_move_string = red_move_string.substring(0, red_move_string.length() - 1);
            }
            g.blue_moves = blue_move_string;
            g.red_moves = red_move_string;

            if (currently_loaded == -1) {
                long id = db.gamesDAO().insertGame(g);
                currently_loaded = id;
            } else {
                db.gamesDAO().deleteGame(currently_loaded);
                long id = db.gamesDAO().insertGame(g);
                //Set it again when it has a new id
                currently_loaded = id;
            }
            db.close();
            return null;
        }
    }

    public class DeleteGameAsync extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... ids) {
            final GamesDatabase db = Room.databaseBuilder(getContext(),
                    GamesDatabase.class, "Games").fallbackToDestructiveMigration().build();
            db.gamesDAO().deleteGame(ids[0]);
            db.close();
            return null;
        }
    }
    public class LoadGameAsync extends AsyncTask<Long, Void, Void> {
        Game g;
        @Override
        protected Void doInBackground(Long... ids) {
            final GamesDatabase db = Room.databaseBuilder(getContext(),
                    GamesDatabase.class, "Games").fallbackToDestructiveMigration().build();
            g = db.gamesDAO().getGameByID(ids[0]);

            db.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                loadFromGame(g);
            } catch (Exception e) {
                //Log.d("fail", "Failed loading! " + e);
            }

        }
    }
}
