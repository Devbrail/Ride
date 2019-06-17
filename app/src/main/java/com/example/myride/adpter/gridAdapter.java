package com.example.myride.adpter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myride.Listener.Interfaces;
import com.example.myride.R;

import java.util.ArrayList;


public class gridAdapter extends BaseAdapter  {
    ArrayList names;
    public static Activity activity;
Context mcontext;
Interfaces interfaces;

    public gridAdapter(Activity activity, ArrayList names, Context context,Interfaces interfaces) {
        this.activity = activity;
        this.names = names;
        this.mcontext=context;
        this.interfaces=interfaces;
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

        final int po=position;
        GridView grid = (GridView)parent;
        int size = grid.getColumnWidth();

        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(activity);
            v = vi.inflate(R.layout.grid_layout, null);
            v.setLayoutParams(new GridView.LayoutParams(size, size));
        }
        TextView textView = (TextView)v.findViewById(R.id.namePlacer);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageHolder);
        if(names.get(position).toString().equals("Find a ride"))
        {
            imageView.setImageResource(R.drawable.searcar);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     interfaces.ItemClicked(po);


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
            imageView.setImageResource(R.mipmap.ic_launcher1);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    interfaces.ItemClicked(po);


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
//        Log.wtf("NOTIFICATION","Building..........");
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
