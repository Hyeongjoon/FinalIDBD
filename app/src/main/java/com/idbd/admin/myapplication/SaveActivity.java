package com.idbd.admin.myapplication;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.idbd.admin.myapplication.Helper.FindEventId;
import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.Post;
import com.idbd.admin.myapplication.Helper.TokenInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by admin on 2017-01-07.
 */

@EActivity(R.layout.activity_save)
public class SaveActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    private String save_url = "http://52.78.18.19/sche_save";
    FirebaseUser mUser;

    @ViewById(R.id.save_weekView)
    WeekView weekView;
    ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

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
                    case 1 : result = "9:30";
                        break;
                    case 2 : result = "10:00";
                        break;
                    case 3 : result = "10:30";
                        break;
                    case 4 : result = "11:00";
                        break;
                    case 5 : result = "11:30";
                        break;
                    case 6 : result = "12:00";
                        break;
                    case 7 : result = "12:30";
                        break;
                    case 8 : result = "13:00";
                        break;
                    case 9 : result = "13:30";
                        break;
                    case 10 : result = "14:00";
                        break;
                    case 11 : result = "14:30";
                        break;
                    case 12 : result = "15:00";
                        break;
                    case 13 : result = "15:30";
                        break;
                    case 14 : result = "16:00";
                        break;
                    case 15 : result = "16:30";
                        break;
                    case 16 : result = "17:00";
                        break;
                    case 17 : result = "17:30";
                        break;
                    case 18 : result = "18:00";
                        break;
                    case 19 : result = "18:30";
                        break;
                    case 20 : result = "19:00";
                        break;
                    case 21 : result = "19:30";
                        break;
                    case 22 : result = "20:00";
                        break;
                    case 23 : result = "20:30";
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
        cal.set(Calendar.DATE , 9);
        weekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                Calendar startTime = (Calendar) time.clone();
                startTime.set(Calendar.MINUTE , 0);
                startTime.set(Calendar.SECOND , 0);
                Calendar endTime = (Calendar) startTime.clone(); //이거 순서 바뀌면 안됨 스타트타임 기준으로 클론한거니까 >.<
                endTime.add(Calendar.HOUR, 1);
                // Create a new event.
                WeekViewEvent event = new WeekViewEvent(FindEventId.find(startTime), "", startTime, endTime);
                if(events.contains(event)){
                    events.remove(event);
                } else{
                    events.add(event);
                }
                // Refresh the week view. onMonthChange will be called again.
                weekView.notifyDatasetChanged();
            }
        });
        weekView.goToDate(cal);
    }

    @AfterViews
    protected void init() {
        initWeekView();
        weekView.setMonthChangeListener(mMonthChangeListener);
        setupDateTimeInterpreter(false);
        weekView.notifyDatasetChanged();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        makeDialog("수업이 있는 시간대를 클릭하여\n 시간표를 완성해 주세요.");
    }

    @Click(R.id.save_btn)
    void go_to_login(){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            makePDialog();
            dialogInterface.cancel();
            sche_save();
        }};
        makeTwoBtnDialog("확인을 누르시면 시간표가 저장이 됩니다.\n추후 마이페이지에서 새로 설정 가능합니다." , listener);
    }

    @Background
    void sche_save(){
            final FormBody.Builder temp = new FormBody.Builder();

            for(int i = 0 ; i < events.size() ; i++){
                temp.add(i+"" , ""+events.get(i).getId());
            }
            temp.add("size" , events.size()+"");
            RequestBody formbody = temp.add("token" , TokenInfo.getTokenId()).build();
        try {
            JSONObject jsonObject = new JSONObject(Post.post(save_url  , formbody));
            String result = jsonObject.get("result").toString();
            if(result.equals("true")){

                if(pDialog!=null) {
                    pDialog.cancel();
                }
                goMain();

            } else{
                if(pDialog!=null) {
                    pDialog.cancel();
                }
                makeDialog("내부 서버 오류입니다. 잠시후에 시도해주세요");
            }
        } catch (JSONException e) {
            if(pDialog!=null) {
                pDialog.cancel();
            }
            e.printStackTrace();
            makeDialog("내부 서버 오류입니다. 잠시후에 시도해주세요");
        } catch (IOException e) {
            e.printStackTrace();

            if(pDialog!=null) {
                pDialog.cancel();
            }             //Post받다가 오류나면 여길로 바로오니까 2번적어줘야하겠지
            makeDialog("내부 서버 오류입니다. 잠시후에 시도해주세요");
        }
    }


    @UiThread
    protected void makePDialog(){
        pDialog = ProgressDialog.show(this, "저장중 입니다...", "Please wait", true, false);
    }

    @UiThread
    protected void makeDialog(String content){
        MakeDialog.oneBtnDialog(this , content);
    }

    @UiThread
    public void makeTwoBtnDialog(String content , DialogInterface.OnClickListener listener ){
        MakeDialog.twoBtn(this , content , listener);
    }

    public void goMain(){
        if(pDialog!=null) {
            pDialog.dismiss();
        }
        MainActivity_.intent(this).start();
        finish();
    }
}
