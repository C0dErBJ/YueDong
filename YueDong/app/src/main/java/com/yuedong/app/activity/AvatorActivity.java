package com.yuedong.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.yuedong.app.Model.StadiumModel;
import com.yuedong.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AvatorActivity extends BaseActivity {

    private ImageView resultView;

    private TextView title;
    private TextView finish;
    private ImageView back;

    private Context mContext;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avator);
        resultView = (ImageView) findViewById(R.id.result_image);
        resultView.setImageDrawable(null);
        mContext = this;
        Crop.pickImage(this);
        init();
    }

    private void init() {
        title = $(R.id.title);
        finish = $(R.id.finish);
        back = $(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
        title.setText("设置头像");
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPic();
            }
        });
        getPic();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(source).output(outputUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            resultView.setImageURI(Crop.getOutput(result));
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                        resultView.setImageBitmap(mBitmap);

                    } else {
                        resultView.setImageResource(R.drawable.avatar);
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

    private void setPic() {
        JSONObject object = new JSONObject();
        try {
            if (mBitmap == null) {
                return;
            }
            object.put("UserId", BaseApplication.getInstance().CurrentUser.Id);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
            object.put("FileData", Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST
                , ApiUrl.getPictureUrl(), object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("地址", jsonObject.toString());
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ((Activity) mContext).finish();

                    }
                } catch (JSONException e) {
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
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        super.onDestroy();
    }
}
