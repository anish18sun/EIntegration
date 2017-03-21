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
import com.hypervisor.eintegrator.model.Entitlement;
import com.hypervisor.eintegrator.model.EntitlementInformation;
import com.hypervisor.eintegrator.model.FamilyHeadType;

import java.util.ArrayList;

/**
 * Created by dexter on 18/3/17.
 */
public class EntitlementsAdapter extends RecyclerView.Adapter<EntitlementsAdapter.BhamashahDetailHolder> {
    private static MyClickListener myClickListener;
    private ArrayList<EntitlementInformation> entitlementDetails;
    private Context mContext;

    public EntitlementsAdapter(Context mContext, ArrayList<EntitlementInformation> entitlementDetails) {
        this.entitlementDetails = entitlementDetails;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public BhamashahDetailHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entitlement_item, parent, false);
        return new BhamashahDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(BhamashahDetailHolder holder, int position) {
        EntitlementInformation info = entitlementDetails.get(position);
        holder.entitlementId.setText(info.getEntitlement().getEntitlementId());
        holder.entitlementDate.setText(info.getAccural().getDueDate());
        holder.entitlementAmount.setText(info.getAccural().getDueAmount());
    }

    public void addItem(EntitlementInformation detail, int index) {
        entitlementDetails.add(index, detail);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        entitlementDetails.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return entitlementDetails.size();
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
        TextView entitlementId;
        TextView entitlementDate;
        TextView entitlementAmount;

        public BhamashahDetailHolder(View itemView) {
            super(itemView);
            entitlementId = (TextView)itemView.findViewById(R.id.tvEntitlementId);
            entitlementAmount = (TextView)itemView.findViewById(R.id.tvEntitlementAmount);
            entitlementDate = (TextView)itemView.findViewById(R.id.tvEntitlementDate);
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