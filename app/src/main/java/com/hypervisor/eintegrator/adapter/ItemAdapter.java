package com.hypervisor.eintegrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.services.DeviceService;
import com.hypervisor.eintegrator.utils.Constants;


/**
 * Created by dexter on 18/1/17.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private String[] head;
    private Context mContext;
    private String language ;
    Intent intentSound ;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public View background;
        public TextView title ;
        public ImageView ivListen ;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvName);
            icon = (ImageView) view.findViewById(R.id.ivIcon);
            ivListen = (ImageView) view.findViewById(R.id.ivListen);
        }
    }


    public ItemAdapter(Context context,String language) {
        mContext = context;
        head = mContext.getResources().getStringArray(R.array.menu_type) ;
        this.language = language;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(head[position]);
        switch(position){
            case 0 : holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bills_gen));
                holder.ivListen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (language){
                            case Constants.LAN_ENG:
                                playSound(R.raw.know_about_bhamashah_card_english);
                                break;
                            case Constants.LAN_HINDI:
                                playSound(R.raw.apne_bhamashah_ke_baare_mein_jaane_hindi);
                                break;
                            case Constants.LAN_MARWARI:
                                playSound(R.raw.apne_bhamashah_ke_baare_mein_jaano_marwari);
                                break;
                        }
                    }
                });
                break;
            case 1 :
                holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bills_gen));
                holder.ivListen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (language){
                            case Constants.LAN_ENG:
                                playSound(R.raw.know_about_your_bills_and_payments_english);
                                break;
                            case Constants.LAN_HINDI:
                                playSound(R.raw.apne_bill_ke_baare_mein_jaane_hindi);
                                break;
                            case Constants.LAN_MARWARI:
                                playSound(R.raw.apne_bill_ke_baare_mein_jaano_marwari);
                                break;
                        }
                    }
                });
                break;
            case 2 :
                holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_entitlements_gen));
                holder.ivListen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (language){
                            case Constants.LAN_ENG:
                                playSound(R.raw.know_about_scheme_entitlements_english);
                                break;
                            case Constants.LAN_HINDI:
                                playSound(R.raw.apni_paatrata_ke_baare_mein_jaane_hindi);
                                break;
                            case Constants.LAN_MARWARI:
                                playSound(R.raw.aapne_paatrataa_ke_baare_mein_marwari);
                                break;
                        }
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        return head.length;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void stopAudio() {
        Intent intent = new Intent(mContext, DeviceService.class);
        mContext.stopService(intent);
    }

    private void playSound(int id){
        intentSound = new Intent(mContext, DeviceService.class);
        intentSound.setAction(Constants.ACTION_PLAY_FILE);
        intentSound.putExtra(Constants.FILE_RAW,id);
        mContext.startService(intentSound);
    }
}