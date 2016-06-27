package com.yuedong.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jialin.chat.*;
import com.yuedong.app.Base.BaseActivity;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.Common.ApiUrl;
import com.yuedong.app.Model.MessageModel;
import com.yuedong.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageActivity extends BaseActivity {
    public Context mContext;

    public MessageInputToolBox box;

    private ListView listView;
    private MessageAdapter adapter;

    public MessageModel[] models;

    private TextView title;
    private TextView finish;
    private ImageView back;

    private String id;
    private List<Message> messages = new ArrayList<Message>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mContext = this;
        Intent intent = getIntent();
        id = intent.getStringExtra("TargetId");
        initMessageInputToolBox();

        init();
    }

    private void init() {
        title = $(R.id.title);
        finish = $(R.id.finish);
        title.setText("消息");
        finish.setVisibility(View.GONE);
        back = $(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
        listView = (ListView) findViewById(R.id.messageListview);
        adapter = new MessageAdapter(mContext, messages);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                box.hide();
                return false;
            }
        });
        GetMessage();
    }

    @SuppressLint("ShowToast")
    private void initMessageInputToolBox() {
        box = (MessageInputToolBox) findViewById(R.id.messageInputToolBox);
        box.setOnOperationListener(new OnOperationListener() {

            @Override
            public void send(String content) {

                System.out.println("===============" + content);

                SendMessage(content);

                listView.setSelection(listView.getBottom());

            }

            @Override
            public void selectedFace(String content) {

                System.out.println("===============" + content);
                Message message = new Message(Message.MSG_TYPE_FACE, Message.MSG_STATE_SUCCESS, "Tomcat", "avatar", "Jerry", "avatar", content, true, true, new Date());
                adapter.getData().add(message);
                listView.setSelection(listView.getBottom());
                //Just demo
                createReplayMsg(message);
            }


            @Override
            public void selectedFuncation(int index) {

                System.out.println("===============" + index);

                switch (index) {
                    case 0:
                        //do some thing
                        break;
                    case 1:
                        //do some thing
                        break;

                    default:
                        break;
                }
                Toast.makeText(mContext, "Do some thing here, index :" + index, Toast.LENGTH_SHORT).show();

            }

        });

    }


    private void initListView() {
        if (models == null) {
            return;
        }
        adapter.getData().clear();
        for (MessageModel model : models) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date mDate = sdf.parse(model.CreateTime);
                Message message = new Message(Message.MSG_TYPE_TEXT
                        , Message.MSG_STATE_SUCCESS,
                        model.FromWho, "avatar", model.ToWho, "avatar", model.Text,
                        model.FromId.equals(((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id), true, mDate);
                messages.add(message);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        adapter.notifyDataSetChanged();

    }


    private void createReplayMsg(Message message) {

        final Message reMessage = new Message(message.getType(), 1, "Tom", "avatar", "Jerry", "avatar",
                message.getType() == 0 ? "Re:" + message.getContent() : message.getContent(),
                false, true, new Date()
        );
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * (new Random().nextInt(3) + 1));
                    ((MainActivity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            adapter.getData().add(reMessage);
                            listView.setSelection(listView.getBottom());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void SendMessage(final String content) {
        String url = "";

        JSONObject object = new JSONObject();
        try {
            if (adapter.getData().size() > 0) {
                url = ApiUrl.getReplay();
                object.put("Batch", models[0].Batch);
            } else {
                url = ApiUrl.getNewMessage();
            }
            object.put("ToWho", id);
            object.put("FromWho", ((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id);
            object.put("MessageContent", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST
                , url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("消息", jsonObject.toString());
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ObjectMapper mapper = new ObjectMapper();
                        MessageModel model = mapper.readValue(jsonObject.getString("ResultData"), MessageModel.class);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date mDate=sdf.parse(model.CreateTime);
                        Message message = new Message(Message.MSG_TYPE_TEXT
                                , Message.MSG_STATE_SUCCESS,
                                model.FromWho, "avatar", model.ToWho, "avatar", model.Text,
                                true, true, mDate);
                        messages.add(message);
                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message message = new Message(Message.MSG_TYPE_TEXT
                        , Message.MSG_STATE_FAIL,
                        ((BaseApplication) mContext.getApplicationContext()).CurrentUser.NickName, "avatar", "jreey", "avatar", content,
                        true, true, new Date());
                messages.add(message);
                adapter.notifyDataSetChanged();
            }
        });
        request.setTag(this);
        ((BaseApplication) mContext.getApplicationContext()).getInstance().addToRequestQueue(request, mContext.toString());
    }

    private void GetMessage() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , ApiUrl.getGetTargetMessage() + ((BaseApplication) mContext.getApplicationContext()).CurrentUser.Id + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("消息", jsonObject.toString());
                try {
                    if (jsonObject.getString("ResultCode").equals("0")) {
                        ObjectMapper mapper = new ObjectMapper();
                        models = mapper.readValue(jsonObject.getString("ResultData"), MessageModel[].class);
                        initListView();

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
