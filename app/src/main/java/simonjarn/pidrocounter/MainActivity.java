package simonjarn.pidrocounter;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    public FragmentManager fm = getSupportFragmentManager();

    public Fragment counter_fragment = new CounterFragment();
    public Fragment history_fragment;
    public Fragment settings_fragment = new SettingsFragment();

    public BottomNavigationView main_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_nav = (BottomNavigationView) findViewById(R.id.main_navigation);
        //Set the middle button as selected
        main_nav.setSelectedItemId(R.id.action_counter);

        history_fragment = new HistoryFragment(fm, counter_fragment, main_nav);
        //Load the first fragment
        fm.beginTransaction().replace(R.id.main_container, counter_fragment).addToBackStack(null).commit();
        //Set the click listener for the navigation
        main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_history:
                        fm.beginTransaction().replace(R.id.main_container, history_fragment).addToBackStack(null).commit();
                        return true;
                    case R.id.action_counter:
                        fm.beginTransaction().replace(R.id.main_container, counter_fragment).addToBackStack(null).commit();
                        return true;
                    case R.id.action_settings:
                        fm.beginTransaction().replace(R.id.main_container, settings_fragment).addToBackStack(null).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
