package com.example.moststarredgithubrepos_challenge;


import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RepoAdapter extends ArrayAdapter<Repo>{

    private Context mContext;
    private List<Repo> mRepoList = new ArrayList<>();

    public RepoAdapter(@NonNull Context context, ArrayList<Repo> list)
    {
        super(context, 0 , list);
        mContext = context;
        mRepoList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_repo,parent,false);

        //get the current repo on the listview
        Repo currentRepo =  mRepoList.get(position);



        TextView name = (TextView) listItem.findViewById(R.id.repo_name);
        name.setText(currentRepo.getRepoName());

        TextView repoDescription = (TextView) listItem.findViewById(R.id.repo_description);
        repoDescription.setText(currentRepo.getRepoDescription());

        ImageView avatarImage = listItem.findViewById(R.id.owner_avatar_image);

        TextView repoStarsNumber =(TextView) listItem.findViewById(R.id.repo_stargazers);
        repoStarsNumber.setText(currentRepo.getStargazersCount()+"");

        TextView owenerNameTextView = (TextView)  listItem.findViewById(R.id.owner_name);
        owenerNameTextView.setText(currentRepo.getRepoOwnerName());

        //a task to load avatar image of the owner
        new DownloadImageTask(avatarImage)
                .execute(currentRepo.getOwnerAvatarUrl());

        return listItem;
    }


    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls)
        {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result)
        {
            bmImage.setImageBitmap(result);
        }
    }

}
