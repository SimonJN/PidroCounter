package simonjarn.pidrocounter;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View settings_fragment = inflater.inflate(R.layout.settings_fragment, container, false);
        // Set the privacy policy link to be clickable
        TextView privacy_policy_text = settings_fragment.findViewById(R.id.privacy_policy);
        privacy_policy_text.setMovementMethod(LinkMovementMethod.getInstance());
        return settings_fragment;
    }

}
