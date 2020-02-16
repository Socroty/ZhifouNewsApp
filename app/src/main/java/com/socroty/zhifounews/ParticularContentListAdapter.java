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
    private String[] content_data;

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

        String[] data_info = content_data[i + 1].split("<bb>");
        switch (data_info[0]) {
            case "title":
                test_list_title.setText(data_info[1]);
                TextPaint textPaint = test_list_title.getPaint();
                textPaint.setFakeBoldText(true);
                test_list_content.setVisibility(View.GONE);
                test_list_mark.setVisibility(View.GONE);
                test_list_image.setVisibility(View.GONE);
                break;
            case "content":
                test_list_content.setText("　　" + data_info[1]);
                test_list_title.setVisibility(View.GONE);
                test_list_mark.setVisibility(View.GONE);
                test_list_image.setVisibility(View.GONE);
                break;
            case "image":
                test_list_image.setImageURL(data_info[1]);
                test_list_title.setVisibility(View.GONE);
                test_list_content.setVisibility(View.GONE);
                test_list_mark.setVisibility(View.GONE);
                break;
            case "mark":
                test_list_mark.setText("▲ " + data_info[1]);
                test_list_title.setVisibility(View.GONE);
                test_list_image.setVisibility(View.GONE);
                test_list_content.setVisibility(View.GONE);
                break;
            default:
                MySnackbar.Custom(listViewHolder.view, "发生未知错误！", 1000 * 3)
                        .backgroundColor(0XFF333333)
                        .radius(24)
                        .margins(16, 16, 16, 16)
                        .show();
                break;
        }
    }

    @Override
    public int getItemCount() {
        NewsParticularActivity newsParticularActivity = new NewsParticularActivity();
        String news_content = newsParticularActivity.getDataContent();
        content_data = news_content.split("<aa>");
        int number = content_data.length;
        return number - 1;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        View view;
        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
