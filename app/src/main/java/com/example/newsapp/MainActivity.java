package com.example.newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_MESSAGE = "com.example.myfirstapp.CHANNEL";
    public static final String TOPIC_MESSAGE = "com.example.myfirstapp.TOPIC";

    String imgUrl_abcNews = "https://icon-locator.herokuapp.com/icon?url=http://abcnews.go.com&size=70..120..200";
    String imgUrl_bbcNews = "https://icon-locator.herokuapp.com/icon?url=http://www.bbc.co.uk/news&size=70..120..200";
    String imgUrl_bloom = "https://icon-locator.herokuapp.com/icon?url=http://www.bloomberg.com&size=70..120..200";
    String imgUrl_cnn = "https://icon-locator.herokuapp.com/icon?url=http://us.cnn.com&size=70..120..200";
    String imgUrl_hacker = "https://icon-locator.herokuapp.com/icon?url=https://news.ycombinator.com&size=70..120..200";
    String imgUrl_techCrunch = "https://icon-locator.herokuapp.com/icon?url=https://techcrunch.com&size=70..120..200";
    String imgUrl_verge = "https://icon-locator.herokuapp.com/icon?url=http://www.theverge.com&size=70..120..200";
    String imgUrl_hindu = "https://icon-locator.herokuapp.com/icon?url=http://www.thehindu.com&size=70..120..200";
    String imgUrl_timesOfIndia = "https://icon-locator.herokuapp.com/icon?url=http://timesofindia.indiatimes.com&size=70..120..200";


    Map<String,String> channel_map = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        channel_map.put("2131165261","bloomberg");
        channel_map.put("2131165262","cnn");
        channel_map.put("2131165258","abc-news");
        channel_map.put("2131165263","hacker-news");
        channel_map.put("2131165264","techcrunch");
        channel_map.put("2131165265","the-verge");
        channel_map.put("2131165259","the-hindu");
        channel_map.put("2131165260","bbc-news");

        ImageView image1 = findViewById(R.id.imageView);
        ImageView image2 = findViewById(R.id.imageView2);
        ImageView image3 = findViewById(R.id.imageView3);
        ImageView image4 = findViewById(R.id.imageView4);
        ImageView image5 = findViewById(R.id.imageView7);
        ImageView image6 = findViewById(R.id.imageView8);
        ImageView image7 = findViewById(R.id.imageView9);
        ImageView image8 = findViewById(R.id.imageView10);

        Picasso.with(this).load(imgUrl_abcNews).into(image1);
        Picasso.with(this).load(imgUrl_bbcNews).into(image2);
        Picasso.with(this).load(imgUrl_bloom).into(image3);
        Picasso.with(this).load(imgUrl_cnn).into(image4);
        Picasso.with(this).load(imgUrl_hacker).into(image5);
        Picasso.with(this).load(imgUrl_techCrunch).into(image6);
        Picasso.with(this).load(imgUrl_verge).into(image7);
        Picasso.with(this).load(imgUrl_hindu).into(image8);
    }

    public void getNewsFromChannel(View view){
        Intent channelIntent = new Intent(this, DisplayChannelArticles.class);
        String message = channel_map.get(Integer.toString(view.getId()));
        channelIntent.putExtra(CHANNEL_MESSAGE, message);
        startActivity(channelIntent);
        System.out.println(view.getId());
    }

    public void getNewsFromTopic(View view) {
        Intent topicIntent = new Intent(this, TopicSearchActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        topicIntent.putExtra(TOPIC_MESSAGE, message);
        startActivity(topicIntent);
    }
}
