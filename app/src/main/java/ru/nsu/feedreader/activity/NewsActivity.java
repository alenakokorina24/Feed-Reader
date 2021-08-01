package ru.nsu.feedreader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import ru.nsu.feedreader.R;

public class NewsActivity extends AppCompatActivity {

    private TextView title, description;
    private ImageView image;
    String mTitle, mDescription, mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        this.title = findViewById(R.id.article_title);
        this.description = findViewById(R.id.desc);
        this.image = findViewById(R.id.full_image);

        Intent intent = getIntent();
        mImage = intent.getStringExtra("image");
        mTitle = intent.getStringExtra("title");
        mDescription = intent.getStringExtra("desc");

        RequestOptions requestOptions = new RequestOptions();

        Glide.with(this)
                .load(mImage)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);

        title.setText(mTitle);
        description.setText(mDescription);
    }
}
