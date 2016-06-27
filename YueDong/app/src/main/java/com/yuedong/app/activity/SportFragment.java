package com.yuedong.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Common.ApiUrl;
import com.yuedong.app.Model.User;
import com.yuedong.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SportFragment extends android.support.v4.app.Fragment {

    // 定位相关
    public LocationClient mLocClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    boolean isFirstLoc = true;// 是否首次定位
    public MyLocationListenner myListener = new MyLocationListenner();

    MapView mMapView;
    BaiduMap mBaiduMap;


    public View mCurrentView;
    public Context mContext;

    public TextView start;
    public TextView end;
    public TextView time;
    public TextView pause;
    public TextView distance;

    public LatLng mLatLng;

    public String StartTime = "00:00:00";

    public long lastTime = 0;

    public Handler mHandler;

    public TimerTask task;

    public Timer mTimer;

    public boolean isPause = false;

    BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");

    @SuppressLint("ValidFragment")
    public SportFragment(Context context, LocationClient client) {
        mLocClient = client;
        this.mContext = context;

    }

    public SportFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCurrentView = inflater.inflate(R.layout.fragment_sport, container, false);
        init();
        return mCurrentView;

    }

    private void init() {
        start = (TextView) mCurrentView.findViewById(R.id.startControl);
        end = (TextView) mCurrentView.findViewById(R.id.endControl);
        time = (TextView) mCurrentView.findViewById(R.id.timeControl);
        pause = (TextView) mCurrentView.findViewById(R.id.pauseControl);
        distance = (TextView) mCurrentView.findViewById(R.id.disControl);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                time.setText(msg.obj.toString());
                super.handleMessage(msg);
            }
        };


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                time.setText(StartTime);
                mTimer = new Timer(true);
                if (isPause) {
                    isPause = false;
                } else {
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            if (!isPause) {
                                Message msg = new Message();
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                try {
                                    Date date = sdf.parse(StartTime);
                                    Calendar cd = Calendar.getInstance();
                                    cd.setTime(date);
                                    cd.add(Calendar.SECOND, 1);
                                    lastTime++;
                                    msg.obj = sdf.format(cd.getTime());
                                    StartTime = sdf.format(cd.getTime());
                                    mHandler.sendMessage(msg);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    mTimer.schedule(task, 0, 1000);
                }

            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                end.setVisibility(View.INVISIBLE);
                mTimer.cancel();
                task.cancel();
                isPause = false;
                post();

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.VISIBLE);
                isPause = true;
            }
        });

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mMapView = (MapView) mCurrentView.findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //  option.setCoorType("bd09ll"); // 设置坐标类型
        option.setAddrType("all");
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setPriority(LocationClientOption.GpsFirst);
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);

        mLocClient.setLocOption(option);
        mLocClient.start();
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, null));
    }

    private void post() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", ((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id);
            object.put("LastTime", this.lastTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiUrl.getInsertSportUrl(), object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (!jsonObject.get("ResultCode").toString().equals("1")) {
                        Toast.makeText(mContext, jsonObject.get("ResultMessage").toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, jsonObject.get("ResultMessage").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                StartTime = "00:00:00";
                lastTime = 0;
                distance.setText("0 km");
                time.setText(StartTime);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(this);
        ((BaseApplication) mContext.getApplicationContext()).getInstance().addToRequestQueue(request, mContext.toString());
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            Geocoder geoCoder = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
            try {
                List<Address> addresses= geoCoder.getFromLocation(
                        location.getLatitude() , location.getLongitude() ,
                        1);
                if(addresses.size()>0){
                    BaseApplication.getInstance().CurrentCity=addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((BaseApplication) mContext.getApplicationContext()).CurrentLocation = location;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            if (mLatLng != null && (ll.latitude != mLatLng.latitude || ll.longitude != mLatLng.latitude)) {
                List<LatLng> points = new ArrayList<LatLng>();
                points.add(mLatLng);
                points.add(ll);
                List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
                textureList.add(mBlueTexture);
                List<Integer> textureIndexs = new ArrayList<Integer>();
                textureIndexs.add(0);
                OverlayOptions ooPolyline11 = new PolylineOptions().width(20)
                        .points(points).dottedLine(true).customTextureList(textureList).textureIndex(textureIndexs);
                mBaiduMap.addOverlay(ooPolyline11);
                distance.setText((GetDistance(ll.longitude, ll.latitude, mLatLng.longitude, mLatLng.latitude) / 1000) + " km");
            }

            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    @Override
    public void onDestroy() {
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();

        super.onResume();
    }

    private static final double EARTH_RADIUS = 6378137;
    /** */
    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 弧度
     *
     * @param d
     * @return
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
