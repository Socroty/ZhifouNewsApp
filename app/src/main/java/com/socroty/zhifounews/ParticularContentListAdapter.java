package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ParticularContentListAdapter extends RecyclerView.Adapter<ParticularContentListAdapter.ListViewHolder> {

    private Context context;
    private static int number = 6;

    ParticularContentListAdapter(Context contexts) {
        this.context = contexts;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.particular_content_list_body, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    @SuppressLint({"RecyclerView", "SetTextI18n"})
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, final int i) {

        TextView test_list_title = listViewHolder.view.findViewById(R.id.news_particular_list_title);
        TextView test_list_content = listViewHolder.view.findViewById(R.id.news_particular_list_content);
        MyRadiusImageView test_list_image = listViewHolder.view.findViewById(R.id.news_particular_list_image);
        TextView test_list_mark = listViewHolder.view.findViewById(R.id.news_particular_list_mark);
        NewsParticularActivity newsParticularActivity = new NewsParticularActivity();
        String text = newsParticularActivity.getDataContent();

        String[] data = text.split("<aa>");
        number = data.length;
        for (int j = 0; j < data.length -1; j++) {
            String[] info = data[j+1].split("<bb>");
            if (i == j){
                switch (info[0]) {
                    case "title":
                        test_list_title.setText("\n" + info[1]);
                        TextPaint tp = test_list_title.getPaint();
                        tp.setFakeBoldText(true);
                        break;
                    case "content":
                        test_list_content.setText("　　"+info[1]);
                        break;
                    case "image":
                        test_list_image.setImageURL(info[1]);
                        break;
                    case "mark":
                        test_list_mark.setText("▲ "+info[1]);
                        break;
                    default:
                        MySnackbar.Custom(listViewHolder.view,"发生未知错误！",1000*3)
                                .backgroundColor(0XFF333333)
                                .radius(24)
                                .margins(16,16,16,16)
                                .show();
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return number -1;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        View view;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
