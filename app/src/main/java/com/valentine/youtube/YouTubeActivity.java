package com.valentine.youtube;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.akoscz.youtube.R;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class YouTubeActivity extends AppCompatActivity {
 private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final String YOUTUBE_PLAYLIST = "PLty8xV3EJYSe5Jqym-_rZtsFvNzXi9AB7";

    private String[] drawerListViewItems;
    private ListView drawerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, YouTubeRecyclerViewFragment.newInstance(YOUTUBE_PLAYLIST))
                    .commit();

            FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "YouOweYou", Snackbar.LENGTH_LONG).show();
                }  });
        }
        // get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.items);

        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.drawer_list);

        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_item_layout,R.id.text1, drawerListViewItems));
        // App Icon
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        );
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
       actionBarDrawerToggle.syncState();


// Set actionBarDrawerToggle as the DrawerListener
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.nav_view) ;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                drawerLayout.closeDrawers();
                return false;
            }

        });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView parent, View view, int position, long id) {
//            Toast.makeText(YouTubeActivity.this, ((TextView) view).getText(), Toast.LENGTH_LONG).show();
//            drawerLayout.closeDrawer(drawerListView);
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
//        // then it has handled the app icon touch event
//        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,
//                R.string.app_name);
//
//        drawerLayout.setDrawerListener(mDrawerToggle);
//
//        mDrawerToggle.syncState();
//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.you_tube, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
//         automatically handle clicks on the Home/Up button, so long
//         as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == com.akoscz.youtube.R.id.action_listview) {
            getSupportFragmentManager().beginTransaction()
                    .replace(com.akoscz.youtube.R.id.container, YouTubeListViewFragment.newInstance(YOUTUBE_PLAYLIST))
                    .commit();
            return true;
        }else if (id == com.akoscz.youtube.R.id.action_recyclerview) {
            getSupportFragmentManager().beginTransaction()
                    .replace(com.akoscz.youtube.R.id.container, YouTubeRecyclerViewFragment.newInstance(YOUTUBE_PLAYLIST))
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
