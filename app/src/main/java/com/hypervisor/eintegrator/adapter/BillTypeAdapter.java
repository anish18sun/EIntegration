package com.hypervisor.eintegrator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hypervisor.eintegrator.R;

/**
 * Created by dexter on 18/3/17.
 */
public class BillTypeAdapter extends RecyclerView.Adapter<BillTypeAdapter.BhamashahDetailHolder> {
    private static MyClickListener myClickListener;
    private String[] billTypes;
    private Context mContext;

    public BillTypeAdapter(Context mContext) {
        this.billTypes = mContext.getResources().getStringArray(R.array.bill_types);
        this.mContext = mContext;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public BhamashahDetailHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_type_item, parent, false);
        return new BhamashahDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(BhamashahDetailHolder holder, int position) {
        holder.type.setText(billTypes[position]);
        switch(position){
            case 0:
                holder.icon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
                break;
            case 1:
                holder.icon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_electricity));
                break;
            case 2:
                holder.icon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_water));
                break;
            case 3:
                holder.icon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_telephone));
                break;
        }
    }


    @Override
    public int getItemCount() {
        return billTypes.length;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class BhamashahDetailHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView type;
        ImageView icon;

        public BhamashahDetailHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.tvName);
            icon = (ImageView) itemView.findViewById(R.id.ivIcon);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
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
}
