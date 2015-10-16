package com.valentine.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.akoscz.youtube.R;
import com.github.clans.fab.FloatingActionButton;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

public class Twitter extends ActionBarActivity {
    String SEARCH_QUERY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        SEARCH_QUERY = getResources().getString(R.string.twitter_search);
        setUpTimeline();

//        ListView listView = (ListView) findViewById(android.R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Twitter.this, YouTubeActivity.class));
            }
        });

    }
    private void setUpTimeline() {

        SearchTimeline searchTimeline = new SearchTimeline.Builder().query(SEARCH_QUERY).build();

        final TweetTimelineListAdapter timelineAdapter = new TweetTimelineListAdapter(this, searchTimeline);

        ListView timelineView = (ListView) findViewById(android.R.id.list);
        timelineView.setEmptyView(findViewById(R.id.empty_timeline));
        timelineView.setAdapter(timelineAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
