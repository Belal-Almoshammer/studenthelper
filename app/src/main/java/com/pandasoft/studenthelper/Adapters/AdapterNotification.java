package com.pandasoft.studenthelper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pandasoft.studenthelper.Entities.EntityNotifications;
import com.pandasoft.studenthelper.R;

import java.util.ArrayList;
import java.util.List;

;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.NotificationHolder> {
    List<EntityNotifications> mList;
    Context mContext;
    OnClickItemListener mListener;

    public AdapterNotification(Context context) {
        mContext = context;
    }

    public void setOnClickListener(OnClickItemListener listener) {
        this.mListener = listener;
    }

    public AdapterNotification setAdapter(List<EntityNotifications> list) {
        if (mList == null) mList = new ArrayList<>();
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        return this;
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_notification, parent, false);
        return new NotificationHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        EntityNotifications entity = mList.get(position);
        holder.txtTitle.setText(entity.getTitle());
        holder.txtTime.setText(entity.getDate_create());
        holder.txtDate.setText(entity.getTime_create());
        holder.txtDescription.setText(entity.getDescription());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface OnClickItemListener {
        void onClick(int position);
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDescription, txtTime, txtDate;
        Button btnClear;

        public NotificationHolder(View view, OnClickItemListener listener) {
            super(view);
            txtTitle = view.findViewById(R.id.txt_ctrl_notification_title);
            txtDescription = view.findViewById(R.id.txt_description);
            txtTime = view.findViewById(R.id.txt_ctrl_notification_time);
            txtDate = view.findViewById(R.id.txt_ctrl_notification_date);
            btnClear = view.findViewById(R.id.btn_delete);
            btnClear.setOnClickListener(v -> {
                if (listener != null)
                    listener.onClick(getLayoutPosition());
            });
        }

    }
}
