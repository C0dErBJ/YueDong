package com.yuedong.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.yuedong.app.Base.BaseActivity;
import com.yuedong.app.Model.StadiumModel;
import com.yuedong.app.R;

public class StadiumActivity extends BaseActivity {
    public TextView name;
    public TextView address;
    public TextView worktime;
    public TextView price;
    public TextView phone;

    public ImageView back;

    private StadiumModel model;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium);
        mContext = this;
        Intent intent = getIntent();
        model = (StadiumModel) intent.getSerializableExtra("Stadiu");
        init();
    }

    private void init() {
        name = $(R.id.name);
        address = $(R.id.address);
        worktime = $(R.id.worktime);
        price = $(R.id.price);
        phone = $(R.id.phone);

        back = $(R.id.back);

        name.setText(model.Name);
        address.setText(model.Address);
        worktime.setText(model.OpenStartTime + "~" + model.OpenEndTime);
        price.setText(model.Price);
        phone.setText(model.Phone);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
    }

}
