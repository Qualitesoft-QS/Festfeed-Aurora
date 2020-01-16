package com.festfeed.awf.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.festfeed.awf.R;
import com.festfeed.awf.gamemodel.QrGameIcon;
import com.festfeed.awf.utils.Preferences;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<QrGameIcon>qrGameIcons;
    LayoutInflater inflter;
    SharedPreferences pref;
    public CustomAdapter(Context applicationContext, List<QrGameIcon>qrGameIcons) {
        this.context = applicationContext;
        this.qrGameIcons = qrGameIcons;
        inflter = (LayoutInflater.from(applicationContext));
        pref = context.getApplicationContext().getSharedPreferences("Festfeed",context. MODE_PRIVATE);
    }
    @Override
    public int getCount() {
        return qrGameIcons.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_gridview, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.selectedImage);






       if(pref.getBoolean(Preferences.INSTANCE.getSelectedGame().getEventid()+""+qrGameIcons.get(i).getQrValue(),false))
       {
           String stringWithOnlyDigits =qrGameIcons.get(i).getOn().replaceAll("[0-9]","");
           stringWithOnlyDigits=  stringWithOnlyDigits.replace("-","");
           Drawable drawable=  context.getResources().getDrawable(context.getResources().getIdentifier(stringWithOnlyDigits.toLowerCase(), "drawable",context.getPackageName()));
           try {
               icon.setBackgroundDrawable(drawable);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       else
       {
           String stringWithOnlyDigits =qrGameIcons.get(i).getOff().replaceAll("[0-9]","");
           stringWithOnlyDigits=  stringWithOnlyDigits.replace("-","");
           Drawable drawable=  context.getResources().getDrawable(context.getResources().getIdentifier(stringWithOnlyDigits.toLowerCase(), "drawable",context.getPackageName()));
           try {

               icon.setBackgroundDrawable(drawable);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }



        return view;
    }
}