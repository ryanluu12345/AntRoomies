package com.example.ryanluu2017.roomright;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ryanluu2017 on 3/22/2018.
 */

public class simpleBrowseRvAdapter extends RecyclerView.Adapter<simpleBrowseRvAdapter.UserViewHolder> {

    //Initializes users list
    ArrayList<User> users;

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        //Initializes UI variables
        private CardView mUserCv;
        private ImageView mProfPicIv;
        private TextView mPersonNameTv;
        private TextView mPersonAboutTv;
        private Button mPersonLikeBtn;

        UserViewHolder(View itemView) {

            super(itemView);
            mUserCv = (CardView) itemView.findViewById(R.id.sm_profile_cv);
            mProfPicIv = (ImageView) itemView.findViewById(R.id.profile_pic_cv);
            mPersonNameTv = (TextView) itemView.findViewById(R.id.person_name_cv);
            mPersonAboutTv = (TextView) itemView.findViewById(R.id.about_me_cv);
            mPersonLikeBtn = (Button) itemView.findViewById(R.id.like_cv);

        }


    }

    public simpleBrowseRvAdapter(ArrayList<User> users) {

        this.users=users;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_card_view_sm,viewGroup,false);         UserViewHolder mUvh=new UserViewHolder(v);
        return mUvh;

    }

    //Method that sets all of the information from the arraylist of data
    @Override
    public void onBindViewHolder(UserViewHolder holder, int pos) {

        //Sets the profile picture
        //TODO uncomment after image storage solution new DownLoadImageTask(holder.mProfPicIv).execute(users.get(pos).picture);

        //Sets the name and about me section
        holder.mPersonNameTv.setText(users.get(pos).name);
        holder.mPersonAboutTv.setText(users.get(pos).about);
        holder.mUserCv.setTag(users.get(pos).uid);

        //Sets onClickListener to each holder element
        holder.mUserCv.setOnClickListener(new SimpleBrowseOnClickListener());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    //Downloads the image from the link and then sets the profile picture image view
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public Bitmap resizeBitmap(Bitmap bm){
            int scaleToUse = 20; // this will be our percentage
            int sizeY = bm.getHeight() * scaleToUse / 100;
            int sizeX = bm.getWidth() * sizeY / bm.getHeight();
            bm= Bitmap.createScaledBitmap(bm,sizeX,sizeY,false);
            return bm;
        }

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap pic = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                pic = BitmapFactory.decodeStream(is);
                pic= resizeBitmap(pic);


            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return pic;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    };


    public class SimpleBrowseOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(final View view) {
            //Extracts the user id from the view tag
            String uid=view.getTag().toString();

            //Creates an intent and attaches the user id
            Intent userProfNavIntent=new Intent(view.getContext(),UserProfileActivity.class);
            userProfNavIntent.putExtra("uid",uid);
            view.getContext().startActivity(userProfNavIntent);

        }
    }
}
