package com.phonegap.calendar.app;


import com.phonegap.DroidGap;

import android.os.Bundle;

public class ApplicationActivity extends DroidGap
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
}