package com.pandasoft.studenthelper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pandasoft.studenthelper.Entities.EntityQuestion;
import com.pandasoft.studenthelper.R;

;

import java.util.ArrayList;
import java.util.List;

public class AdapterQuestion extends RecyclerView.Adapter<AdapterQuestion.QuestionHolder> {


    private static boolean is_admin;
    List<EntityQuestion> mList;
    Context mContext;
    OnClickItemListener mListener;

    public AdapterQuestion(Context context, boolean is_admn) {
        mContext = context;
        is_admin = is_admn;
    }

    public List<EntityQuestion> getList() {
        return mList;
    }

    public void setList(List<EntityQuestion> list) {
        if (mList == null) mList = new ArrayList<>();
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void add(EntityQuestion question) {
        this.mList.add(0, question);
        notifyItemInserted(0);
    }

    public void remove(int position) {
        this.mList.remove(position);
        notifyItemRemoved(position);
    }

    public void set(int position, EntityQuestion question) {
        this.mList.set(position, question);
        notifyItemChanged(position);
    }

    public EntityQuestion get(int position) {
        return this.mList.get(position);
    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_question, parent, false);
        return new QuestionHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder( QuestionHolder holder, int position) {
        EntityQuestion entity = mList.get(position);
        holder.txtTitle.setText(entity.getQuestion());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        mListener = listener;
    }

    public interface OnClickItemListener {
        void onClickAction(QuestionHolder handler, int position);
    }

    public static class QuestionHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        AdapterQuestion mAdapter;
        Button btnAction;

        public QuestionHolder( View view, OnClickItemListener listener) {
            super(view);
            txtTitle = view.findViewById(R.id.question_title);
            btnAction = view.findViewById(R.id.btn_action);
            if (is_admin) {
                btnAction.setOnClickListener(v -> {
                    int position = getLayoutPosition();
                    if (listener != null)
                        listener.onClickAction(this, position);
                });
            } else {
                btnAction.setVisibility(View.GONE);
            }
        }

        public QuestionHolder setAdapter(AdapterQuestion adapter) {
            mAdapter = adapter;
            return this;
        }
    }
}
