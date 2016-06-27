package com.yuedong.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jialin.chat.*;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Common.ApiUrl;
import com.yuedong.app.Model.MessageModel;
import com.yuedong.app.Model.StadiumModel;
import com.yuedong.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


public class MessageFragment extends android.support.v4.app.Fragment {


    public Context mContext;
    public View mCurrentView;

    public ListView mListView;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter mSimple;
    public TextView title;
    public TextView searchFriend;

    public MessageModel[] models;


    public MessageFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MessageFragment(Context mContext) {
        // Required empty public constructor
        this.mContext = mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.fragment_message, container, false);
        init();
        return mCurrentView;
    }

    private void init() {
        mListView = (ListView) mCurrentView.findViewById(R.id.messagelist);
        title = (TextView) mCurrentView.findViewById(R.id.mestitle);
        searchFriend = (TextView) mCurrentView.findViewById(R.id.searchfriend);
        title.setText("刷新");
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMessage();
            }
        });
        mSimple = new SimpleAdapter(mContext, listItem, R.layout.layout_item_message, new String[]{
                "Avator", "NickName", "Message"
        }, new int[]{R.id.mesavator, R.id.msgnickname, R.id.msganswer});
        mListView.setAdapter(mSimple);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TargetId", models[i].ToId.equals(((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id) ? models[i].FromId : models[i].ToId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SearchFriendActivity.class));
            }
        });
        GetMessage();
    }

    public void GetMessage() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , ApiUrl.getGetLatestMessage() + ((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("消息", jsonObject.toString());
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ObjectMapper mapper = new ObjectMapper();
                        models = mapper.readValue(jsonObject.getString("ResultData"), MessageModel[].class);

                        listItem.clear();
                        if (models != null && models.length > 0) {
                            for (MessageModel model : models) {
                                HashMap<String, Object> Map = new HashMap<String, Object>();
                                Map.put("Avator", R.drawable.avatar);
                                Map.put("NickName", model.FromWho);
                                Map.put("Message", model.Text);
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
        ((BaseApplication) mContext.getApplicationContext()).getInstance().addToRequestQueue(request, mContext.toString());
    }

    public void GetId(String Id) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , ApiUrl.getPictureUrl() + ((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("消息", jsonObject.toString());
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ObjectMapper mapper = new ObjectMapper();
                        models = mapper.readValue(jsonObject.getString("ResultData"), MessageModel[].class);

                        listItem.clear();
                        if (models != null && models.length > 0) {
                            for (MessageModel model : models) {
                                HashMap<String, Object> Map = new HashMap<String, Object>();
                                Map.put("Avator", R.drawable.avatar);
                                Map.put("NickName", model.FromWho);
                                Map.put("Message", model.Text);
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
        ((BaseApplication) mContext.getApplicationContext()).getInstance().addToRequestQueue(request, mContext.toString());
    }
}
