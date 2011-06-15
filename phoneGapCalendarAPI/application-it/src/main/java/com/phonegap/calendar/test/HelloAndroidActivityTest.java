package com.phonegap.calendar.test;

import android.test.ActivityInstrumentationTestCase2;

import com.phonegap.calendar.app.ApplicationActivity;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<ApplicationActivity> {

    public HelloAndroidActivityTest() {
        super(ApplicationActivity.class);
    }

    public void testActivity() {
    	ApplicationActivity activity = getActivity();
        assertNotNull(activity);
    }
}

