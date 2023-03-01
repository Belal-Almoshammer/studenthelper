package com.pandasoft.studenthelper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pandasoft.studenthelper.Entities.EntityTipsAndAdvice;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;

import java.util.ArrayList;
import java.util.List;

public class AdapterTips extends RecyclerView.Adapter<AdapterTips.TipViewHolder> {
    private static boolean is_admin;
    private final Context mContext;
    public onItemOptionClickLister mListener;
    private List<EntityTipsAndAdvice> mList = new ArrayList<>();
    private final String user_id;


    public AdapterTips(Context context, boolean is_admn) {
        this.mContext = context;
        is_admin = is_admn;
        user_id = MyToolsCls.getCurrentUserId(context);
    }

    public void setOnItemOptionClick(onItemOptionClickLister listener) {
        this.mListener = listener;
    }

    public void setAdapter(List<EntityTipsAndAdvice> lst) {
        //setup new list
        if (this.mList == null) {
            this.mList = new ArrayList<>();
        }
        mList.clear();
        if (lst != null) {
            mList.addAll(lst);
            notifyDataSetChanged();
        }
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_tip, parent, false);
        return new TipViewHolder(itemView, mListener).setAdapterBook(this);
    }

    @Override
    public void onBindViewHolder(TipViewHolder holder, final int position) {
        EntityTipsAndAdvice entity = this.mList.get(position);
        holder.txtTitle.setText(entity.getTitle());
        String note = entity.getNote();
        if (note != null && note.length() > 50) {
            int last_index = note.length() - 1;
            char[] chrs = note.toCharArray();
            while (chrs[last_index] != ' ') {
                last_index--;
            }
            note = note.substring(0, last_index);
            note = note + " ...";
        }
        holder.txtDescription.setText(note);
        if(entity.getUser_update_id() != null && !entity.getUser_update_id().equals(user_id)){
            holder.btnItemOption.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public interface onItemOptionClickLister {
        void onItemOptionClick(int position);
    }

    public static class TipViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtDescription;
        private final Button btnItemOption;

        AdapterTips adapterTips;

        public TipViewHolder(View itemView, onItemOptionClickLister listener) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_tip_title);
            txtDescription = itemView.findViewById(R.id.txt_tip_description);
            btnItemOption = itemView.findViewById(R.id.btn_action_tip);

            if (is_admin) {
                btnItemOption.setOnClickListener(v -> {
                    int position = getLayoutPosition();
                    listener.onItemOptionClick(position);
                });
            } else btnItemOption.setVisibility(View.GONE);

        }

        public TipViewHolder setAdapterBook(AdapterTips adapter) {
            this.adapterTips = adapterTips;
            return this;
        }
    }
}
