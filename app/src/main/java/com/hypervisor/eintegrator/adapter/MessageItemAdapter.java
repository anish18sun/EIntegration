package com.hypervisor.eintegrator.adapter;

/**
 * Created by dexter on 20/3/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexter on 5/6/16.
 */
public class MessageItemAdapter extends RecyclerView.Adapter<MessageItemAdapter.ViewHolder> {
    private static final int TYPE_SEND = 2;
    private static final int TYPE_RECEIVE = 1;
    Context mContext;
    ArrayList<Message> mMessages;
    String searchString = "";

    public MessageItemAdapter(Context context, ArrayList<Message> mMessages) {
        mContext = context;
        this.mMessages = new ArrayList<Message>(mMessages);
    }

    @Override
    public MessageItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v;
        Log.e("POSITION", String.valueOf(viewType));

        if (viewType == TYPE_SEND) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_sent, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_receive, parent, false);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("Position", String.valueOf(position));
        Log.e("ITEM", "eNter");

        String messageBody = mMessages.get(position).getMes();
        holder.mMessage.setText(messageBody);
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).getType() == TYPE_SEND)
            return TYPE_SEND;
        return TYPE_RECEIVE;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public Message removeItem(int position) {
        final Message messageDetails = mMessages.remove(position);
        notifyItemRemoved(position);
        return messageDetails;
    }

    public void addItem(int position, Message messageDetails) {
        mMessages.add(position, messageDetails);
        notifyItemInserted(position);
    }

    public int addItem(Message messageDetails) {
        mMessages.add(messageDetails);
        Log.e("Inserted", "ITEM1");
        notifyDataSetChanged();
        return mMessages.size() - 1;

    }

    public void moveItem(int fromPosition, int toPosition) {
        final Message messageDetails = mMessages.remove(fromPosition);
        mMessages.add(toPosition, messageDetails);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Message> models, String newText) {
        Log.e("TextNew", "newtext");
        searchString = newText;
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Message> newModels) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            final Message messageDetails = mMessages.get(i);
            if (!newModels.contains(messageDetails)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Message> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Message model = newModels.get(i);
            if (!mMessages.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Message> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Message model = newModels.get(toPosition);
            final int fromPosition = mMessages.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mMessage;

        public ViewHolder(View v) {
            super(v);
            mMessage = (TextView) v.findViewById(R.id.messagebody);
        }

    }
}