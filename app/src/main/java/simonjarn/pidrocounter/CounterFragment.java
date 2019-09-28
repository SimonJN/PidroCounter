package simonjarn.pidrocounter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CounterFragment extends Fragment {

    int blue_score = 0;
    int red_score = 0;

    List<Integer> blue_moves = new ArrayList<Integer>();
    List<Integer> red_moves = new ArrayList<Integer>();

    int selected_team = 0; //0=red , 1=blue
    int selected_action = 0; //0=win, 1=fail

    int last_changed = 0; //0=red, 1=blue

    Button fail_button;
    Button win_button;

    TextView red_score_text;
    TextView blue_score_text;
    GridLayout blue_moves_grid;
    GridLayout red_moves_grid;

    public CounterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root_view = inflater.inflate(R.layout.counter_fragment, container, false);
        //Get elements
        GridLayout number_grid = root_view.findViewById(R.id.number_grid);
        red_score_text = root_view.findViewById(R.id.redTeamScore);
        blue_score_text = root_view.findViewById(R.id.blueTeamScore);
        blue_moves_grid = root_view.findViewById(R.id.blueTeamMoves);
        red_moves_grid = root_view.findViewById(R.id.redTeamMoves);
        win_button = root_view.findViewById(R.id.winButton);
        fail_button = root_view.findViewById(R.id.failButton);
        final ImageButton restart_button = root_view.findViewById(R.id.restart);
        final ImageButton undo_button = root_view.findViewById(R.id.undo);

        for (int move : blue_moves) {
            TextView move_text = (TextView) inflater.inflate(R.layout.team_move_text, blue_moves_grid, false);
            move_text.setText(Integer.toString(move));
            blue_moves_grid.addView(move_text);
        }

        for (int move : red_moves) {
            TextView move_text = (TextView) inflater.inflate(R.layout.team_move_text, blue_moves_grid, false);
            move_text.setText(Integer.toString(move));
            blue_moves_grid.addView(move_text);
        }

//        blue_score_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeTeam(1);
//            }
//        });
//
//        red_score_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeTeam(0);
//            }
//        });

        win_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAction(0);
            }
        });

        fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAction(1);
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
                        last_changed = Math.abs(last_changed-1);
                        if (blue_moves.size() > 4) {
                            TextView move = (TextView) inflater.inflate(R.layout.team_move_text, blue_moves_grid, false);
                            move.setText(Integer.toString(blue_moves.get(blue_moves.size()-1-4)));
                            blue_moves_grid.addView(move,0);
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
                            move.setText(Integer.toString(red_moves.get(red_moves.size()-1-4)));
                            red_moves_grid.addView(move, 0);
                        }
                    }
                }
            }
        });

        restart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog ad = AskOption();
                ad.show();
            }
        });

        blue_score_text.setText(Integer.toString(blue_score));

        for (int i = 0; i < 15; i++) {
            Button bt = (Button) inflater.inflate(R.layout.number_button, number_grid, false);
            if (i != 14) {
                bt.setText(Integer.toString(i+1));
            } else {
                bt.setText(Integer.toString(28));
            }

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int points = Integer.parseInt(((Button)view).getText().toString());
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
                }
            });
            number_grid.addView(bt);
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
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle(getResources().getString(R.string.delete))
                .setMessage(getResources().getString(R.string.ask_delete))

                .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which_button) {
                        //your deleting code
                        blue_moves = new ArrayList<Integer>();
                        red_moves = new ArrayList<Integer>();
                        red_score = 0;
                        blue_score = 0;
                        blue_moves_grid.removeAllViews();
                        red_moves_grid.removeAllViews();
                        red_score_text.setText("0");
                        blue_score_text.setText("0");
                        changeAction(0);
                        changeTeam(0);
                        last_changed = 0;
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which_button) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;}
}
