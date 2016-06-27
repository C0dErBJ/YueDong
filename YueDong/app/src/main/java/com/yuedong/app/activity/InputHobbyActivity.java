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

public class InputHobbyActivity extends BaseActivity {

    public TextView title;
    public TextView finish;
    public ImageView back;
    public EditText mInput;
    public Button clear;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_hobby);
        mContext = this;
        init();
    }

    private void init() {
        title = $(R.id.title);
        finish = $(R.id.finish);
        back = $(R.id.back);
        mInput = $(R.id.hobbyinput);
        clear = $(R.id.hobbyClear);
        title.setText("爱好");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
        mInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                clear.setVisibility(View.VISIBLE);
                return false;
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User model = new User();
                model.Id = ((BaseApplication) getApplication()).getInstance().CurrentUser.Id;
                model.Hobby = mInput.getText().toString();
                JSONObject object = new JSONObject();
                try {
                    object.put("Id", model.Id);
                    object.put("Hobby", model.Hobby);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiUrl.getUpdateUserInfo(), object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (!jsonObject.get("ResultCode").toString().equals("1")) {
                                ((BaseApplication) getApplication()).getInstance().CurrentUser.Hobby = mInput.getText().toString();
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
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInput.setText("");
            }
        });
        mInput.setText(((BaseApplication) getApplication()).CurrentUser.Hobby);
        clear.setVisibility(View.VISIBLE);
    }
}
