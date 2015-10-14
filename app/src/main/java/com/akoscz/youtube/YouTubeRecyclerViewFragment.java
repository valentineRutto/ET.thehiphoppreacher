package com.akoscz.youtube;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akoscz.youtube.model.Playlist;
import com.akoscz.youtube.model.PlaylistItem;
import com.akoscz.youtube.model.Video;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YouTubeRecyclerViewFragment extends Fragment {
    // the fragment initialization parameter
    private static final String ARG_YOUTUBE_PLAYLIST_ID = "YOUTUBE_PLAYLIST_ID";
    // key used in the saved instance bundle to persist the playlist
    private static final String KEY_SAVED_INSTANCE_PLAYLIST = "SAVED_INSTANCE_PLAYLIST";

    private String mPlaylistId;
    private RecyclerView mRecyclerView;
    private Playlist mPlaylist;
    private RecyclerView.LayoutManager mLayoutManager;
    private PlaylistCardAdapter mAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param playlistId The YouTube Playlist ID parameter string
     * @return A new instance of fragment YouTubeRecyclerViewFragment.
     */
    public static YouTubeRecyclerViewFragment newInstance(String playlistId) {
        YouTubeRecyclerViewFragment fragment = new YouTubeRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_YOUTUBE_PLAYLIST_ID, playlistId);
        fragment.setArguments(args);
        return fragment;
    }

    public YouTubeRecyclerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlaylistId = getArguments().getString(ARG_YOUTUBE_PLAYLIST_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String json = new Gson().toJson(mPlaylist);
        outState.putString(KEY_SAVED_INSTANCE_PLAYLIST, json);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // set the Picasso debug indicator only for debug builds
        Picasso.with(getActivity()).setIndicatorsEnabled(BuildConfig.DEBUG);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(com.akoscz.youtube.R.layout.youtube_recycler_view_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(com.akoscz.youtube.R.id.youtube_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        Resources resources = getResources();
        if (resources.getBoolean(com.akoscz.youtube.R.bool.isTablet)) {
            // use a staggered grid layout if we're on a large screen device
            mLayoutManager = new StaggeredGridLayoutManager(resources.getInteger(com.akoscz.youtube.R.integer.columns),
                    StaggeredGridLayoutManager.VERTICAL);
        } else {
            // use a linear layout on phone devices
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        mRecyclerView.setLayoutManager(mLayoutManager);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // restore the playlist after an orientation change
        if (savedInstanceState != null) {
            mPlaylist = new Gson().fromJson(savedInstanceState.getString(KEY_SAVED_INSTANCE_PLAYLIST),
                    Playlist.class);
        }

        // if we have a saved playlist, ensure the adapter is initialized
        if (mPlaylist != null) {
            initAdapter(mPlaylist);
        } else {
            // otherwise start loading the first page of our playlist
            new GetYouTubePlaylistAsyncTask() {
                @Override
                public void onPostExecute(JSONObject result) {
                    if (result == null) return;
                    handlePlaylistResult(result);
                }
            }.execute(mPlaylistId, null);
        }
    }

    private void initAdapter(final Playlist mPlaylist) {
        // create the adapter with our playlist and a callback to handle when we reached the last item
        mAdapter = new PlaylistCardAdapter(mPlaylist, new LastItemReachedListener() {
            @Override
            public void onLastItem(int position, String nextPageToken) {
                new GetYouTubePlaylistAsyncTask() {
                    @Override
                    public void onPostExecute(JSONObject result) {
                        handlePlaylistResult(result);
                    }
                }.execute(mPlaylistId, nextPageToken);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void handlePlaylistResult(JSONObject result) {
        try {
            if (result == null) {
                return;
            }

            if (mPlaylist == null) {
                mPlaylist = new Playlist(result);
                initAdapter(mPlaylist);
            }

            final Playlist.Page page = mPlaylist.addPage(result);
            final int itemsPerPage = page.items.size();
            final int pageNumberBase = page.pageNumber * itemsPerPage;

            // fetch all the video details for the current page of Playlist Items
            new GetYouTubeVideoAsyncTask() {

                @Override
                public void onPostExecute(JSONObject result) {
                    if (result == null) {
                        return;
                    }

                    try {
                        JSONArray resultItems = result.getJSONArray("items");
                        PlaylistItem playlistItem;
                        for (int i = 0; i < itemsPerPage; i++) {
                            playlistItem = page.items.get(i);
                            playlistItem.video = new Video(resultItems.getJSONObject(i));
                            // make sure the UI gets updated for the item
                            mAdapter.notifyItemChanged(pageNumberBase + i);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(page);

            mAdapter.notifyItemRangeInserted(pageNumberBase, pageNumberBase + itemsPerPage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Interface used by the {@link PlaylistCardAdapter} to inform us that we reached the last item in the list.
     */
    public interface LastItemReachedListener {
        void onLastItem(int position, String nextPageToken);
    }
}
