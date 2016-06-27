package com.yuedong.app.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuedong.app.Base.BaseActivity;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Common.ApiUrl;
import com.yuedong.app.Model.User;
import com.yuedong.app.R;
import org.json.JSONException;
import org.json.JSONObject;

public class InputSexActivity extends BaseActivity {

    public TextView title;
    public TextView finish;
    public ImageView back;
    public Context mContext;

    public Button male;
    public Button female;

    public String Sex = ((BaseApplication) getApplication()).getInstance().CurrentUser.Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sex);
        mContext = this;
        init();
    }

    private void init() {
        title = $(R.id.title);
        finish = $(R.id.finish);
        back = $(R.id.back);
        male = $(R.id.btn_male);
        female = $(R.id.btn_female);
        title.setText("性别");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setBackgroundResource(R.drawable.ic_check_circle_36dp);
                male.setBackgroundResource(R.drawable.ic_check_circle_black_36dp);
                Sex = "男";
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setBackgroundResource(R.drawable.ic_check_circle_black_36dp);
                male.setBackgroundResource(R.drawable.ic_check_circle_36dp);
                Sex = "女";
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User model = new User();
                model.Id = ((BaseApplication) getApplication()).getInstance().CurrentUser.Id;
                model.Gender = Sex;
                JSONObject object = new JSONObject();
                try {
                    object.put("Id", model.Id);
                    object.put("Gender", model.Gender);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiUrl.getUpdateUserInfo(), object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (!jsonObject.get("ResultCode").toString().equals("1")) {
                                ((BaseApplication) getApplication()).getInstance().CurrentUser.Gender = Sex;
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
                });
                request.setTag(this);
                ((BaseApplication) getApplication()).getInstance().addToRequestQueue(request, mContext.toString());
            }
        });
        if(((BaseApplication) getApplication()).CurrentUser.Gender.equals("女")){
            female.setBackgroundResource(R.drawable.ic_check_circle_black_36dp);
            male.setBackgroundResource(R.drawable.ic_check_circle_36dp);
        }else{
            male.setBackgroundResource(R.drawable.ic_check_circle_black_36dp);
            female.setBackgroundResource(R.drawable.ic_check_circle_36dp);
        }
    }
}
