package com.yuedong.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegistActivity extends BaseActivity {

    public EditText nickname;
    public EditText password;
    public EditText phone;
    public EditText reinput;
    public Button signUp;

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        mContext=this;
        init();
    }

    private void init() {
        nickname = $(R.id.input_name);
        phone = $(R.id.input_username);
        reinput = $(R.id.re_input_password);
        password = $(R.id.input_password);
        signUp = $(R.id.btn_signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nickname.getText().toString().isEmpty()){
                    Toast.makeText(mContext, "昵称不能为空", Toast.LENGTH_SHORT).show();
                }else if(phone.getText().toString().isEmpty()){
                    Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().isEmpty()){
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else if(!password.getText().toString().equals(reinput.getText().toString())){
                    Toast.makeText(mContext, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject para = new JSONObject();
                    try {
                        para.put("username", phone.getText().toString());
                        para.put("password", password.getText().toString());
                        para.put("nickname", nickname.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiUrl.getSignUpUrl(), para, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (!jsonObject.get("ResultCode").toString().equals("1")) {
                                    ObjectMapper mapper = new ObjectMapper();
                                    User user = mapper.readValue(jsonObject.get("ResultData").toString(), User.class);
                                    BaseApplication.getInstance().CurrentUser = user;
                                    startActivity(new Intent(mContext, MainActivity.class));
                                    ((Activity)mContext).finish();
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
                            Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_regist, menu);
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
