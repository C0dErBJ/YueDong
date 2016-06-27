package com.yuedong.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuedong.app.Base.BaseActivity;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Common.ApiUrl;
import com.yuedong.app.Model.FreeTimeModel;
import com.yuedong.app.Model.User;
import com.yuedong.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InputSpaceTimeActivity extends BaseActivity {
    //region time
    public TextView time1;
    public TextView time2;
    public TextView time3;
    public TextView time4;
    public TextView time5;
    public TextView time6;
    public TextView time7;
    public TextView time8;
    public TextView time9;
    public TextView time10;
    public TextView time11;
    public TextView time12;
    public TextView time13;
    public TextView time14;
    public TextView time15;
    public TextView time16;
    public TextView time17;
    public TextView time18;
    public TextView time19;
    public TextView time20;
    public TextView time21;
    public TextView time22;
    public TextView time23;
    public TextView time24;
    public TextView time25;
    public TextView time26;
    public TextView time27;
    public TextView time28;
    public TextView time29;
    public TextView time30;
    public TextView time31;
    public TextView time32;
    public TextView time33;
    public TextView time34;
    public TextView time35;
    public TextView time36;
    public TextView time37;
    public TextView time38;
    public TextView time39;
    public TextView time40;
    public TextView time41;
    public TextView time42;

    //endregion
    public Context mContext;

    public TableLayout table;

    public TextView finish;
    public TextView title;
    public ImageView back;

    public HashMap<TextView, Boolean> status = new HashMap<TextView, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_space_time);
        mContext = this;
        init();

    }

    private void init() {
        //region Description
        time1 = $(R.id.time1);
        time1 = $(R.id.time1);
        time2 = $(R.id.time2);
        time3 = $(R.id.time3);
        time4 = $(R.id.time4);
        time5 = $(R.id.time5);
        time6 = $(R.id.time6);
        time7 = $(R.id.time7);
        time8 = $(R.id.time8);
        time9 = $(R.id.time9);
        time10 = $(R.id.time10);
        time11 = $(R.id.time11);
        time12 = $(R.id.time12);
        time13 = $(R.id.time13);
        time14 = $(R.id.time14);
        time15 = $(R.id.time15);
        time16 = $(R.id.time16);
        time17 = $(R.id.time17);
        time18 = $(R.id.time18);
        time19 = $(R.id.time19);
        time20 = $(R.id.time20);
        time21 = $(R.id.time21);
        time22 = $(R.id.time22);
        time23 = $(R.id.time23);
        time24 = $(R.id.time24);
        time25 = $(R.id.time25);
        time26 = $(R.id.time26);
        time27 = $(R.id.time27);
        time28 = $(R.id.time28);
        time29 = $(R.id.time29);
        time30 = $(R.id.time30);
        time31 = $(R.id.time31);
        time32 = $(R.id.time32);
        time33 = $(R.id.time33);
        time34 = $(R.id.time34);
        time35 = $(R.id.time35);
        time36 = $(R.id.time36);
        time37 = $(R.id.time37);
        time38 = $(R.id.time38);
        time39 = $(R.id.time39);
        time40 = $(R.id.time40);
        time41 = $(R.id.time41);
        time42 = $(R.id.time42);
        //endregion

        finish = $(R.id.finish);
        title = $(R.id.title);
        back = $(R.id.back);
        table = $(R.id.table);
        title.setText("空闲时间设置");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFreeTime();
            }
        });
        for (int i = 1; i < table.getChildCount(); i++) {
            for (int j = 1; j < ((TableRow) table.getChildAt(i)).getChildCount(); j++) {
                TextView targetView = (TextView) ((TableRow) table.getChildAt(i)).getChildAt(j);
                targetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getContentDescription().equals("unchecked")) {
                            view.setBackgroundResource(R.drawable.greenbox);
                            view.setContentDescription("checked");
                            if (status.containsKey((TextView) view)) {
                                status.remove((TextView) view);
                            } else {
                                status.put((TextView) view, true);
                            }

                        } else {
                            view.setBackgroundResource(R.drawable.redbox);
                            view.setContentDescription("unchecked");
                            if (status.containsKey((TextView) view)) {
                                status.remove((TextView) view);
                            } else {
                                status.put((TextView) view, false);
                            }
                        }
                    }
                });
            }
        }

        getFreeTime();
    }

    public String getTime(int row) {
        String start = "";
        String end = "";
        switch (row) {
            case 0:
                start = "6:00";
                end = "8:00";
                break;
            case 1:
                start = "8:00";
                end = "12:00";
                break;
            case 2:
                start = "12:00";
                end = "16:00";
                break;
            case 3:
                start = "16:00";
                end = "18:00";
                break;
            case 4:
                start = "18:00";
                end = "20:00";
                break;
            case 5:
                start = "20:00";
                end = "22:00";
                break;

        }
        return start + "|" + end;
    }

    public int getRow(String time) {
        if (time.equals("06:00|08:00")) {
            return 1;
        } else if (time.equals("08:00|12:00")) {
            return 2;
        } else if (time.equals("12:00|16:00")) {
            return 3;
        } else if (time.equals("16:00|18:00")) {
            return 4;
        } else if (time.equals("18:00|20:00")) {
            return 5;
        } else if (time.equals("20:00|22:00")) {
            return 6;
        }
        return 0;
    }

    public void setFreeTime() {
        JSONArray pata = new JSONArray();
        String url = ApiUrl.getUpdateFreeTime();
        Iterator it = status.entrySet().iterator();
        if (status.size() == 0) {
            return;
        }
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Resources res = mContext.getResources();
            String id = res.getResourceEntryName(((TextView) entry.getKey()).getId());
            int num = Integer.parseInt(id.split("time")[1]);
            int week = num % 7;
            int row = num / 7;
            String time = getTime(row);
            try {
                JSONObject para = new JSONObject();
                para.put("UserId", ((BaseApplication) getApplication()).CurrentUser.Id);
                para.put("Week", week);
                para.put("StartTime", time.split("\\|")[0]);
                para.put("EndTime", time.split("\\|")[1]);
                para.put("Status", ((Boolean) entry.getValue()).booleanValue() ? 1 : 0);
                pata.put(para);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final JSONArray p = pata;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (!jsonObject.get("ResultCode").toString().equals("1")) {
                        ((Activity) mContext).finish();
                    } else {
                        Toast.makeText(mContext, jsonObject.get("ResultMessage").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public byte[] getBody() {
                return p.toString().getBytes();
            }
        };
        request.setTag(this);
        ((BaseApplication) getApplication()).getInstance().addToRequestQueue(request, mContext.toString());
    }

    public void getFreeTime() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiUrl.getFreeTime() + ((BaseApplication) getApplication()).CurrentUser.Id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (!jsonObject.get("ResultCode").toString().equals("1")) {
                        ObjectMapper mapper = new ObjectMapper();
                        FreeTimeModel[] free = mapper.readValue(jsonObject.getString("ResultData"), FreeTimeModel[].class);
                        for (FreeTimeModel model : free) {
                            TextView view = (TextView) ((TableRow) table.getChildAt(getRow(model.StartTime + "|" + model.EndTime))).getChildAt(model.Week == 0 ? 7 : model.Week);
                            view.setBackgroundResource(R.drawable.greenbox);
                            view.setContentDescription("checked");
                        }
                    } else {
                        Toast.makeText(mContext, jsonObject.get("ResultMessage").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "更新失败", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(this);
        ((BaseApplication) getApplication()).getInstance().addToRequestQueue(request, mContext.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_space_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
