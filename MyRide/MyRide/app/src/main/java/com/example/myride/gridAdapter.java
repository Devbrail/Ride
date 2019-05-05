package com.example.myride;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class gridAdapter extends BaseAdapter{
    ArrayList names;
    public static Activity activity;
Context mcontext;
    public gridAdapter(Activity activity, ArrayList names, Context context) {
        this.activity = activity;
        this.names = names;
        this.mcontext=context;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(activity);
            v = vi.inflate(R.layout.grid_layout, null);
        }
        TextView textView = (TextView)v.findViewById(R.id.namePlacer);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageHolder);
        if(names.get(position).toString().equals("Find a ride"))
        {
            imageView.setImageResource(R.mipmap.ic_launcher);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent =new Intent(mcontext,FindRide.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);


                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }else if(names.get(position).toString().equals("Offer a ride")) {
            imageView.setImageResource(R.mipmap.ic_launcher);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }
        textView.setText(names.get(position).toString());
        return v;
    }


//    public static void makeNotification(String userIntrouble) {
//        Log.d("NOTIFICATION","Building..........");
//        Intent notificationIntent = new Intent(activity.getApplicationContext(), noteActivity.class);
////        notificationIntent.putExtra(MainListAdapter.USER_EMAIL,userIntrouble);
////        notificationIntent.putExtra(MainListAdapter.IS_EMERGENCY, true);
//        PendingIntent pIntent = PendingIntent.getActivity(activity, 0, notificationIntent,
//                0);
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
//        Uri ring = Uri.parse(sharedPrefs.getString("Notification_Sound", Settings.System.DEFAULT_NOTIFICATION_URI.toString()));
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity.getBaseContext())
//                .setTicker("Ticker Title").setContentTitle("Notes Are Available For this subject")
//                .setSmallIcon(R.drawable.ic_notes)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(userIntrouble))
//                .setContentIntent(pIntent)
//                .setSound(ring);
//
//        Notification noti = builder.build();
//        noti.contentIntent = pIntent;
//        noti.flags = Notification.FLAG_AUTO_CANCEL;
//        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, noti);
//    }




}
