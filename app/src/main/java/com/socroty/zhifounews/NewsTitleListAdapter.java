package com.socroty.zhifounews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class NewsTitleListAdapter extends RecyclerView.Adapter<NewsTitleListAdapter.ListViewHolder> {

    private Context context;

    NewsTitleListAdapter(Context contexts) {
        this.context = contexts;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_title_list_body, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, final int i) {

        String[] data;
        final String[] data_title;
        final String[] data_id;
        final String[] data_image_url;

        CardView cardView = listViewHolder.view.findViewById(R.id.news_list_card);
        MyRadiusImageView imageView = listViewHolder.view.findViewById(R.id.news_list_card_image);

        TextView textView = listViewHolder.view.findViewById(R.id.news_list_title);
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);

        SharedPreferences pref = context.getSharedPreferences("user_info", MODE_PRIVATE);
        boolean user_permit = pref.getBoolean("user_permit", false);
        final String user_id = pref.getString("user_id", "null");

        NewsTitleListActivity newsTitleListActivity = new NewsTitleListActivity();
        String extra_data = newsTitleListActivity.getExtraData();

        data = extra_data.split("/@/");
        data_title = data[0].split("/%/");
        data_id = data[1].split("/%/");
        data_image_url = data[2].split("/%/");

        for (int j = 0; j < data_title.length - 1; j++) {
            if (i == j) {
                if (data_image_url[j + 1].equals("null.image")) {
                    //textView.setPadding(48,0,48,0);
                    textView.setText(data_title[j + 1]);
                    imageView.setImageResource(R.drawable.cover_default);
                } else {
                    textView.setText(data_title[j + 1]);
                    imageView.setImageURL(data_image_url[j + 1]);
                }
                cardView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                        String data = null;
                        final SocketClient socketClient = new SocketClient();
                        try {
                            data = socketClient.getDataInfo("data_get_info" + "/#/" + data_id[i + 1] + "/&/" + user_id);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (data != null) {
                            Intent intent = new Intent(context, NewsParticularActivity.class);
                            intent.putExtra("extra_id", data);
                            context.startActivity(intent);
                        } else {
                            Intent intent_error = new Intent();
                            intent_error.setClass(context, ErrorActivity.class);
                            intent_error.putExtra("extra_data", "服务离线！请点此返回重试！");
                            context.startActivity(intent_error);
                            Activity activity = (Activity) context;
                            activity.finish();
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        NewsTitleListActivity newsTitleListActivity = new NewsTitleListActivity();
        int title_length = newsTitleListActivity.getTitleLength();
        return title_length - 1;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        View view;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
