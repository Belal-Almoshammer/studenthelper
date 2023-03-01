package com.pandasoft.studenthelper.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pandasoft.studenthelper.Entities.EntityUniversities;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelUniversities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdapterUniversity extends RecyclerView.Adapter<AdapterUniversity.Holder> {

    private static boolean is_admin;
    private final Context mContext;
    private final String user_id;
    private onClickItemListener mListener;
    private List<EntityUniversities> mList;
    private ViewModelUniversities viewModelUniversities;

    public AdapterUniversity(Context context, boolean is_admn) {
        mContext = context;
        is_admin = is_admn;
        user_id = MyToolsCls.getCurrentUserId(context);
    }

    public void setViewModel(ViewModelUniversities viewModel) {
        this.viewModelUniversities = viewModel;
    }

    public void setAdapter(List<EntityUniversities> list) {
        if (mList == null)
            mList = new ArrayList<>();
        mList.clear();
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void setOnClickItemListener(onClickItemListener listener) {
        mListener = listener;
    }


    @Override
    @NonNull
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_university, parent, false);
        return new Holder(view, mListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        EntityUniversities entity = mList.get(position);
        holder.txtName.setText(entity.getName());
        holder.txtBrief.setText(entity.getBrief());
        //hide more button if the user isn't the creator
        if (!user_id.equals(entity.getUser_update_id()) && entity.getIs_uploaded()) {
            holder.btnAction.setVisibility(View.GONE);
        }
        if (entity.getIs_uploaded()) {
            //load from url to image using Glide library
            if (MyToolsCls.isNetworkConnected(mContext)) {
                try {
                    URL url = new URL(entity.getCloud_img());
                    Glide.with(mContext)
                            .load(url)
                            .fitCenter()
                            .centerCrop()
                            .into(holder.img);
                } catch (MalformedURLException e) {
                    Log.e("onBindViewHolder", e.getMessage());
                }
            }
        } else if (is_admin) {

            holder.btnUpload.setVisibility(View.VISIBLE);
            File file = MyToolsCls.fileIsDownloaded(mContext, entity.getLocal_img());
            if (file != null)
                Glide.with(mContext)
                        .load(file)
                        .centerCrop()
                        .fitCenter()
                        .into(holder.img);

            holder.btnUpload.setOnClickListener(v -> {
                if (file != null && file.exists()) {
                    if (MyToolsCls.isNetworkConnected(mContext)) {
                        Uri localUri = Uri.fromFile(file);

                        holder.progressUpload.setVisibility(View.VISIBLE);
                        holder.btnUpload.setVisibility(View.GONE);

                        new UploadCls.UploadFileTask("universities", localUri, new UploadCls.OnUploadOrDownloadFileListener() {
                            @Override
                            public void onProgress(long value) {
                                //holder.progressUpload.setProgress((int) (value));
                            }

                            @Override
                            public void onFailure(String msg) {
                                holder.progressUpload.setVisibility(View.GONE);
                                holder.btnUpload.setVisibility(View.VISIBLE);
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(Uri uri) {
                                entity.setCloud_img(uri.toString());
                                new UploadCls.UploadEntityTask<>(mContext, "universities", entity, new MyToolsCls.OnUpload() {
                                    @Override
                                    public void onSuccess() {
                                        holder.progressUpload.setVisibility(View.GONE);
                                        holder.btnUpload.setVisibility(View.GONE);

                                        entity.setIs_uploaded(true);
                                        viewModelUniversities.update(entity, msg -> {
                                        });
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        holder.progressUpload.setVisibility(View.GONE);
                                        holder.btnUpload.setVisibility(View.GONE);
                                    }
                                }).execute(); // end task of upload entity
                            }
                        }).execute(); // end task upload file
                    } else {
                        Toast.makeText(mContext, "لايوجد اتصال بالانترنت!", Toast.LENGTH_SHORT).show();
                    }
                }// end if file exist
            });//end button click
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    public interface onClickItemListener {
        void onClick(int position);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final TextView txtBrief;
        private final ImageView img;
        private final Button btnUpload;
        private final ProgressBar progressUpload;
        private final Button btnAction;


        public Holder(View item, onClickItemListener listener) {
            super(item);
            txtName = item.findViewById(R.id.item_title);
            txtBrief = item.findViewById(R.id.item_description);
            img = item.findViewById(R.id.item_image);
            btnUpload = item.findViewById(R.id.btn_upload);
            progressUpload = itemView.findViewById(R.id.progress_upload);

            btnAction = item.findViewById(R.id.item_btn_action);


            if (is_admin) {
                btnAction.setOnClickListener(v -> {
                    int position = getLayoutPosition();
                    if (position != -1 && listener != null) {
                        listener.onClick(position);
                    }
                });
            } else btnAction.setVisibility(View.GONE);
        }
    }
}
