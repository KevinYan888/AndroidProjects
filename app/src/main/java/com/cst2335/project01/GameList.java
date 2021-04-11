package com.cst2335.project01;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 */
public class GameList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private MyListAdapter myAdapter;
    private String title;
    private String gameDate;
    private String gameDescription;
    private TextView detailTV;
    private String imageUrl;
    private String articleUrl;
    static  boolean isShowDialog = false;
    /**
     * The Soccer details list.
     */
    List<SoccerDetails> soccerDetailsList = new ArrayList<>();
    private ProgressBar pb;

    /**
     *this method is used to  accomplish the function of all buttons, and to determine whether to use fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isShowDialog == false){ ShowDialog();}
        setContentView(R.layout.activity_game_list);
        boolean isTablet = findViewById(R.id.soc_fragmentLocation) != null;
        if(!isTablet){
            Toolbar tBar = findViewById(R.id.soc_list_toolbar);
            setSupportActionBar(tBar);
            DrawerLayout drawer = findViewById(R.id.list_drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                    drawer, tBar, R.string.open, R.string.close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = findViewById(R.id.soc_list_nav);
            navigationView.setItemIconTintList(null); //
            navigationView.setNavigationItemSelectedListener(this);
        }
        Button btn = findViewById(R.id.showBtn);
        pb = findViewById(R.id.pb1);
        pb.setVisibility(View.VISIBLE);
        String url = "https://www.goal.com/en/feeds/news?fmt=rss";
        GameListHttpRequest myRequest = new GameListHttpRequest();
        myRequest.execute(url);
        btn.setOnClickListener(b ->{
            Intent goToDe = new Intent(GameList.this,Favorite_Game_List.class);
            startActivity(goToDe);
        });

        ListView myList = findViewById(R.id.gameList);
        myList.setAdapter( myAdapter = new MyListAdapter());

        myList.setOnItemClickListener(((parent, view, position, id) -> {
            Bundle dataToPass = new Bundle();

            String gtitle = soccerDetailsList.get(position).title;
            String gdate = soccerDetailsList.get(position).date;
            String gurl = soccerDetailsList.get(position).newsUrl;
            String iurl = soccerDetailsList.get(position).imgUrl;
            String gdes = soccerDetailsList.get(position).newsdes;

            String source = "listPage";
            dataToPass.putString("gametitle", gtitle);
            dataToPass.putString("gamedate", gdate);
            dataToPass.putString("gameurl", gurl);
            dataToPass.putString("imageurl", iurl);
            dataToPass.putString("newsdescription",gdes);
            dataToPass.putString("sourcePage",source);

            if (isTablet) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.soccer_alert_title) + gtitle).setMessage(R.string.soccer_alert_msg
                ).setPositiveButton(R.string.soccer_postive, (click, arg) -> {
                    SoccerDetailsFragment dFragment = new SoccerDetailsFragment(); //add a DetailFragment
                    dFragment.setArguments(dataToPass); //pass it a bundle for information
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.soc_fragmentLocation, dFragment) //Add the fragment in FrameLayout
                            .commit();
                    Toast.makeText(this, R.string.soccer_toast_txt, Toast.LENGTH_SHORT).show();
                }).setNegativeButton(R.string.soccer_negative, (click, arg) -> {
                    Snackbar.make(btn, R.string.soccer_snackbar_msg, Snackbar.LENGTH_SHORT).show();
                }).create().show();

            } else {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.soccer_alert_title) + gtitle).setMessage(R.string.soccer_alert_msg
                ).setPositiveButton(R.string.soccer_postive, (click, arg) -> {
                    Intent nextActivity = new Intent(this, GameDetailActivity.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivity(nextActivity); //make the transition
                    Toast.makeText(this, R.string.soccer_toast_txt, Toast.LENGTH_SHORT).show();
                }).setNegativeButton(R.string.soccer_negative, (click, arg) -> {
                    Snackbar.make(btn, R.string.soccer_snackbar_msg, Snackbar.LENGTH_SHORT).show();
                }).create().show();
            }
        }));
    }




    /**
     * this method is used to inflate tool bar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * this method is used to finish different function when user
     * selected different menu on toolbar
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                Intent goToCar = new Intent(GameList.this, CarActivity.class);
                startActivity(goToCar);
                break;

            case R.id.item2:
                Intent goToTrival = new Intent(GameList.this, TriviaActivity.class);
                startActivity(goToTrival);
                break;
            case R.id.item3:
                Intent goToSong = new Intent(GameList.this, SongActivity.class);
                startActivity(goToSong);
                break;
            case R.id.item4:
                Toast.makeText(this, R.string.soc_tbar_msg, Toast.LENGTH_LONG).show();
                break;
//            case R.id.item5:
//                Intent gotofav = new Intent(GameList.this, Favorite_Game_List.class);
//                startActivity(gotofav);
//                break;
        }

        return true;
    }

    /**
     * this method is used to finish different function when user
     * selected different menu on navigation drawer
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {


        switch(item.getItemId())
        {
            case R.id.api:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData( Uri.parse("https://www.goal.com/en/feeds/news?fmt=rss") );
                startActivity(i);
                break;
            case R.id.instruction:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(R.string.soc_instruction_title).setMessage(R.string.soc_intro_msg
                ).setPositiveButton(R.string.soc_intro_positive, (click, arg) -> {})
                        .create().show();
                break;
            case R.id.rate:
                final EditText et = new EditText(this);
                et.setHint("...");

                new AlertDialog.Builder(this).setTitle(R.string.rate_alert_msg).setMessage(R.string.rate_msg)
                        .setView(et)
                        .setPositiveButton("Thank you", (click,arg) ->{

                        })
                        .setNegativeButton("cancel", null)
                        .show();
                break;
            case R.id.fav:
                Intent gotofav = new Intent(GameList.this, Favorite_Game_List.class);
                startActivity(gotofav);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.list_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    /**
     * class SoccerDetials
     * this class is used to store details of soccer game
     */

    private class SoccerDetails {
        /**
         * The Title.
         */
        String title;
        /**
         * The Date.
         */
        String date;
        /**
         * The news article URL.
         */
        String newsUrl;
        /**
         * The Img url.
         */
        String imgUrl;
        /**
         * The news description.
         */
        String newsdes;

        /**
         * Instantiates a new Soccer details.
         *
         * @param title    the title
         * @param date     the date
         * @param newsUrl the news article URL
         * @param imgUrl   the img url
         */
        public SoccerDetails(String title, String date, String newsUrl,String imgUrl,String newsdes){
            this.date = date;
            this.title = title;
            this.newsUrl = newsUrl;
            this.imgUrl = imgUrl;
            this.newsdes = newsdes;
        }

    }

    /**
     *class GameListHttpRequest
     * this class is used to read and access the url and get the data back
     */

    private class GameListHttpRequest extends AsyncTask< String, Integer, String> {
        /**
         * Read a JsonArray and get the data back
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                //create a URL object of what server to contact:
                URL url = new URL(strings[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");
                int eventType = xpp.getEventType();
                while(eventType != XmlPullParser.END_DOCUMENT){

                    switch(eventType){
                        case XmlPullParser.START_TAG:

                        if(xpp.getName().equals("item")) {
                        }
                       else if(xpp.getName().equals("title")){
                            Log.e("gametitle", "no");
                            title = xpp.nextText();
                            Log.e("gametitle", title);
                            publishProgress(50);

                        }else if(xpp.getName().equals("pubDate")){
                            gameDate = xpp.nextText();
                            publishProgress(70);
                        }else if(xpp.getName().equals("media:thumbnail")) {
                            imageUrl = xpp.getAttributeValue(null, "url");
                        } else if(xpp.getName().equals("description")){
                            gameDescription = xpp.nextText();
                        }else if(xpp.getName().equals("link")){
                            articleUrl = xpp.nextText();
                        }
                       break;
                        case XmlPullParser.END_TAG:
                            if(xpp.getName().equals("item")){
                                soccerDetailsList.add(new SoccerDetails(title,gameDate,articleUrl,imageUrl, gameDescription));
                    }break;
                }eventType = xpp.next();}}
                catch (XmlPullParserException xmlPullParserException) {
                xmlPullParserException.printStackTrace();
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return "Done";
        }

        /**
         * set the progress bar visible
         * @param value
         */

        @Override
        public void onProgressUpdate(Integer...value){
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(value[0]);

        }

        /**
         * set progress bar invisible and notify the listview data changed after http request executed
         * @param fromDoInBackground
         */
        @Override
        public void onPostExecute(String fromDoInBackground)
        {
            pb.setVisibility(View.INVISIBLE);
            myAdapter.notifyDataSetChanged();

        }
    }

    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return soccerDetailsList.size();}
        public SoccerDetails getItem(int position) { return  soccerDetailsList.get(position); }

        public long getItemId(int position) { return position ; }

        /**
         * set the text of each listview based on the title in soccerDetailsList
         * @param position
         * @param old
         * @param parent
         * @return
         */

        @Override
        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate(R.layout.detail_layout, parent, false);
            //set what the text should be for this row:
            detailTV = newView.findViewById(R.id.detailTV);
            detailTV.setText( getItem(position).title );
           // Log.e("listview", getItem(position).title);
            //return it to be put in the table
            return newView;
        }
    }
    public void ShowDialog()
    {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);

        LinearLayout linearLayout = new LinearLayout(this);
        final RatingBar ratingBar = new RatingBar(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ratingBar.setLayoutParams(lp);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);

        //add ratingBar to linearLayout
        linearLayout.addView(ratingBar);


        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        float rating = pref.getFloat("numStars", 0f);
        ratingBar.setRating(rating);
        popDialog.setTitle(R.string.add_Rating);

        //add linearLayout to dailog
        popDialog.setView(linearLayout);



        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                System.out.println("Rated val:"+v);
            }
        });



        // Button OK
        popDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //textView.setText(String.valueOf(rating.getProgress()));
                        Float value = ratingBar.getRating();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putFloat("numStars", value);//put value
                        editor.commit();

                        float rating = pref.getFloat("numStars", 0f);
                        ratingBar.setRating(rating);//save
                        dialog.dismiss();
                    }

                })

                // Button Cancel
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        isShowDialog = true;
        popDialog.create();
        popDialog.show();

    }


}

