package com.yuedong.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
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
import com.yuedong.app.Model.MessageModel;
import com.yuedong.app.Model.User;
import com.yuedong.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchFriendActivity extends BaseActivity {

    public TextView title;
    public TextView finish;
    public ImageView back;

    public Context mContext;

    public ListView mListView;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter mSimple;

    public User[] models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        mContext = this;
        init();
    }

    public void init() {
        title = $(R.id.title);
        finish = $(R.id.finish);
        back = $(R.id.back);
        title.setText("选择好友");
        finish.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });

        mListView = $(R.id.friendlist);
        mSimple = new SimpleAdapter(mContext, listItem, R.layout.layout_item_searchfriend, new String[]{
                "Avator", "NickName"
        }, new int[]{R.id.friavator, R.id.frinickname});
        mListView.setAdapter(mSimple);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TargetId", models[i].Id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        GetUsers();

    }

    public void GetUsers() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , ApiUrl.getUsers(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("消息", jsonObject.toString());
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ObjectMapper mapper = new ObjectMapper();
                        models = mapper.readValue(jsonObject.getString("ResultData"), User[].class);

                        listItem.clear();
                        if (models != null && models.length > 0) {
                            for (User model : models) {
                                if(model.Id.equals(((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id)){
                                    continue;
                                }
                                HashMap<String, Object> Map = new HashMap<String, Object>();
                                Map.put("Avator", R.drawable.avatar);
                                Map.put("NickName", model.NickName);
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
