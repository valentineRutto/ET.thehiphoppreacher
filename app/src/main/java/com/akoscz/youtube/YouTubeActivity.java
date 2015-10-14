package com.akoscz.youtube;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

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
public class YouTubeActivity extends ActionBarActivity {


    private static final String YOUTUBE_PLAYLIST = "PLty8xV3EJYSe5Jqym-_rZtsFvNzXi9AB7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.akoscz.youtube.R.layout.youtube_activity);

        if (ApiKey.YOUTUBE_API_KEY.startsWith("YOUR_API_KEY")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Edit ApiKey.java and replace \"YOUR_API_KEY\" with your Applications Browser API Key")
                        .setTitle("Missing API Key")
                        .setNeutralButton("Ok, I got it!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(com.akoscz.youtube.R.id.container, YouTubeRecyclerViewFragment.newInstance(YOUTUBE_PLAYLIST))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.akoscz.youtube.R.menu.you_tube, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
