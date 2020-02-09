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

public class QualityNewsTitleListAdapter extends RecyclerView.Adapter<QualityNewsTitleListAdapter.ListViewHolder> {

    private Context context;
    String extra_data;
    String[] data_title;
    String[] data_id;
    String[] data_image_url;

    QualityNewsTitleListAdapter(Context contexts) {
        this.context = contexts;
    }

    @NonNull
    @Override
    public QualityNewsTitleListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.quality_news_title_list_body, viewGroup, false);
        return new QualityNewsTitleListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QualityNewsTitleListAdapter.ListViewHolder listViewHolder, final int i) {

        CardView cardView = listViewHolder.view.findViewById(R.id.quality_news_list_card);
        MyRadiusImageView imageView = listViewHolder.view.findViewById(R.id.quality_news_list_card_image);

        TextView textView = listViewHolder.view.findViewById(R.id.quality_news_list_title);
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);

        SharedPreferences pref = context.getSharedPreferences("user_info", MODE_PRIVATE);
        //boolean user_permit = pref.getBoolean("user_permit", false);
        final String user_id = pref.getString("user_id", "null");

        if (i == 0){
            final SocketClient socketClient = new SocketClient();
            extra_data = "null";
            try {
                extra_data = socketClient.getDataInfo("data_get_title/#/Science");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] data_all = extra_data.split("/@/");
            data_title = data_all[0].split("/%/");
            data_id = data_all[1].split("/%/");
            data_image_url = data_all[2].split("/%/");
        }

        textView.setText(data_title[i+1]);
        imageView.setImageURL(data_image_url[i+1]);

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

    @Override
    public int getItemCount() {
        return 20;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        View view;
        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
