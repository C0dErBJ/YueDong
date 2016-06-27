package com.yuedong.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.widget.*;
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
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Common.ApiUrl;
import com.yuedong.app.Model.StadiumModel;
import com.yuedong.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class NearByFragment extends android.support.v4.app.Fragment {

    public Context mContext;
    public View mCurrentView;
    public BaseApplication mApplicationContext;

    public ListView mListView;
    private ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter mSimple;

    private View.OnKeyListener listener;

    private StadiumModel[] models;

    private EditText search;
    private BDLocation location;

    public NearByFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public NearByFragment(Context context, View.OnKeyListener listener, BaseApplication mApplicationContext) {
        // Required empty public constructor
        this.mContext = context;
        this.mApplicationContext = mApplicationContext;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCurrentView = inflater.inflate(R.layout.fragment_near_by, container, false);
        init();
        return mCurrentView;
    }

    public void init() {
        search = (EditText) mCurrentView.findViewById(R.id.et_search);

        search.setOnKeyListener(listener);
        mListView = (ListView) mCurrentView.findViewById(R.id.list);
        mSimple = new SimpleAdapter(mContext, listItem, R.layout.layout_item_neayby, new String[]{
                "Name", "Address", "Phone"
        }, new int[]{R.id.staName, R.id.staAdd, R.id.staPho});
        mListView.setAdapter(mSimple);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, StadiumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Stadiu", models[i]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (BaseApplication.getInstance().CurrentLocation != null) {
            if (location != null) {
                if (BaseApplication.getInstance().CurrentLocation.getLatitude() != location.getLatitude() || BaseApplication.getInstance().CurrentLocation.getLongitude() != location.getLongitude()) {
                    location = ((BaseApplication) getActivity().getApplication()).CurrentLocation;
                    GetStadium(location, search.getText().toString());
                }
            } else {
                location = BaseApplication.getInstance().CurrentLocation;
                GetStadium(location, search.getText().toString());
            }
        }
    }


    public void GetStadium(BDLocation bdLocation, String keyword) {
        String url = "";
        if (bdLocation != null) {
            url = ApiUrl.getNearByStadium() +
                    "?lng=" + bdLocation.getLongitude() + "&lat=" +
                    bdLocation.getLatitude() + "&distance=10" + "&keyword=" + keyword;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("地址", jsonObject.toString());
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ObjectMapper mapper = new ObjectMapper();
                        models = mapper.readValue(jsonObject.getString("ResultData"), StadiumModel[].class);
                        listItem.clear();
                        if (models != null && models.length > 0) {
                            for (StadiumModel model : models) {
                                HashMap<String, String> Map = new HashMap<String, String>();
                                Map.put("Name", model.Name);
                                Map.put("Address", model.Address);
                                Map.put("Phone", model.Phone);
                                listItem.add(Map);
                            }
                            mSimple.notifyDataSetChanged();
                        }
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

            }
        });
        request.setTag(this);
        ((BaseApplication) getActivity().getApplication()).getInstance().addToRequestQueue(request, mContext.toString());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BaseApplication.getInstance().CurrentLocation != null) {
            if (location != null) {
                if (BaseApplication.getInstance().CurrentLocation.getLatitude() != location.getLatitude() || BaseApplication.getInstance().CurrentLocation.getLongitude() != location.getLongitude()) {
                    location = ((BaseApplication) getActivity().getApplication()).CurrentLocation;
                    GetStadium(location, search.getText().toString());
                }
            } else {
                location = BaseApplication.getInstance().CurrentLocation;
                GetStadium(location, search.getText().toString());
            }
        }
    }

    public void OnKeyDown(int keyCode) {
        if (keyCode == 66) {
            GetStadium(location, search.getText().toString());
        }
    }
}
