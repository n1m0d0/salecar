package com.example.salecar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;

import com.google.android.material.shape.RoundedCornerTreatment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class AdvertisementAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<AdvertisementObject> advertisements;

    public AdvertisementAdapter(Activity activity, ArrayList<AdvertisementObject> advertisements) {
        this.activity = activity;
        this.advertisements = advertisements;
    }

    @Override
    public int getCount() {
        return advertisements.size();
    }

    @Override
    public Object getItem(int i) {
        return advertisements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return advertisements.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewForm = view;
        if (viewForm == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewForm = layoutInflater.inflate(R.layout.item_advertisement, null);
        }

        AdvertisementObject item = advertisements.get(i);

        TextView tvBrand = viewForm.findViewById(R.id.tvBrand);
        tvBrand.setText(item.getBrand().toUpperCase());

        TextView tvModel = viewForm.findViewById(R.id.tvModel);
        tvModel.setText(item.getModel().toUpperCase());

        TextView tvPlate = viewForm.findViewById(R.id.tvPlate);
        tvPlate.setText(item.getPlate().toUpperCase());

        TextView tvMileage = viewForm.findViewById(R.id.tvMileage);
        tvMileage.setText(item.getMileage().toUpperCase());

        TextView tvFunctioning = viewForm.findViewById(R.id.tvFunctioning);
        tvFunctioning.setText("10/" + item.getFunctioning());

        TextView tvEsthetic = viewForm.findViewById(R.id.tvEsthetic);
        tvEsthetic.setText("10/" + item.getEsthetic());

        ImageView image1 = viewForm.findViewById(R.id.ivImage);

        String[] separated = item.getImage1().split("/");

        Picasso.get().load("https://carsale.ajatic.com/" + "storage/" + separated[1]).error(R.drawable.sale_car).into(image1);

        return viewForm;
    }
}
