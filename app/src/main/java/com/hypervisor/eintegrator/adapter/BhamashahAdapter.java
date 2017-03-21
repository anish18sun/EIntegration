package com.hypervisor.eintegrator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.model.FamilyHeadType;

import java.util.ArrayList;

/**
 * Created by dexter on 18/3/17.
 */
public class BhamashahAdapter extends RecyclerView.Adapter<BhamashahAdapter.BhamashahDetailHolder> {
    private static MyClickListener myClickListener;
    private ArrayList<FamilyHeadType> bhamshahDetail;
    private Context mContext;

    public BhamashahAdapter(Context mContext, ArrayList<FamilyHeadType> bhamshahDetail) {
        this.bhamshahDetail = bhamshahDetail;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public BhamashahDetailHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bhamashah_item, parent, false);
        return new BhamashahDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(BhamashahDetailHolder holder, int position) {
        FamilyHeadType obj = bhamshahDetail.get(position);
        holder.name.setText(obj.getNAME_ENG());
        holder.fatherName.setText(obj.getFATHER_NAME_ENG());
        holder.mothersName.setText(obj.getMOTHER_NAME_ENG());
        holder.relation.setText(obj.getRELATION_ENG());
        holder.bhamshahId.setText(obj.getBHAMASHAH_ID());
        holder.dateOfBirth.setText(obj.getDOB());
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color1 = generator.getRandomColor();
        // generate color based on a key (same key returns the same color), useful for list/grid views
        int color2 = generator.getColor(obj.getNAME_ENG());


        TextDrawable drawable = TextDrawable.builder().buildRound((obj.getNAME_ENG().charAt(0)+"").toUpperCase(), color1);

        holder.imageView.setImageDrawable(drawable);
    }

    public void addItem(FamilyHeadType detail, int index) {
        bhamshahDetail.add(index, detail);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        bhamshahDetail.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return bhamshahDetail.size();
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
        TextView name;
        TextView fatherName;
        TextView mothersName;
        TextView dateOfBirth;
        TextView bhamshahId;
        TextView relation;
        ImageView imageView ;

        public BhamashahDetailHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvname);
            fatherName = (TextView) itemView.findViewById(R.id.tvFatherName);
            mothersName = (TextView) itemView.findViewById(R.id.tvMothersName);
            dateOfBirth = (TextView) itemView.findViewById(R.id.tvDOB);
            bhamshahId = (TextView) itemView.findViewById(R.id.tvBhamashahId);
            relation = (TextView) itemView.findViewById(R.id.tvRelation);
            imageView = (ImageView)itemView.findViewById(R.id.image_view);
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