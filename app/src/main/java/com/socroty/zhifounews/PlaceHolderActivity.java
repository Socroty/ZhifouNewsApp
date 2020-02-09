package com.socroty.zhifounews;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PlaceHolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_holder_activity);

        //设置白底黑字状态栏
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Intent intent = getIntent();
        String data = intent.getStringExtra("extra_data");
        String type = intent.getStringExtra("extra_type");

        ImageView imageView = findViewById(R.id.place_holder_image);
        TextView textView = findViewById(R.id.place_holder_text);

        assert type != null;
        switch (type) {
            case "blank": {
                imageView.setImageResource(R.drawable.activity_blank);
                textView.setText(data);
                final CustomPaintDrawable customPaintDrawable = new CustomPaintDrawable();
                textView.setBackground(customPaintDrawable.paintDrawable(80, "#333333"));
                break;
            }
            case "warning": {
                imageView.setImageResource(R.drawable.activity_warning);
                textView.setText(data);
                final CustomPaintDrawable customPaintDrawable = new CustomPaintDrawable();
                textView.setBackground(customPaintDrawable.paintDrawable(80, "#333333"));
                break;
            }
            case "wechat": {
                imageView.setImageResource(R.drawable.wechat);
                textView.setText(data);
                final CustomPaintDrawable customPaintDrawable = new CustomPaintDrawable();
                textView.setBackground(customPaintDrawable.paintDrawable(80, "#333333"));
                break;
            }
        }
    }
}
