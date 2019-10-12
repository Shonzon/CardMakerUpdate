package com.example.cardmaker.adapter.myCard;



import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;

import android.os.Environment;

import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.cardmaker.BuildConfig;
import com.example.cardmaker.R;
import com.example.cardmaker.Send;
import com.example.cardmaker.models.MyCardModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MyCardAdaper extends RecyclerView.Adapter<MyCardAdaper.MyCardViewHolder> {
    private Context context;
    private List<MyCardModel> myCardModelList;
    File file;
    File picDir;
    ProgressDialog mProgressDialog;

    private OnAdapterItemClickListener mlistener;
    public interface OnAdapterItemClickListener{
        void OnItemClick(View v,int position);
    }

    public OnAdapterItemClickListener getMlistener() {
        return mlistener;
    }

    public void setMlistener(OnAdapterItemClickListener mlistener) {
        this.mlistener = mlistener;
    }
    public List<MyCardModel> getCasetlist() {
        return myCardModelList;
    }

    public void setCasetlist(List<MyCardModel> myCardModelList) {
        this.myCardModelList = myCardModelList;
    }


    public MyCardAdaper(Context context, List<MyCardModel> list) {
        this.context = context;
        this.myCardModelList = list;

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Sharing On process");
        mProgressDialog.setMessage("Please wait,Sharing On process ...");

    }

    @Override
    public MyCardViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cardview,parent,false);
        context=parent.getContext();
        return new MyCardAdaper.MyCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyCardViewHolder myCardViewHolder, int i) {

        final MyCardModel cardModel = myCardModelList.get(i);
        Glide.with(context).load(myCardModelList.get(i).getImage()).into(myCardViewHolder.imageView);


        myCardViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(cardModel.getImage(),context);
            }
        });
        myCardViewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage(cardModel.getImage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return myCardModelList.size();
    }


    public class MyCardViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Button save,share,delete;

        public MyCardViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            save=(Button) itemView.findViewById(R.id.mycard_save);
            share=(Button) itemView.findViewById(R.id.mycard_share);
            delete=(Button) itemView.findViewById(R.id.mycard_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getMlistener().OnItemClick(view,getAdapterPosition());
                }
            });
        }

    }
    private void shareImage(String url, final Context context){
        mProgressDialog.show();
        Picasso.get().load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                picDir = null;
                picDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"CardMaker");

                if (!picDir.exists()){
                    picDir.mkdirs();
                    Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                }
                ByteArrayOutputStream bao = null;
                file = null;
                try {
                    bao = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
                    String filename = picDir.getPath() +File.separator+ System.currentTimeMillis()+".jpg";
                    file = new File(filename);
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bao.toByteArray());
                    fos.close();
                    if (file != null){
                        Intent i = new Intent();
                        i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri photoURI = FileProvider.getUriForFile(context,
                                BuildConfig.APPLICATION_ID,file);
                        i.setAction(Intent.ACTION_SEND);
                        i.setType("image/*");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Sending Image of My Created Cards.");
                        i.putExtra(Intent.EXTRA_TEXT, "PLEASE CHECK THE LINK TO DOWNLOAD THE APP \n https://drive.google.com/open?id=1P0mxx_nJgQpbubXDDb7iP49irJadIACX");
                        i.putExtra(Intent.EXTRA_STREAM, photoURI);
                        try {
                            context.startActivity(Intent.createChooser(i, "Share My Cards"));
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(context,"Sharing",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                scanGallery(context,file.getAbsolutePath());
                mProgressDialog.dismiss();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }
    private void sendImage(String url){
        mProgressDialog.show();
        Picasso.get().load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent intent = new Intent(context, Send.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                intent.putExtra("byteArray", bs.toByteArray());
                context.startActivity(intent);
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                mProgressDialog.dismiss();
                Toast.makeText(context,"DOWNLOAD FAILED",Toast.LENGTH_LONG).show();
            }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }

}
