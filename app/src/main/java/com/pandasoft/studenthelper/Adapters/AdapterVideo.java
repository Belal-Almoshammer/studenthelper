package com.pandasoft.studenthelper.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pandasoft.studenthelper.Entities.EntityVideo;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;

import java.util.ArrayList;
import java.util.List;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.CourseHolder> {
    private static boolean is_admin;
    List<EntityVideo> mList;
    Context mContext;
    OnActionClickListener mListener;
    private final String user_id;
    @LayoutRes
    int layout_resource_id;

    public AdapterVideo(Context context, boolean is_admn) {
        layout_resource_id = R.layout.list_item_video;
        mContext = context;
        is_admin = is_admn;
        user_id = MyToolsCls.getCurrentUserId(context);
    }

    public void setOnClickActionListener(OnActionClickListener listener) {
        mListener = listener;
    }

    public void setAdapter(List<EntityVideo> list) {
        if (mList == null) mList = new ArrayList<>();
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }


    @Override
    public CourseHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layout_resource_id, parent, false);
        return new CourseHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder( CourseHolder holder, int position) {
        EntityVideo entity = mList.get(position);
        holder.txtTitle.setText(entity.getTitle());
        holder.txtDescription
                .setText(String.format("%s . %s"
                        , entity.getDate_create()
                        , entity.getUser_name()));

        // get youtube id
        String vid_rul = MyToolsCls.getYoutubeId(entity.getCloud_url());
        String full_url = "https://img.youtube.com/vi/" + vid_rul + "/0.jpg";
        //show image from url
       // Toast.makeText(mContext, "url:" + full_url, Toast.LENGTH_LONG).show();
        //Toast.makeText(mContext, "real:" + entity.getCloud_url(), Toast.LENGTH_LONG).show();
        Glide.with(mContext)
                .asBitmap()
                .centerCrop()
                .load(full_url)
                .into(holder.img);
        if(entity.getUser_update_id() != null && !entity.getUser_update_id().equals(this.user_id)){
            holder.btnAction.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface OnActionClickListener {
        void ItemCard(int position);
        void optionClick(int position);
    }

    public static class CourseHolder extends RecyclerView.ViewHolder {
        final TextView txtTitle, txtDescription;
        final ImageView img;
        final Button btnAction;
        final LinearLayout layout;

        @SuppressLint("ResourceType")
        public CourseHolder(View itemView, OnActionClickListener listener) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_video_title);
            txtDescription = itemView.findViewById(R.id.list_video_description);
            layout = itemView.findViewById(R.id.list_view_video_layout);
            btnAction = itemView.findViewById(R.id.btn_action);
            img = itemView.findViewById(R.id.list_video_img);
            if (is_admin) {
                btnAction.setOnClickListener(v -> {
                    int position = getLayoutPosition();
                    if (listener != null && position != -1) {
                        listener.optionClick(position);
                    }
                });
            } else {
                btnAction.setVisibility(View.GONE);
            }

            layout.setOnClickListener(v -> {
                listener.ItemCard(getLayoutPosition());
            });

        }

    }
}
