package com.pandasoft.studenthelper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pandasoft.studenthelper.Entities.EntitySubjectChapters;
import com.pandasoft.studenthelper.R;

;

import java.util.ArrayList;
import java.util.List;

public class AdapterChapter extends RecyclerView.Adapter<AdapterChapter.ChapterHolder> {
    List<EntitySubjectChapters> mList;
    Context mContext;
    OnActionClickListener mListener;

    public AdapterChapter(Context context) {
        mContext = context;
    }

    public void setOnClickListener(OnActionClickListener listener) {
        mListener = listener;
    }

    public void setAdapter(List<EntitySubjectChapters> list) {
        if (mList == null) mList = new ArrayList<>();
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public ChapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_chapter, parent, false);
        return new ChapterHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder( ChapterHolder holder, int position) {
        EntitySubjectChapters entity = mList.get(position);
        holder.txtChapterName.setText(entity.getName());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface OnActionClickListener {
        void onClick(int position);
    }

    public static class ChapterHolder extends RecyclerView.ViewHolder {

        TextView txtChapterName;
        CardView card;

        public ChapterHolder( View itemView, OnActionClickListener listener) {
            super(itemView);
            txtChapterName = itemView.findViewById(R.id.txt_chapter_name);
            card = itemView.findViewById(R.id.card_view);
            card.setOnClickListener(v -> {
                listener.onClick(getLayoutPosition());
            });
        }
    }
}
