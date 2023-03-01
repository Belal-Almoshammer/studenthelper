package com.pandasoft.studenthelper.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;
import com.pandasoft.studenthelper.Entities.EntitySubjectsLevel;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;

;

import java.util.ArrayList;
import java.util.List;

public class AdapterSubject extends RecyclerView.Adapter<AdapterSubject.HolderSubject> {
    OnClickItemListener mListener;
    List<EntitySubjectsLevel> mList;
    Context mContext;


    public AdapterSubject(Context context) {
        mContext = context;
    }

    public void setAdapter(List<EntitySubjectsLevel> list) {
        if (mList == null) mList = new ArrayList<>();
        mList.clear();
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }


    @Override
    public HolderSubject onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_subject, parent, false);
        return new HolderSubject(view, mListener).setAdapter(this);
    }

    @Override
    public void onBindViewHolder(HolderSubject holder, int position) {

        EntitySubjectsLevel entity = mList.get(position);
        holder.txtName.setText(entity.getSubject_name());
        String image_name = entity.getImg_name();
        Log.i("Image_name", "onBindViewHolder: " + image_name);
        try {
            if (!Strings.isEmptyOrWhitespace(image_name))
                MyToolsCls.setImageSrc(mContext, holder.subjectImage, image_name);
            holder.subjectImage.setBackgroundColor(Color.parseColor(entity.getColor_code()));
        } catch (Exception ex) {
            Log.i("DEBUG_ERROR", "onBindViewHolder: " + ex.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        mListener = listener;
    }

    public interface OnClickItemListener {
        void onClick(int position, HolderSubject holder);
    }

    public static class HolderSubject extends RecyclerView.ViewHolder {
        public final TextView txtName;
        public final ImageView subjectImage;

        public HolderSubject(View view, OnClickItemListener listener) {
            //Initialize
            super(view);

            txtName = view.findViewById(R.id.subject_name);
            subjectImage = view.findViewById(R.id.subject_image);
            CardView mCard = view.findViewById(R.id.subject_card);
            // Events
            mCard.setOnClickListener(v -> {
                int position = getLayoutPosition();
                if (position != -1 && listener != null)
                    listener.onClick(position, this);
            });

        }

        public HolderSubject setAdapter(AdapterSubject adapter) {
            return this;
        }
    }
}
