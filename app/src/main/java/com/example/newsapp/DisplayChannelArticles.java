package com.example.newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class DisplayChannelArticles extends AppCompatActivity {

    private String NEWS_API_KEY = "2911bc7666d14ce5acd5d28509600d84";

    private RequestQueue mRequestQueue;

    private StringRequest stringRequest;

    private String channel_id = "";

    private String gNewsUrl = "";

    private int ARTICLE_COUNT = 0;

    private int ARTICLE_LENGTH = 0;

    Vector<String> articleList = new Vector<String>(10);
    Vector<String> imageList = new Vector<String>(10);
    Vector<String> titleList = new Vector<String>(10);
    Vector<String> urlList = new Vector<String>(10);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_channel_articles);

        Intent intent = getIntent();
        channel_id = intent.getStringExtra(MainActivity.CHANNEL_MESSAGE);
        gNewsUrl = "https://newsapi.org/v2/top-headlines?sources="+channel_id+"&apiKey="+NEWS_API_KEY;

        loadNewsArticles();
    }

    protected void fillImage(String imgUrl){
        ImageView image = findViewById(R.id.imageView_channel);
        Picasso.with(this).load(imgUrl).into(image);
        System.out.println("IMAGE FILLED");
    }

    protected void fillTitle(String title , String url){
        TextView textView = findViewById(R.id.title);
        System.out.println("TITLE FILLED");

        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='"+url+"'>"+title+"</a>";
        textView.setText(Html.fromHtml(text));
    }

    public void fillArticleBody(String heading){
        System.out.println("FILLING BODY");
        TextView textView = findViewById(R.id.textView_channel);
        textView.setMovementMethod(new ScrollingMovementMethod());
        System.out.println("BODY OK");
        textView.setText(heading);
        System.out.println("BODY FILLED");


    }

    public void nextArticle(View view){
//        if(ARTICLE_COUNT+1 < ARTICLE_LENGTH)
        ARTICLE_COUNT++;
        if(ARTICLE_COUNT >= ARTICLE_LENGTH){
            ARTICLE_COUNT = 0;
            loadNewsArticles();
        }
        fillArticle(ARTICLE_COUNT);
    }

    public void prevArticle(View view){
        if(ARTICLE_COUNT-1 >= 0)
            ARTICLE_COUNT--;
        fillArticle(ARTICLE_COUNT);
    }

    public void nextPage(View view){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void loadNewsArticles() {

        Log.d("myTag", "This is my message");

        System.out.println(gNewsUrl);


        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, gNewsUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response){

                JSONObject resObj = null;
                try {
                    resObj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray array = null;
                try {
                    array = resObj.getJSONArray("articles");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("myTag", "This is my message!");
                ARTICLE_LENGTH = array.length();
                System.out.println("ARTICLE LENGHT : " + ARTICLE_LENGTH);
                for(int i = 0 ; i < array.length() ; i++){
                    try {
                        if(array.getJSONObject(i).getString("description").toString() == "null"){
                            articleList.add("Tap on the link to read more");
                        }
                        else{
                        articleList.add(array.getJSONObject(i).getString("description"));
                        }
                        if(array.getJSONObject(i).getString("urlToImage").length() == 0 || array.getJSONObject(i).getString("urlToImage") == "null"){
                            System.out.println("*** PATH IS EMPTY ***");
                            imageList.add("https://upload.wikimedia.org/wikipedia/en/f/f9/No-image-available.jpg");
                        }
                        else{
                            imageList.add(array.getJSONObject(i).getString("urlToImage"));
                        }
                        titleList.add(array.getJSONObject(i).getString("title"));
                        urlList.add(array.getJSONObject(i).getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                fillArticle(0);

                System.out.println("FILL_ARTICLE");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String message = "Error is making a request";

                TextView textView = findViewById(R.id.textView_channel);

                textView.setText(message);

            }
        });

        mRequestQueue.add(stringRequest);

        for(int i=0; i<articleList.size(); i++){
            System.out.println("HELLOWORLD");
            System.out.println(articleList.get(i));
        }

        System.out.println("HELLOWORLD");
    }

    private void fillArticle(int article_count){
        fillArticleBody(articleList.get(article_count).toString());
        fillTitle(titleList.get(article_count).toString() , urlList.get(article_count).toString());
        fillImage(imageList.get(article_count));
    }



}
