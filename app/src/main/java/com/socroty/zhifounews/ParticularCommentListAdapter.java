package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class ParticularCommentListAdapter extends RecyclerView.Adapter<ParticularCommentListAdapter.ListViewHolder> {

    private Context context;

    ParticularCommentListAdapter(Context contexts) {
        this.context = contexts;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.particular_comment_list_body, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    @SuppressLint({"RecyclerView", "SetTextI18n"})
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, final int i) {
        ImageView news_particular_comment_image = listViewHolder.view.findViewById(R.id.news_particular_comment_image);
        TextView news_particular_comment_name = listViewHolder.view.findViewById(R.id.news_particular_comment_name);
        TextView news_particular_comment_content = listViewHolder.view.findViewById(R.id.news_particular_comment_content);
        news_particular_comment_image.setImageResource(R.drawable.cover_default);

        NewsParticularActivity newsParticularActivity = new NewsParticularActivity();
        String data_comment = newsParticularActivity.getDataComment();

        String[] comment_data = Objects.requireNonNull(data_comment).split("/@/");
        String[] comment_user_name = comment_data[0].split("/%/");
        String[] comment_content = comment_data[1].split("/%/");

        for (int j = 0; j < comment_user_name.length; j++) {
            if (i == j){
                news_particular_comment_image.setImageResource(R.drawable.cover_default);
                news_particular_comment_name.setText(comment_user_name[j+1].replace("/NN/","\n"));
                news_particular_comment_content.setText(comment_content[j+1].replace("/NN/","\n"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        View view;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
