
package com.valentine.DailyMotivation.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public class Playlist {
    public final int totalResults;
    public final int resultsPerPage;

    public List<Playlist.Page> pages;

    public Playlist(JSONObject jsonPlaylist) throws JSONException {
        pages = new ArrayList<Page>();

        JSONObject pageInfo = jsonPlaylist.getJSONObject("pageInfo");
        totalResults = pageInfo.getInt("totalResults");
        resultsPerPage = pageInfo.getInt("resultsPerPage");
    }

    public int getCount() {
        int count = 0;
        for (Page page : pages) {
            count += page.items.size();
        }

        return count;
    }

    public Page addPage(JSONObject jsonPlaylist) throws JSONException {
        final Page page = new Page(
                pages.size(),
                jsonPlaylist.getJSONArray("items"),
                jsonPlaylist.getString("etag"),
                jsonPlaylist.optString("nextPageToken", null));

        pages.add(page);

        return page;
    }

    public PlaylistItem getItem(int position) {
        int pageNumber = position / resultsPerPage;
        Page page = pages.get(pageNumber);

        return page.items.get(position % resultsPerPage);
    }

    public String getNextPageToken(int position) {
        int pageNumber = position / resultsPerPage;
        Page page = pages.get(pageNumber);

        return page.nextPageToken;
    }

    public String getEtag(int position) {
        int pageNumber = position / resultsPerPage;
        Page page = pages.get(pageNumber);

        return page.eTag;
    }

    public class Page {
        public final String nextPageToken;
        public final List<PlaylistItem> items;
        public final String eTag;
        public final int pageNumber;

        Page(int number, JSONArray jsonItems, String etag, String nextPageToken) throws JSONException {
            pageNumber = number;
            eTag = etag;
            items = new ArrayList<PlaylistItem>(jsonItems.length());
            this.nextPageToken = nextPageToken;

            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject item = jsonItems.getJSONObject(i);
                items.add(new PlaylistItem(item));
            }
        }

        public String getVideoIds() {
            StringBuffer buffer = new StringBuffer();
            for (PlaylistItem item : items) {
                buffer.append(item.videoId);
                buffer.append(',');
            }
            buffer.deleteCharAt(buffer.lastIndexOf(","));

            return buffer.toString();
        }
    }
}
