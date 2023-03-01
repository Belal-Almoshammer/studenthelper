package com.pandasoft.studenthelper.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.Strings;
import com.google.android.material.button.MaterialButton;
import com.pandasoft.studenthelper.Entities.EntityBook;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.FilesCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelBook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.BookViewHolder> {
    private static boolean is_admin;
    private final Context mContext;
    private final String user_id;
    public onItemOptionClickLister mListener;
    ViewModelBook viewModelBook;
    private List<EntityBook> entityBooks = new ArrayList<>();

    public AdapterBook(Context context, boolean is_admn) {
        this.mContext = context;
        is_admin = is_admn;
        user_id = MyToolsCls.getCurrentUserId(mContext);
    }

    public void setViewModelBook(ViewModelBook model) {
        this.viewModelBook = model;
    }

    public void setOnItemOptionClick(onItemOptionClickLister listener) {
        this.mListener = listener;
    }

    public void setAdapter(List<EntityBook> lst) {
        //setup new list
        if (this.entityBooks == null) {
            this.entityBooks = new ArrayList<>();
        }
        entityBooks.clear();
        if (lst != null) {
            entityBooks.addAll(lst);
            notifyDataSetChanged();
        }
    }

    @Override
    @NonNull
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_book, parent, false);
        return new BookViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.entity = this.entityBooks.get(position);
        holder.txtBookName.setText(holder.entity.getName());
        holder.txtDescription.setText(holder.entity.getLevel_name());
        if (!user_id.equals(holder.entity.getUser_update_id())) {
            holder.btnTry.setVisibility(View.GONE);
        }
        downloadBookActions(holder, position);
    }

    @Override
    public int getItemCount() {
        if (entityBooks != null)
            return entityBooks.size();
        return 0;
    }

    private void downloadBookActions(BookViewHolder holder, int position) {
        File file = MyToolsCls.fileIsDownloaded(mContext, entityBooks.get(position).getLocal_url());
        //download or upload
        //1-check file if founded
        if (file != null && file.exists()) {
            //2-set click event to open file
            holder.card.setOnClickListener(v -> FilesCls.openPDF(mContext, file));

            //3-convert file path format to uri path format
            Uri uri = Uri.fromFile(file);
            byte[] data = MyToolsCls.generatePdfThumpNail(uri, mContext);
            //4-set book image
            Glide.with(mContext)
                    .load(data)
                    .centerCrop()
                    .into(holder.imgBookImg);
            //5-check file if uploaded
            if (!holder.entity.getIs_uploaded() && is_admin) {
                //6-show upload button
                holder.btnTry.setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_upload));
                holder.btnTry.setVisibility(View.VISIBLE);

                holder.txtStatus.setText("لم يتم الرفع");
                //7-set set upload button event
                holder.btnTry.setOnClickListener(v -> {
                    //8-check internet connection
                    if (!MyToolsCls.isNetworkConnected(mContext)) {
                        Toast.makeText(mContext, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //9-show loading progress for upload
                    holder.startLoading(0);
                    //10-get file name
                    String file_name = URLUtil.guessFileName(holder.entity.getLocal_url(), null, null);
                    //11-check update type if insert or update
                    if (holder.entity.getUpdate_type() <= 1 && file_name != null && file_name.length() > 1) {

                        UploadCls.UploadFileTask uploadFileTask = new UploadCls.UploadFileTask("book", uri, new UploadCls.OnUploadOrDownloadFileListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onProgress(long value) {
//                                int prg = (int) value;
//                                holder.linearProgress.setProgress(prg);
//                                holder.txtProgress.setText(value + " %");
                            }

                            @Override
                            public void onFailure(String msg) {
                                holder.stopLoading();
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(Uri uri) {
                                holder.entity.setCloud_url(uri.toString());
                                UploadCls.UploadEntityTask<EntityBook> task = new UploadCls.UploadEntityTask<>(mContext, "book", holder.entity, new MyToolsCls.OnUpload() {
                                    @Override
                                    public void onSuccess() {
                                        holder.entity.setIs_uploaded(true);
                                        viewModelBook.update(holder.entity, msg -> {
                                        });
                                        holder.stopLoading();
                                        holder.btnTry.setVisibility(View.GONE);
                                        Toast.makeText(mContext, "تم رفع الملف", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        holder.stopLoading();
                                        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                task.execute();//upload entity
                            }
                        });
                        uploadFileTask.execute();//upload the pdf file
                    } else {// Here we're uploading entity only
                        holder.entity.setCloud_url(uri.toString());
                        UploadCls.UploadEntityTask<EntityBook> task = new UploadCls.UploadEntityTask<>(mContext, "book", holder.entity, new MyToolsCls.OnUpload() {
                            @Override
                            public void onSuccess() {
                                holder.entity.setIs_uploaded(true);
                                viewModelBook.update(holder.entity, msg -> {
                                });
                                holder.stopLoading();
                                holder.btnTry.setVisibility(View.GONE);
                                Toast.makeText(mContext, "تم رفع الملف", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(String error) {
                                holder.stopLoading();
                                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                        task.execute();
                    }

                });
            } else { //else if file is downloaded
                holder.stopLoading();
                holder.btnTry.setVisibility(View.GONE);
            }
        } else {
            //downloading of book
            holder.btnTry.setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_download));
            holder.btnTry.setVisibility(View.VISIBLE);
            holder.txtStatus.setText("لم يتم التنزيل");

            //تحديد عند الضغط على الزر يتم التنزيل
            holder.btnTry.setOnClickListener(v -> {
                if (!MyToolsCls.isNetworkConnected(mContext)) return;
                holder.startLoading(1); //downloading process
                // normal process
                // DownloadFromPath(entityBooks.get(position).getCloud_url());
                // advanced process
                if (Strings.isEmptyOrWhitespace(holder.entity.getCloud_url())) return;
                Uri uri = Uri.parse(holder.entity.getCloud_url());
                new UploadCls.DownloadFileTask("book", uri, new UploadCls.OnUploadOrDownloadFileListener() {
                    @Override
                    public void onProgress(long value) {
//                        int prg = (int) value;
//                        holder.linearProgress.setProgress(prg);
//                        holder.txtProgress.setText(value + " %");
                    }

                    @Override
                    public void onFailure(String msg) {
                        holder.stopLoading();
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(Uri uri) {
                        holder.entity.setLocal_url(uri.toString());
                        holder.stopLoading();
                        holder.btnTry.setVisibility(View.GONE);
                        viewModelBook.update(holder.entity, msg -> {
                            //nothing
                        });
                    }
                }).execute();
            });
        }


    }

    public interface onItemOptionClickLister {
        void onItemOptionClick(int position);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtBookName;
        private final TextView txtDescription;
        private final ImageView imgBookImg;
        private final MaterialButton btnTry;
        private final ProgressBar linearProgress;
        private final ProgressBar loadingProgress;
        private final TextView txtStatus;
        private final TextView txtProgress;
        private final CardView card;
        private EntityBook entity;
        private Button btnOption;

        public BookViewHolder(View view, onItemOptionClickLister listener) {
            super(view);
            txtBookName = view.findViewById(R.id.txt_view_book_name);
            txtDescription = view.findViewById(R.id.txt_view_book_description);
            imgBookImg = view.findViewById(R.id.img_view_book);
            card = view.findViewById(R.id.card_view);

            btnOption = view.findViewById(R.id.btn_list_item_book_option);

            if (is_admin) {
                if (listener != null)
                    btnOption.setOnClickListener(v -> {
                        int position = getLayoutPosition();
                        listener.onItemOptionClick(position);
                    });
            } else {

                btnOption.setVisibility(View.GONE);

            }

            //Download and upload actions
            btnTry = view.findViewById(R.id.btn_try);
            linearProgress = view.findViewById(R.id.progress_linear);
            loadingProgress = view.findViewById(R.id.progress_loading);

            txtStatus = view.findViewById(R.id.txt_book_status);
            txtProgress = view.findViewById(R.id.txt_progress);

        }

        void startLoading(int process_type) {//0: upload, 1: download
            btnTry.setVisibility(View.GONE);
            loadingProgress.setVisibility(View.VISIBLE);

            txtStatus.setVisibility(View.VISIBLE);

            if (process_type == 0) {
                txtStatus.setText("جاري الرفع");
            } else if (process_type == 1) {
                txtStatus.setText("جاري التنزيل");
            }

            txtProgress.setText("0%");
            txtProgress.setVisibility(View.VISIBLE);

            linearProgress.setVisibility(View.VISIBLE);
            linearProgress.setProgress(0);
        }

        void stopLoading() {
            loadingProgress.setVisibility(View.GONE);

            txtStatus.setVisibility(View.GONE);

            txtProgress.setVisibility(View.GONE);

            linearProgress.setVisibility(View.GONE);
        }
    }
}
