package cuexpo.cuexpo2017.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import cuexpo.cuexpo2017.R;
import cuexpo.cuexpo2017.adapter.CityListAdapter;
import cuexpo.cuexpo2017.view.ExpandableHeightListView;


public class CityListFragment extends Fragment {

    ExpandableHeightListView city;
    CityListAdapter cityListAdapter;

    public CityListFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static CityListFragment newInstance() {
        CityListFragment fragment = new CityListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cities_list, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        city = (ExpandableHeightListView) rootView.findViewById(R.id.cities);
        cityListAdapter = new CityListAdapter();
        city.setExpanded(true);
        city.setAdapter(cityListAdapter);
        city.setOnItemClickListener(cityItemClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    /**
     * Listener
     */

    AdapterView.OnItemClickListener cityItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SharedPreferences activitySharedPref = getActivity().getSharedPreferences("Zone", Context.MODE_PRIVATE);

            switch(position) {
                case 0:
                    activitySharedPref.edit().putString("ZoneName", "SMART").apply();
                    activitySharedPref.edit().putInt("FacultyId", 12).apply();
                    break;
                case 1:
                    activitySharedPref.edit().putString("ZoneName", "HEALTH").apply();
                    activitySharedPref.edit().putInt("FacultyId", 11).apply();
                    break;
                case 2:
                    activitySharedPref.edit().putString("ZoneName", "HUMAN").apply();
                    activitySharedPref.edit().putInt("FacultyId", 10).apply();
                    break;
                case 3:
                    activitySharedPref.edit().putString("ZoneName", "HALL").apply();
                    activitySharedPref.edit().putInt("FacultyId", 1).apply();
                    break;
                case 4:
                    activitySharedPref.edit().putString("ZoneName", "SALA").apply();
                    activitySharedPref.edit().putInt("FacultyId", 2).apply();
                    break;
                case 5:
                    activitySharedPref.edit().putString("ZoneName", "ART").apply();
                    activitySharedPref.edit().putInt("FacultyId", 4).apply();
                    break;
                case 6:
                    activitySharedPref.edit().putString("ZoneName", "INTERFORUM").apply();
                    activitySharedPref.edit().putInt("FacultyId", 3).apply();
                    break;
            }

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, new ZoneMainPageFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

}
