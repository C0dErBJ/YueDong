package com.yuedong.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soundcloud.android.crop.Crop;
import com.yuedong.app.Base.BaseActivity;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Common.ApiUrl;
import com.yuedong.app.Model.PictureModel;
import com.yuedong.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class PersonalDetailActivity extends BaseActivity {
    public RelativeLayout iv_user;
    public RelativeLayout nickname;
    public RelativeLayout sex;
    public RelativeLayout age;
    public RelativeLayout hobby;
    public RelativeLayout spacetime;

    public TextView tv_nickname;
    public TextView tv_sex;
    public TextView tv_age;

    public ImageView head;
    private Bitmap mBitmap;

    public Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);
        mContext = this;
    }

    private void init() {
        iv_user = $(R.id.iv_user);
        nickname = $(R.id.rl_nickname);
        sex = $(R.id.rl_sex);
        age = $(R.id.rl_age);
        hobby = $(R.id.rl_hobby);
        spacetime = $(R.id.rl_spacetime);

        tv_nickname = $(R.id.tv_nickname);
        tv_age = $(R.id.tv_age);
        tv_sex = $(R.id.tv_sex);
        head = $(R.id.head);
        iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AvatorActivity.class));
            }
        });
        spacetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, InputSpaceTimeActivity.class));
            }
        });
        tv_nickname.setText(((BaseApplication) getApplication()).CurrentUser.NickName);
        tv_age.setText(((BaseApplication) getApplication()).CurrentUser.Age + "");
        tv_sex.setText(((BaseApplication) getApplication()).CurrentUser.Gender);
        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, InputNickNameSettingActivity.class));
            }
        });
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, InputSexActivity.class));
            }
        });
        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, InputAgeActivity.class));
            }
        });
        hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, InputHobbyActivity.class));
            }
        });
        getPic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void getPic() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , ApiUrl.getPictureUrl() + "/" + BaseApplication.getInstance().CurrentUser.Id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ObjectMapper mapper = new ObjectMapper();
                        PictureModel model = mapper.readValue(jsonObject.getString("ResultData"), PictureModel.class);
                        mBitmap = BitmapFactory.decodeByteArray(model.FileData, 0, model.FileData.length);
                        head.setImageBitmap(mBitmap);

                    } else {
                        head.setImageResource(R.drawable.avatar);
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
        BaseApplication.getInstance().addToRequestQueue(request, mContext.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBitmap!=null){
            mBitmap.recycle();
        }
    }
}
