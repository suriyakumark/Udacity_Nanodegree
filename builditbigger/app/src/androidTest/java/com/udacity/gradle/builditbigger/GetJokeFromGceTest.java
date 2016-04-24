package com.udacity.gradle.builditbigger;

import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.text.TextUtils;
import android.content.Context;
import android.util.Log;

import java.lang.String;
import java.util.concurrent.CountDownLatch;
import com.simplesolutions2003.builditbigger.GetJokeFromGce;

public class GetJokeFromGceTest extends InstrumentationTestCase {
    Context context;
    String mJoke = null;
    String mError = null;
    CountDownLatch signal = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new MockContext();
        assertNotNull(context);
        signal = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        signal.countDown();
    }

    public void testGetJokeFromGce() throws InterruptedException {

        GetJokeFromGce task = new GetJokeFromGce();
        task.testMode = true;
        task.setListener(new GetJokeFromGce.GetJokeFromGceListener() {
            @Override
            public void onComplete(String joke, String err) {
                mJoke = joke;
                mError = err;
                signal.countDown();
            }
        }).execute(context);
        signal.await();


        if(mError != null){
            Log.v("GetJokeFromGceTest - ",mError);
        }
        if(mJoke != null){
            Log.v("GetJokeFromGceTest - ",mJoke);
        }

        //check if error is null. if its not null, we have an error message.
        //fail the test if error is not null.
        //we are passing the error message instead of joke when we get error.
        //so, we need to assert the two fields is may not be empty.

        assertNull(mError);
        assertFalse(TextUtils.isEmpty(mJoke));


    }
}
