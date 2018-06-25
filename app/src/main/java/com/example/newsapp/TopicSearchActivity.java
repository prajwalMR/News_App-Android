package com.example.newsapp;

import android.app.SearchManager;
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

public class TopicSearchActivity extends AppCompatActivity {

    private String NEWS_API_KEY = "2911bc7666d14ce5acd5d28509600d84";

    private RequestQueue mRequestQueue;

    private StringRequest stringRequest;

    private String url = "http://www.mocky.io/v2/5b29e9f430000066009cd071";

    private String topic = "";

    private String topicNewsUrl = "";

    private int ARTICLE_COUNT = 0;

    private int ARTICLE_LENGTH = 0;

    private int totalResults  = 0;

    Vector<String> articleList_topic = new Vector<String>(10);
    Vector<String> imageList_topic = new Vector<String>(10);
    Vector<String> titleList_topic = new Vector<String>(10);
    Vector<String> urlList_topic = new Vector<String>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_search);

        Intent intent = getIntent();
        topic = intent.getStringExtra(MainActivity.TOPIC_MESSAGE);
        String temp_topic = topic;
        String [] temp_topic_array = temp_topic.split(" ");
        temp_topic = "";
        for(int i =0 ; i<temp_topic_array.length; i++){
            temp_topic += temp_topic_array[i];
        }
        topicNewsUrl = "https://newsapi.org/v2/everything?q="+temp_topic+"&apiKey="+NEWS_API_KEY;

        loadNewsArticles();
    }

    protected void fillImage(String imgUrl){
        ImageView image = findViewById(R.id.imageView_topic);
        Picasso.with(this).load(imgUrl).into(image);
        System.out.println("IMAGE FILLED");
    }

    protected void fillTitle(String title , String url){
        TextView textView = findViewById(R.id.title_topic);
        System.out.println("TITLE FILLED");

        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='"+url+"'>"+title+"</a>";
        textView.setText(Html.fromHtml(text));
    }

    public void fillArticleBody(String body){
        System.out.println("FILLING BODY");
        TextView textView = findViewById(R.id.textView_topic);
        textView.setMovementMethod(new ScrollingMovementMethod());
//        TextView.setMovementMethod(new ScrollingMovementMethod());
        System.out.println("BODY OK");
        textView.setText(body);
        System.out.println(body);
        System.out.println("BODY FILLED");
    }

    public void nextArticle(View view){

        if(totalResults > 0){
            ARTICLE_COUNT++;
            System.out.println("ARTICLE_COUNT : " + ARTICLE_COUNT);
            if(ARTICLE_COUNT >= ARTICLE_LENGTH){
                ARTICLE_COUNT = 0;
            }
            fillArticle(ARTICLE_COUNT);
        }
    }

    public void prevArticle(View view){
        if(totalResults > 0){
            if(ARTICLE_COUNT-1 >= 0)
                ARTICLE_COUNT--;
            fillArticle(ARTICLE_COUNT);
        }
    }

    public void loadNewsArticles() {
        System.out.println(topicNewsUrl);
        mRequestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, topicNewsUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response){

                JSONObject resObj = null;
                try {
                    resObj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    totalResults = resObj.getInt("totalResults");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray array = null;
                try {
                    array = resObj.getJSONArray("articles");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ARTICLE_LENGTH = array.length();
                System.out.println("ARTICLE LENGHT : " + ARTICLE_LENGTH);
                for(int i = 0 ; i < array.length() ; i++){
                    try {
                        if(array.getJSONObject(i).getString("description").equals("null")){
                            articleList_topic.add("Tap on the link to read more");
                        }
                        else{
                            articleList_topic.add(array.getJSONObject(i).getString("description"));
                        }
                        System.out.println("URLtoImage : " + array.getJSONObject(i).getString("urlToImage") + " len : " + array.getJSONObject(i).getString("urlToImage").length());
                        if(array.getJSONObject(i).getString("urlToImage").length() == 0 || array.getJSONObject(i).getString("urlToImage").equals("null")){
                            System.out.println("*** PATH IS EMPTY ***");
                            imageList_topic.add("https://upload.wikimedia.org/wikipedia/en/f/f9/No-image-available.jpg");
                        }
                        else{
                        imageList_topic.add(array.getJSONObject(i).getString("urlToImage"));
                        }
                        titleList_topic.add(array.getJSONObject(i).getString("title"));
                        urlList_topic.add(array.getJSONObject(i).getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(totalResults > 0){
                    fillArticle(0);
                }

                else{
                    fillArticleBody("NO ARTICLES FOUND");
                }

                System.out.println("FILL_ARTICLE");

                TextView topic_heading = findViewById(R.id.textView_topic_heading);
                topic_heading.setText("TOPIC : " + topic + " (" + ARTICLE_LENGTH + " articles)");

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

        for(int i=0; i<articleList_topic.size(); i++){
            System.out.println(articleList_topic.get(i));
        }

    }

    private void fillArticle(int article_count){
        fillArticleBody(articleList_topic.get(article_count));
        fillTitle(titleList_topic.get(article_count) , urlList_topic.get(article_count));
        fillImage(imageList_topic.get(article_count));
    }

}
