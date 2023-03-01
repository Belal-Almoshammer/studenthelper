package com.pandasoft.studenthelper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pandasoft.studenthelper.Entities.EntityQuiz;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;

import java.util.ArrayList;
import java.util.List;

;

public class AdapterQuiz extends RecyclerView.Adapter<AdapterQuiz.Holder> {
    private static boolean is_admin;
    private final String user_id;
    Context mContext;
    List<EntityQuiz> mList;
    OnClickCardListener mListener;


    public AdapterQuiz(Context context, boolean is_admn) {
        this.mContext = context;
        is_admin = is_admn;
        user_id = MyToolsCls.getCurrentUserId(context);
    }

    public void setAdapter(List<EntityQuiz> list) {
        if (mList == null) mList = new ArrayList<>();
        this.mList.clear();
        if (list != null && list.size() > 0) this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setActionListener(OnClickCardListener listener) {
        this.mListener = listener;
    }

    @NonNull

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_quiz, parent, false);
        return new Holder(view, mListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        EntityQuiz entity = mList.get(position);
        holder.txtName.setText(entity.getName());
        holder.txtDescription.setText(entity.getDescription());
        holder.txtCount.setText(String.format("%s %s", entity.getQuestions_count(), "اسئلة"));
        holder.txtPoints.setText("");
        if (entity.getUser_update_id() != null && !entity.getUser_update_id().equals(user_id)) {
            holder.btnAction.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface OnClickCardListener {
        void onClick(int position, Holder holder);

        void onActionClick(int position);
    }

    public static class Holder extends RecyclerView.ViewHolder {

        final TextView txtName, txtDescription, txtCount, txtPoints;
        final CardView card;

        final Button btnAction;

        public Holder(View view, OnClickCardListener listener) {
            super(view);

            txtName = view.findViewById(R.id.quiz_name);
            txtDescription = view.findViewById(R.id.quiz_description);
            txtCount = view.findViewById(R.id.quiz_count);
            txtPoints = view.findViewById(R.id.quiz_points);
            card = view.findViewById(R.id.layout_card);
            btnAction = view.findViewById(R.id.btn_action);

            card.setOnClickListener(v -> {
                listener.onClick(getLayoutPosition(), this);
            });

            if (is_admin) {
                btnAction.setOnClickListener(v -> {
                    listener.onActionClick(getLayoutPosition());
                });
            } else {
                btnAction.setVisibility(View.GONE);
            }


        }
    }
}
