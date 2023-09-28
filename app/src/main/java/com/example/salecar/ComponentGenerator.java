package com.example.salecar;

import android.app.Activity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComponentGenerator {
    private Activity activity;
    private LinearLayout llBody;

    public ComponentGenerator(Activity activity, LinearLayout llBody) {
        this.activity = activity;
        this.llBody = llBody;
    }

    public void customItemList(int id, String phone) {
        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setWeightSum(10);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        llBody.addView(linearLayout);

        ImageView ivIconPhone = new ImageView(activity);
        LinearLayout.LayoutParams lpIconPhone = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120, 4);
        lpIconPhone.gravity = Gravity.CENTER;
        ivIconPhone.setLayoutParams(lpIconPhone);
        ivIconPhone.setImageResource(R.drawable.baseline_call_24);

        linearLayout.addView(ivIconPhone);

        TextView tvTextPhone = new TextView(activity);
        LinearLayout.LayoutParams lpTextPhone = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        tvTextPhone.setLayoutParams(lpTextPhone);
        tvTextPhone.setTextSize(20);
        tvTextPhone.setText(phone);

        linearLayout.addView(tvTextPhone);

        Button btnDelete = new Button(activity);
        LinearLayout.LayoutParams lpButtonDelete = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4);
        btnDelete.setLayoutParams(lpButtonDelete);
        btnDelete.setBackgroundResource(R.drawable.custom_button);
        btnDelete.setText(activity.getString(R.string.delete));
        btnDelete.setTextColor(activity.getColor(R.color.white));

        linearLayout.addView(btnDelete);
    }
}