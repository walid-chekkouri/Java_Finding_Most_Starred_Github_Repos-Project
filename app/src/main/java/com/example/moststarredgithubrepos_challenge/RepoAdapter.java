package com.example.moststarredgithubrepos_challenge;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    private Context mContext;
    private List<Repo> mRepoList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView repoName, repoDescription;
        ImageView avatarImage;
        TextView repoStarsNumber;
        TextView owenerNameTextView;
        public ViewHolder(View itemView)
        {

            super(itemView);

            repoName = (TextView) itemView.findViewById(R.id.repo_name);

            repoDescription = (TextView) itemView.findViewById(R.id.repo_description);

            avatarImage = (ImageView) itemView.findViewById(R.id.owner_avatar_image);

            repoStarsNumber =(TextView) itemView.findViewById(R.id.repo_stargazers);

            owenerNameTextView = (TextView) itemView.findViewById(R.id.owner_name);

        }
    }

    public RepoAdapter(Context context, List<Repo> reposList )
    {
        mContext=context;
        mRepoList = reposList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Log.i("AEW", "Voila_9999");
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_repo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        Repo currentRepo = mRepoList.get(position);
        holder.repoName.setText(currentRepo.getRepoName());
        holder.repoDescription.setText(currentRepo.getRepoDescription());
        holder.repoStarsNumber.setText(currentRepo.getStargazersCount()+"");
        holder.owenerNameTextView.setText(currentRepo.getRepoOwnerName());


        //a task to load avatar image of the owner
        new DownloadImageTask(holder.avatarImage)
                .execute(currentRepo.getOwnerAvatarUrl());


    }


    @Override
    public int getItemCount()
    {
        return mRepoList.size();
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


    public void addAll(List<Repo> repoList )
    {
        mRepoList = repoList;
        this.notifyDataSetChanged();
    }

}
