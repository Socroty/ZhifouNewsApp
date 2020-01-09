package com.socroty.zhifounews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class VarietyListAdapter extends RecyclerView.Adapter<VarietyListAdapter.ViewHolder> {

    private Context context;

    VarietyListAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.variety_list_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TextView textView_0 = viewHolder.view.findViewById(R.id.variety_list_card_text_1);
        TextView textView_1 = viewHolder.view.findViewById(R.id.variety_list_card_text_2);
        CardView cardView = viewHolder.view.findViewById(R.id.variety_list_card);
        View line = viewHolder.view.findViewById(R.id.variety_list_card_line);
        ImageView imageView = viewHolder.view.findViewById(R.id.variety_list_card_image);

        //推荐
        if (i == 0) {
            textView_0.setText("  " + "推荐");
            textView_1.setText("   " + "Preference");
            imageView.setImageResource(R.drawable.variety_recommend);
            cardView.setCardBackgroundColor(Color.parseColor("#d5d5d5"));

            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#3a3a3a"));
            line.setBackground(drawable);

            textView_0.setTextColor(Color.parseColor("#3a3a3a"));
            textView_1.setTextColor(Color.parseColor("#3a3a3a"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Preference";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }

        /*
        //头条
        if (i == 1) {
            textView_0.setText("  " + "头条");
            textView_1.setText("   " + "Headline");
            imageView.setImageResource(R.drawable.variety_headline);
            cardView.setCardBackgroundColor(Color.parseColor("#1a1d22"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#d6dee1"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#d6dee1"));
            textView_1.setTextColor(Color.parseColor("#d6dee1"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Headline";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }
        */

        //国内
        if (i == 1) {
            textView_0.setText("  " + "国内");
            textView_1.setText("   " + "Domestic");
            imageView.setImageResource(R.drawable.variety_domestic);
            cardView.setCardBackgroundColor(Color.parseColor("#411403"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#f19d2d"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#f19d2d"));
            textView_1.setTextColor(Color.parseColor("#f19d2d"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Domestic";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }

        //国际
        if (i == 2) {
            textView_0.setText("  " + "国际");
            textView_1.setText("   " + "International");
            imageView.setImageResource(R.drawable.variety_international);
            cardView.setCardBackgroundColor(Color.parseColor("#202f2a"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#dfb17a"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#dfb17a"));
            textView_1.setTextColor(Color.parseColor("#dfb17a"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "International";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }

        //社会
        if (i == 3) {
            textView_0.setText("  " + "社会");
            textView_1.setText("   " + "Society");
            imageView.setImageResource(R.drawable.variety_society);
            cardView.setCardBackgroundColor(Color.parseColor("#01326d"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#acd7f0"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#acd7f0"));
            textView_1.setTextColor(Color.parseColor("#acd7f0"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Society";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }

        //体育
        if (i == 4) {
            textView_0.setText("  " + "体育");
            textView_1.setText("   " + "Sports");
            imageView.setImageResource(R.drawable.variety_sports);
            cardView.setCardBackgroundColor(Color.parseColor("#032e36"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#8cc311"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#8cc311"));
            textView_1.setTextColor(Color.parseColor("#8cc311"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Sports";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }

        //娱乐
        if (i == 5) {
            textView_0.setText("  " + "娱乐");
            textView_1.setText("   " + "Amusement");
            imageView.setImageResource(R.drawable.variety_entertainment);
            cardView.setCardBackgroundColor(Color.parseColor("#5a252b"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#14c9f1"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#14c9f1"));
            textView_1.setTextColor(Color.parseColor("#14c9f1"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Amusement";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }

        //军事
        if (i == 6) {
            textView_0.setText("  " + "军事");
            textView_1.setText("   " + "Military");
            imageView.setImageResource(R.drawable.variety_military);
            cardView.setCardBackgroundColor(Color.parseColor("#005099"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#f1b101"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#f1b101"));
            textView_1.setTextColor(Color.parseColor("#f1b101"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Military";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }

        //经济
        if (i == 7) {
            textView_0.setText("  " + "财经");
            textView_1.setText("   " + "Economics");
            imageView.setImageResource(R.drawable.variety_economic);
            cardView.setCardBackgroundColor(Color.parseColor("#f5cb5f"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#4b454d"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#4b454d"));
            textView_1.setTextColor(Color.parseColor("#4b454d"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Economics";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);

                    context.startActivity(intent);
                }
            });
        }

        //流行
        if (i == 8) {
            textView_0.setText("  " + "时尚");
            textView_1.setText("   " + "Fashion");
            imageView.setImageResource(R.drawable.variety_fashion);
            cardView.setCardBackgroundColor(Color.parseColor("#feac94"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#ba075c"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#ba075c"));
            textView_1.setTextColor(Color.parseColor("#ba075c"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Fashion";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }

        //科技
        if (i == 9) {
            textView_0.setText("  " + "科技");
            textView_1.setText("   " + "Science");
            imageView.setImageResource(R.drawable.variety_science);
            cardView.setCardBackgroundColor(Color.parseColor("#222930"));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.parseColor("#dccfbe"));
            line.setBackground(drawable);
            textView_0.setTextColor(Color.parseColor("#dccfbe"));
            textView_1.setTextColor(Color.parseColor("#dccfbe"));
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String data = "Science";
                    Intent intent = new Intent(context, NewsTitleListActivity.class);
                    intent.putExtra("Genre", data);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
