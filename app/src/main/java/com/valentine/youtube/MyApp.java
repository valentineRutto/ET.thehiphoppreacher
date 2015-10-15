package com.valentine.youtube;

import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by valentine on 10/14/15.
 */
public class MyApp extends android.app.Application {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "3PxKipJ0JoTDHaOLEFQ1IbTcm";
    private static final String TWITTER_SECRET = "hcB48sOQ9qPCMJ2cLpcYhq7G16wx93eDOqNjWfGEG0kMbCLq5B";

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new com.twitter.sdk.android.Twitter(authConfig));
    }
}
