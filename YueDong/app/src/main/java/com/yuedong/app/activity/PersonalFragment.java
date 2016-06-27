package com.yuedong.app.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Common.ApiUrl;
import com.yuedong.app.Model.PictureModel;
import com.yuedong.app.Model.SportModel;
import com.yuedong.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends android.support.v4.app.Fragment {

    public TextView UserName;
    public ImageView UserAvator;
    public ImageView Sex;
    public TextView UserAccount;

    public TextView SportTime;
    public TextView SportCount;

    public RelativeLayout UserInfo;

    public Button Logout;

    public Context mContext;
    public View mCurrentView;
    Bitmap map;

    public PersonalFragment() {
        // Required empty public constructor
    }

    public PersonalFragment(Context context) {
        // Required empty public constructor
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCurrentView = inflater.inflate(R.layout.fragment_personal, container, false);
        init();
        return mCurrentView;
    }

    private void init() {
        UserName = (TextView) mCurrentView.findViewById(R.id.tvname);
        UserAccount = (TextView) mCurrentView.findViewById(R.id.tvmsg);
        SportTime = (TextView) mCurrentView.findViewById(R.id.tv_sporttime);
        SportCount = (TextView) mCurrentView.findViewById(R.id.tv_sportdistance);
        UserAvator = (ImageView) mCurrentView.findViewById(R.id.head);
        Sex = (ImageView) mCurrentView.findViewById(R.id.iv_sex);
        Logout = (Button) mCurrentView.findViewById(R.id.btn_sendmsg);
        UserInfo = (RelativeLayout) mCurrentView.findViewById(R.id.view_user);
        Logout.setVisibility(View.GONE);
        UserName.setText(((BaseApplication) mContext.getApplicationContext()).CurrentUser.NickName);
        UserAccount.setText("账号: " + ((BaseApplication) mContext.getApplicationContext()).CurrentUser.UserName);
        Sex.setImageResource(BaseApplication.getInstance().CurrentUser.Gender.equals("男") ? R.drawable.ic_sex_male : R.drawable.ic_sex_female);
        UserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, PersonalDetailActivity.class));
            }
        });
        GetSport();
        GetPic();

    }

    public void GetSport() {
        String Id = ((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiUrl.getSportUrl() + Id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ObjectMapper mapper = new ObjectMapper();
                        SportModel model = mapper.readValue(jsonObject.getString("ResultData"), SportModel.class);
                        SportTime.setText((model.LastTime / 60) + " min");
                        SportCount.setText(model.Count + " 次");

                    } else {
                        SportTime.setText("0 min");
                        SportCount.setText("0 次");
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

    public void GetPic() {
        String Id = ((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiUrl.getPictureUrl() + "/" + Id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ObjectMapper mapper = new ObjectMapper();
                        PictureModel model = mapper.readValue(jsonObject.getString("ResultData"), PictureModel.class);
                        map = BitmapFactory.decodeByteArray(model.FileData, 0, model.FileData.length);
                        UserAvator.setImageBitmap(map);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (map != null) {
            map.recycle();
        }
    }
}
