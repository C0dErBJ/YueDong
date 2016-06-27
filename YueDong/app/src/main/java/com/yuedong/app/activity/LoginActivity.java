package com.yuedong.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuedong.app.Base.BaseActivity;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Model.User;
import com.yuedong.app.R;
import com.yuedong.app.Common.ApiUrl;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends BaseActivity {

    public EditText username;
    public EditText password;
    public TextView login;

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        init();
    }

    private void init() {

        username = $(R.id.username);
        password = $(R.id.password);
        login = $(R.id.logintv);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject para = new JSONObject();
                    try {
                        para.put("username", username.getText().toString());
                        para.put("password", password.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiUrl.getLoginUrl(), para, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (!jsonObject.get("ResultCode").toString().equals("1")) {
                                    ObjectMapper mapper = new ObjectMapper();
                                    User user = mapper.readValue(jsonObject.get("ResultData").toString(), User.class);
                                    Log.i("login", "登陆成功");
                                    BaseApplication.getInstance().CurrentUser = user;
                                    startActivity(new Intent(mContext, MainActivity.class));
                                    ((Activity) mContext).finish();
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
                            Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    request.setTag(this);
                    ((BaseApplication) getApplication()).getInstance().addToRequestQueue(request, mContext.toString());
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.register:
                Intent intent = new Intent(this, RegistActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
