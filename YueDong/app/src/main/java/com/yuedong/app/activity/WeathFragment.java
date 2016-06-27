package com.yuedong.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Common.ApiUrl;
import com.yuedong.app.Model.User;
import com.yuedong.app.Model.WeatherModel;
import com.yuedong.app.R;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class WeathFragment extends android.support.v4.app.Fragment {

    public Context mContext;
    public BaseApplication mApplicationContext;
    public LocationClient mLocationClient;
    public String City = "shanghai";

    public View mCurrentView;

    public TextView Citys;
    public TextView Date;
    public TextView WeatherMax;
    public TextView WeathDes;
    public TextView Wind;
    public TextView Suggest;
    public ImageView TP;

    public TextView WeatherMax1;
    public TextView WeathDes1;
    public TextView Wind1;
    public ImageView TP1;

    public TextView WeatherMax2;
    public TextView WeathDes2;
    public TextView Wind2;
    public ImageView TP2;

    public WeathFragment() {
    }

    @SuppressLint("ValidFragment")
    public WeathFragment(Context context, LocationClient client,BaseApplication mApplicationContext) {
        this.mContext = context;
        this.mApplicationContext=mApplicationContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCurrentView = inflater.inflate(R.layout.fragment_weath, container, false);
        init();
        return mCurrentView;
    }

    private void init() {

        Citys = (TextView) mCurrentView.findViewById(R.id.cityField);
        Date = (TextView) mCurrentView.findViewById(R.id.date_y);
        WeatherMax = (TextView) mCurrentView.findViewById(R.id.currentTemp);
        WeathDes = (TextView) mCurrentView.findViewById(R.id.currentWeather);
        Suggest = (TextView) mCurrentView.findViewById(R.id.index_d);
        Wind = (TextView) mCurrentView.findViewById(R.id.currentWind);
        TP = (ImageView) mCurrentView.findViewById(R.id.weather_icon01);

        WeatherMax1 = (TextView) mCurrentView.findViewById(R.id.temp02);
        WeathDes1 = (TextView) mCurrentView.findViewById(R.id.weather02);
        Wind1 = (TextView) mCurrentView.findViewById(R.id.wind02);
        TP1 = (ImageView) mCurrentView.findViewById(R.id.weather_icon02);

        WeatherMax2 = (TextView) mCurrentView.findViewById(R.id.temp03);
        WeathDes2 = (TextView) mCurrentView.findViewById(R.id.weather03);
        Wind2 = (TextView) mCurrentView.findViewById(R.id.wind03);
        TP2 = (ImageView) mCurrentView.findViewById(R.id.weather_icon03);

    }

    private void getCityWeather() {
        HanyuPinyinOutputFormat hpyformat = new HanyuPinyinOutputFormat();
        hpyformat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        hpyformat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        if(mApplicationContext.CurrentCity==null){
            return;
        }
        char[] chars = mApplicationContext.CurrentCity.replace("省", "").replace("市", "").replace("区", "").replace("县", "").toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : chars) {
            try {
                String[] list = PinyinHelper.toHanyuPinyinStringArray(ch, hpyformat);
                if (list.length > 0) {
                    sb.append(list[0]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
                continue;
            }
        }
        City = sb.toString();
        GetWeather(City);
    }



    public void GetWeather(String City) {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiUrl.getWeathUrl() + "?city=" + City, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray objectArray = jsonObject.getJSONArray("HeWeather data service 3.0");
                    if (objectArray != null && objectArray.length() > 0) {
                        JSONObject object = objectArray.getJSONObject(0);
                        WeatherModel now = new WeatherModel();
                        now.City = object.getJSONObject("basic").getString("city");
                        now.Date = object.getJSONObject("basic").getJSONObject("update").getString("loc");
                        now.Suggest = object.getJSONObject("suggestion").getJSONObject("drsg").getString("txt");
                        now.WeathDes = object.getJSONObject("now").getJSONObject("cond").getString("txt");
                        now.WeatherMax = object.getJSONObject("now").getString("tmp");
                        now.WeatherMin = object.getJSONObject("now").getString("tmp");
                        now.Wind = object.getJSONObject("now").getJSONObject("wind").getString("dir") + object.getJSONObject("now").getJSONObject("wind").getString("sc") + "级";

                        Citys.setText(now.City);
                        Suggest.setText(now.Suggest);
                        Date.setText(now.Date);
                        WeatherMax.setText(now.WeatherMax + "℃");
                        WeathDes.setText(now.WeathDes);
                        TP.setImageResource(getWeatherBitMapResource(now.WeathDes));
                        Wind.setText(now.Wind);

                        JSONObject tomobj = object.getJSONArray("daily_forecast").getJSONObject(0);
                        WeatherModel tom = new WeatherModel();
                        tom.City = object.getJSONObject("basic").getString("city");
                        tom.Date = tomobj.getString("date");
                        tom.WeathDes = tomobj.getJSONObject("cond").getString("txt_d");
                        tom.WeatherMax = tomobj.getJSONObject("tmp").getString("max");
                        tom.WeatherMin = tomobj.getJSONObject("tmp").getString("min");
                        tom.Wind = tomobj.getJSONObject("wind").getString("dir") + object.getJSONObject("now").getJSONObject("wind").getString("sc") + "级";

                        Wind1.setText(tom.Wind);
                        WeatherMax1.setText(tom.WeatherMin + "℃ ~ " + tom.WeatherMax + "℃");
                        WeathDes1.setText(tom.WeathDes);
                        TP1.setImageResource(getWeatherBitMapResource(tom.WeathDes));


                        JSONObject dtomobj = object.getJSONArray("daily_forecast").getJSONObject(1);
                        WeatherModel dtom = new WeatherModel();
                        dtom.City = object.getJSONObject("basic").getString("city");
                        dtom.Date = dtomobj.getString("date");
                        dtom.WeathDes = dtomobj.getJSONObject("cond").getString("txt_d");
                        dtom.WeatherMax = dtomobj.getJSONObject("tmp").getString("max");
                        dtom.WeatherMin = dtomobj.getJSONObject("tmp").getString("min");
                        dtom.Wind = dtomobj.getJSONObject("wind").getString("dir") + object.getJSONObject("now").getJSONObject("wind").getString("sc") + "级";

                        Wind2.setText(dtom.Wind);
                        WeatherMax2.setText(dtom.WeatherMin + "℃ ~ " + dtom.WeatherMax + "℃");
                        WeathDes2.setText(dtom.WeathDes);
                        TP2.setImageResource(getWeatherBitMapResource(dtom.WeathDes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("apikey", ApiUrl.getApiWeather());
                header.put("Content-Type", "application/x-www-form-urlencoded");
                return header;
            }

            @Override
            protected String getParamsEncoding() {
                return super.getParamsEncoding();
            }
        };
        request.setTag(this);
        ((BaseApplication) getActivity().getApplication()).getInstance().addToRequestQueue(request, mContext.toString());
    }

    public static int getWeatherBitMapResource(String weather) {
        Log.i("weather_info", "=============" + weather + "===============");
        if (weather.equals("晴")) {
            return R.drawable.weathericon_condition_01;
        } else if (weather.equals("多云")) {
            return R.drawable.weathericon_condition_02;
        } else if (weather.equals("阴")) {
            return R.drawable.weathericon_condition_04;
        } else if (weather.equals("雾")) {
            return R.drawable.weathericon_condition_05;
        } else if (weather.equals("沙尘暴")) {
            return R.drawable.weathericon_condition_06;
        } else if (weather.equals("阵雨")) {
            return R.drawable.weathericon_condition_07;
        } else if (weather.equals("小雨") || weather.equals("小到中雨")) {
            return R.drawable.weathericon_condition_08;
        } else if (weather.equals("大雨")) {
            return R.drawable.weathericon_condition_09;
        } else if (weather.equals("雷阵雨")) {
            return R.drawable.weathericon_condition_10;
        } else if (weather.equals("小雪")) {
            return R.drawable.weathericon_condition_11;
        } else if (weather.equals("大雪")) {
            return R.drawable.weathericon_condition_12;
        } else if (weather.equals("雨夹雪")) {
            return R.drawable.weathericon_condition_13;
        } else {
            return R.drawable.weathericon_condition_17;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("偏偏","onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("偏偏","onresume");
        getCityWeather();
    }

    @Override
    public void onStart() {
        Log.i("偏偏","onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("偏偏","onStop");
    }
}
