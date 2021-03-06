package com.android.yunix77.uniplan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;


public class AddOfficeFragment extends Fragment {
    DatabaseControl   db;
    View              view;
    Button            submit;
    ArrayList<String> input;
    Spinner           day;
    TimePicker        start_time, end_time;
    int               i_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view       = inflater.inflate(R.layout.fragment_add_office, container, false);
        db         = new DatabaseControl(getActivity());
        submit     = (Button) view.findViewById(R.id.submitCourse);
        day        = (Spinner) view.findViewById(R.id.spinnerDay);
        i_id       = this.getArguments().getInt("INSTRUCTOR_ID");
        start_time = (TimePicker) view.findViewById(R.id.startTime);
        end_time   = (TimePicker) view.findViewById(R.id.endTime);

        fillDays(day);

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager inputManager =
                            (InputMethodManager) getActivity().
                                    getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                setUserInput();
                insertDB();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });



        return view;
    }
    public String formatTime(TimePicker time) {
        String output = "";
        int    hour   = time.getCurrentHour();
        int    min    = time.getCurrentMinute();

        if(hour < 10)
            output += "0" + hour + ":";
        else
            output += hour + ":";
        if(min < 10)
            output += "0" + min;
        else
            output += min;

        return output;
    }



    private void insertDB() {
        Cursor cursor;
        String s_time = formatTime(start_time);
        String e_time = formatTime(end_time);


        try {
            db.addTime(i_id, 2, input.get(0), null, s_time, e_time, day.getSelectedItemPosition());
        } catch(Exception e) {
            Log.i("DEBUG", "Err: "+ e.toString());
        }

    }

    private void fillDays(Spinner day) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice);
        adapter.add("Monday");
        adapter.add("Tuesday");
        adapter.add("Wednesday");
        adapter.add("Thursday");
        adapter.add("Friday");
        adapter.add("Saturday");
        adapter.add("Sunday");
        day.setAdapter(adapter);
    }

    private void setUserInput() {
        EditText location   = (EditText) view.findViewById(R.id.editLocation);
        input               = new ArrayList<String>();
        input.add(location.getText().toString());
    }
}
