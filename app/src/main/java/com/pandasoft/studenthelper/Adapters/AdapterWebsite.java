package com.pandasoft.studenthelper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pandasoft.studenthelper.Entities.EntityWebsites;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;

import java.util.ArrayList;
import java.util.List;

;

public class AdapterWebsite extends RecyclerView.Adapter<AdapterWebsite.WebsiteHolder> {
    private static boolean is_admin;
    private final String user_id;
    List<EntityWebsites> mList;
    OnClickActionListener mListener;
    Context mContext;


    public AdapterWebsite(Context context, boolean is_admn) {
        this.mContext = context;
        is_admin = is_admn;
        user_id = MyToolsCls.getCurrentUserId(context);
    }

    public void setOnClickActionListener(OnClickActionListener listener) {
        mListener = listener;
    }

    public void setAdapter(List<EntityWebsites> list) {
        if (mList == null) mList = new ArrayList<>();
        mList.clear();
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

    }


    @Override
    public WebsiteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_website, parent, false);
        return new WebsiteHolder(view, mListener).setAdapter(this);
    }

    @Override
    public void onBindViewHolder(WebsiteHolder holder, int position) {
        EntityWebsites website = mList.get(position);
        holder.txtName.setText(website.getTitle());
        holder.txtLink.setText(website.getLink_web());
        if (website.getUser_update_id() != null && !website.getUser_update_id().equals(user_id)) {
            holder.btnAction.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    public interface OnClickActionListener {
        void onClick(int position);

        void onCardClick(int position);
    }

    public class WebsiteHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final TextView txtLink;
        private final Button btnAction;
        private final Button btnOpen;
        private AdapterWebsite mAdapter;

        public WebsiteHolder(View view, OnClickActionListener listener) {
            super(view);
            txtName = view.findViewById(R.id.list_website_name);
            txtLink = view.findViewById(R.id.list_website_link);
            btnOpen = view.findViewById(R.id.list_btn_open_website);
            btnAction = view.findViewById(R.id.btn_action_website);
            if (is_admin) {
                btnAction.setOnClickListener(v -> {
                    int position = getLayoutPosition();
                    if (position != -1 && listener != null) {
                        listener.onClick(position);
                    }
                });
            } else
                btnAction.setVisibility(View.GONE);


            btnOpen.setOnClickListener(v -> {
                int position = getLayoutPosition();
                if (position != -1 && listener != null) {
                    listener.onCardClick(position);
                }
            });
        }

        public WebsiteHolder setAdapter(AdapterWebsite adapter) {
            this.mAdapter = adapter;
            return this;
        }
    }
}
