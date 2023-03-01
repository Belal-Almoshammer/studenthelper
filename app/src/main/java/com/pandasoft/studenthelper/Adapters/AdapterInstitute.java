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
import com.pandasoft.studenthelper.Entities.EntityInstitutes;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelInstitutes;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdapterInstitute extends RecyclerView.Adapter<AdapterInstitute.Holder> {
    private static boolean is_admin;
    private final Context context;
    private final String user_id;
    private final ViewModelInstitutes viewModelInstitutes;

    private List<EntityInstitutes> mList;
    private onButtonActionClickListener mListener;

    public AdapterInstitute(Context context, ViewModelInstitutes viewModel, boolean is_admn) {
        this.context = context;
        is_admin = is_admn;
        user_id = MyToolsCls.getCurrentUserId(context);
        this.viewModelInstitutes = viewModel;
    }

    public void setOnButtonActionClickListener(onButtonActionClickListener listener) {
        mListener = listener;
    }

    public void setListItems(List<EntityInstitutes> list) {
        if (mList == null)
            mList = new ArrayList<>();
        mList.clear();
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    @NonNull
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_institute, parent, false);
        return new Holder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        EntityInstitutes entity = mList.get(position);
        holder.txtName.setText(entity.getName());
        holder.txtBrief.setText(entity.getBrief());
        //hide more button if the user isn't the creator
        if (!user_id.equals(entity.getUser_update_id()) && entity.getIs_uploaded()) {
            holder.btnAction.setVisibility(View.GONE);
        }
        if (entity.getIs_uploaded()) {
            //load from url to image using Glide library
            if (MyToolsCls.isNetworkConnected(context)) {
                try {
                    URL url = new URL(entity.getCloud_img());
                    Glide.with(context)
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
            File file = MyToolsCls.fileIsDownloaded(context, entity.getLocal_img());
            Glide.with(context)
                    .load(file)
                    .centerCrop()
                    .fitCenter()
                    .into(holder.img);
            holder.btnUpload.setOnClickListener(v -> {
                if (file != null && file.exists()) {
                    Uri localUri = Uri.fromFile(file);
                    if (MyToolsCls.isNetworkConnected(context)) {
                        holder.progressUpload.setVisibility(View.VISIBLE);
                        holder.btnUpload.setVisibility(View.GONE);
                        new UploadCls.UploadFileTask("institutes", localUri, new UploadCls.OnUploadOrDownloadFileListener() {
                            @Override
                            public void onProgress(long value) {
                                //holder.progressUpload.setProgress((int) (value));
                            }

                            @Override
                            public void onFailure(String msg) {
                                holder.progressUpload.setVisibility(View.GONE);
                                holder.btnUpload.setVisibility(View.VISIBLE);
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(Uri uri) {
                                entity.setCloud_img(uri.toString());

                                new UploadCls.UploadEntityTask<>(context, "institutes", entity, new MyToolsCls.OnUpload() {
                                    @Override
                                    public void onSuccess() {
                                        holder.progressUpload.setVisibility(View.GONE);
                                        holder.btnUpload.setVisibility(View.GONE);
                                        //updating
                                        entity.setIs_uploaded(true);

                                        if(viewModelInstitutes != null)
                                        viewModelInstitutes.update(entity, msg -> {
                                        });
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        holder.progressUpload.setVisibility(View.GONE);
                                        holder.btnUpload.setVisibility(View.GONE);
                                    }
                                }).execute();
                            }
                        }).execute(); // end task
                    } else {
                        Toast.makeText(context, "لايوجد اتصال بالانترنت!", Toast.LENGTH_SHORT).show();
                    }
                }
            });//end button click
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public interface onButtonActionClickListener {
        void onClick(int position);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final TextView txtBrief;
        private final ImageView img;
        private final Button btnUpload;
        private final ProgressBar progressUpload;
        private final Button btnAction;

        public Holder(View item, onButtonActionClickListener listener) {
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
