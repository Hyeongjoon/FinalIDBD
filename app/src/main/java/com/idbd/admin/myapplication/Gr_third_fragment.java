package com.idbd.admin.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.AutoScrollHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.idbd.admin.myapplication.Helper.FindEventId;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 2017-01-12.
 */



@EFragment(R.layout.gr_layout3)
public class Gr_third_fragment extends Fragment{

    @ViewById(R.id.gr_layout3_weekview)
    WeekView weekView;
    ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

    FirebaseUser mUser;

    MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // Populate the week view with some events.

            return events;
        }
    };

    private void setupDateTimeInterpreter(final boolean shortDate) {

        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {

            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());

                // All android api level do not have a standard way of getting the first letter of

                // the week day name. Hence we get the first char programmatically.

                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657

                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));

                String result =weekday;

                return result;
            }

            @Override
            public String interpretTime(int hour) {
                String result ="";
                switch (hour){
                    case 0 : result = "9:00";
                        break;
                    case 1 : result = "";
                        break;
                    case 2 : result = "10:00";
                        break;
                    case 3 : result = "";
                        break;
                    case 4 : result = "11:00";
                        break;
                    case 5 : result = "";
                        break;
                    case 6 : result = "12:00";
                        break;
                    case 7 : result = "";
                        break;
                    case 8 : result = "13:00";
                        break;
                    case 9 : result = "";
                        break;
                    case 10 : result = "14:00";
                        break;
                    case 11 : result = "";
                        break;
                    case 12 : result = "15:00";
                        break;
                    case 13 : result = "";
                        break;
                    case 14 : result = "16:00";
                        break;
                    case 15 : result = "";
                        break;
                    case 16 : result = "17:00";
                        break;
                    case 17 : result = "";
                        break;
                    case 18 : result = "18:00";
                        break;
                    case 19 : result = "";
                        break;
                    case 20 : result = "19:00";
                        break;
                    case 21 : result = "";
                        break;
                    case 22 : result = "20:00";
                        break;
                    case 23 : result = "";
                        break;
                }
                return result;
            }
        });
    }

    protected void initWeekView(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR , 2017);
        cal.set(Calendar.MONTH , 0);
        cal.set(Calendar.DATE , 15);
        if(!Gr_info_Activity.gr_sche.equals("null")){  //시간표 없으면 null로 넘기게 해놓음 아니면 Try Catch문으로 가게되어서 ㅠㅠㅠㅠㅠ
            try {
                JSONArray jsonArray = new JSONArray(Gr_info_Activity.gr_sche);
                for(int j = 0 ; j < jsonArray.length() ; j++){
                    JSONObject pre_info = jsonArray.getJSONObject(j);
                    for (int i = 0; i < pre_info.length()-1; i++) { //jsonObject 길이는 jsonObject에 포함 된 uid 땜에 1개 빼야됨
                        if (pre_info.getInt("" + i) == 1) {
                            Calendar start_temp = FindEventId.idToCalender(i);
                            Calendar end_temp = (Calendar) start_temp.clone();
                            end_temp.add(Calendar.HOUR, 1);
                            events.add(new WeekViewEvent(i, "", start_temp, end_temp));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        weekView.goToDate(cal);
        int height = weekView.getHeight();
        int padding = weekView.getHeaderRowPadding();
        height = height - (2 * padding);
        weekView.setHourHeight(height / 24);
        weekView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                switch (action){
                    case MotionEvent.AXIS_HSCROLL :{
                        return false;
                    }case MotionEvent.AXIS_VSCROLL :{
                        return true;
                    }default:{
                        return true;
                    }
                }
            }
        });
    }

    @AfterViews
    protected void init() {
        weekView.setMonthChangeListener(mMonthChangeListener);
        initWeekView();
        setupDateTimeInterpreter(false);
        weekView.notifyDatasetChanged();
    }

}
