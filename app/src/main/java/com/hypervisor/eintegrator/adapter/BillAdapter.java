package com.hypervisor.eintegrator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.model.BillDetails;
import com.hypervisor.eintegrator.model.EntitlementInformation;

import java.util.ArrayList;

/**
 * Created by dexter on 18/3/17.
 */
public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BhamashahDetailHolder> {
    private static MyClickListener myClickListener;
    private ArrayList<BillDetails> billDetails;
    private Context mContext;

    public BillAdapter(Context mContext,ArrayList<BillDetails> billDetails) {
        this.billDetails = billDetails;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public BhamashahDetailHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_item, parent, false);
        return new BhamashahDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(BhamashahDetailHolder holder, int position) {
        holder.labelName.setText(billDetails.get(position).getLabelName());
        holder.labelValue.setText(billDetails.get(position).getLableValue());
    }

    public void addItem(BillDetails detail, int index) {
       billDetails.add(index, detail);
        notifyItemInserted(index);
    }
    @Override
    public int getItemCount() {
        return billDetails.size();
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
        TextView labelName ;
        TextView labelValue ;

        public BhamashahDetailHolder(View itemView) {
            super(itemView);
            labelName = (TextView)itemView.findViewById(R.id.tvLabelName);
            labelValue = (TextView)itemView.findViewById(R.id.tvLabelValue);
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
