/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sunshine.wearable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.example.android.sunshine.R;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Digital watch face with seconds. In ambient mode, the seconds aren't displayed. On devices with
 * low-bit ambient mode, the text is drawn without anti-aliasing in ambient mode.
 */
public class SunshineWatchFace extends CanvasWatchFaceService {
    private static final Typeface NORMAL_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    /**
     * Update rate in milliseconds for interactive mode. We update once a second since seconds are
     * displayed in interactive mode.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;
    private static final int WEATHER_ALLOWED_INTERVAL = 1800;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<SunshineWatchFace.Engine> mWeakReference;

        public EngineHandler(SunshineWatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            SunshineWatchFace.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        final Handler mUpdateTimeHandler = new EngineHandler(this);
        private static final String SUNSHINE_PREF = "SunshinePref";

        boolean mRegisteredTimeZoneReceiver = false;
        Paint mBackgroundPaint;
        Paint mBackgroundPaint2;

        Paint mTextTimePaint;
        Paint mTextDatePaint;
        Paint mTextWeatherHiPaint;
        Paint mTextWeatherLoPaint;
        Paint mLinePaint;

        boolean mAmbient;
        boolean mShowWeather = false;
        Time mTime;
        Date mDate;
        String mWeatherHi = new String("");
        String mWeatherLo = new String("");
        int mWeatherId = 0;
        Long mWeatherTS = 0L;
        Bitmap weatherImage;

        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };
        int mTapCount;

        float mXOffsetTime;
        float mYOffsetTime;

        float mXOffsetDate;
        float mYOffsetDate;

        float mYStartLine;
        float mXStartLine;
        float mYEndLine;
        float mXEndLine;

        float mXOffsetWeatherImage;
        float mXOffsetWeatherHi;
        float mXOffsetWeatherLo;
        float mYOffsetWeather;
        float mYOffsetWeatherImage;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(SunshineWatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());
            Resources resources = SunshineWatchFace.this.getResources();

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(resources.getColor(R.color.background));

            mBackgroundPaint2 = new Paint();
            mBackgroundPaint2.setColor(resources.getColor(R.color.background2));

            mTextTimePaint = new Paint();
            mTextTimePaint = createTextPaint(resources.getColor(R.color.text_main));

            mTextDatePaint = new Paint();
            mTextDatePaint = createTextPaint(resources.getColor(R.color.text_sub));

            mLinePaint = new Paint();
            mLinePaint.setColor(resources.getColor(R.color.line));

            mTextWeatherHiPaint = new Paint();
            mTextWeatherHiPaint = createTextPaint(resources.getColor(R.color.text_main));

            mTextWeatherLoPaint = new Paint();
            mTextWeatherLoPaint = createTextPaint(resources.getColor(R.color.text_sub));

            mTime = new Time();
            mDate = new Date(System.currentTimeMillis());

        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        private Paint createTextPaint(int textColor) {
            Paint paint = new Paint();
            paint.setColor(textColor);
            paint.setTypeface(NORMAL_TYPEFACE);
            paint.setAntiAlias(true);
            return paint;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();
                mDate.setTime(System.currentTimeMillis());
                fetchDataElements();
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            SunshineWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            SunshineWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            // Load resources that have alternate values for round watches.
            Resources resources = SunshineWatchFace.this.getResources();
            boolean isRound = insets.isRound();

            float textSizeBig = resources.getDimension(isRound
                    ? R.dimen.text_size_big_round : R.dimen.text_size_big);

            float textSizeMedium = resources.getDimension(isRound
                    ? R.dimen.text_size_medium_round : R.dimen.text_size_medium);

            float textSizeSmall = resources.getDimension(isRound
                    ? R.dimen.text_size_small_round : R.dimen.text_size_small);

            mTextTimePaint.setTextSize(textSizeBig);
            mTextDatePaint.setTextSize(textSizeSmall);
            mTextWeatherHiPaint.setTextSize(textSizeBig);
            mTextWeatherLoPaint.setTextSize(textSizeMedium);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;
                if (mLowBitAmbient) {
                    mTextTimePaint.setAntiAlias(!inAmbientMode);
                    mTextDatePaint.setAntiAlias(!inAmbientMode);
                    mLinePaint.setAntiAlias(!inAmbientMode);
                    mTextWeatherHiPaint.setAntiAlias(!inAmbientMode);
                    mTextWeatherLoPaint.setAntiAlias(!inAmbientMode);
                }
                invalidate();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            Resources resources = SunshineWatchFace.this.getResources();
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    // The user has started touching the screen.
                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    // The user has completed the tap gesture.
                    mTapCount++;
                    mBackgroundPaint.setColor(resources.getColor(mTapCount % 2 == 0 ?
                            R.color.background : R.color.background2));
                    break;
            }
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            // Draw the background.
            if (isInAmbientMode()) {
                canvas.drawColor(Color.BLACK);
            } else {
                canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);
            }

            // Draw H:MM in ambient mode or H:MM:SS in interactive mode.
            mTime.setToNow();
            String text = mAmbient
                    ? String.format("%d:%02d", mTime.hour, mTime.minute)
                    : String.format("%d:%02d:%02d", mTime.hour, mTime.minute, mTime.second);

            SimpleDateFormat mDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy");
            mDate.setTime(System.currentTimeMillis());
            String date = mDateFormat.format(mDate);

            long canvasWidth = canvas.getWidth();
            long canvasHeight = canvas.getHeight();
            long canvasHorizontalCenter = canvasWidth/2;
            long canvasHorizontalLeftQuarter = canvasWidth/4;
            long canvasHorizontalRightQuarter = canvasWidth*3/4;

            long canvasVerticalCenter = canvasHeight/2;
            long canvasVerticalUpperQuarter = canvasHeight/4;
            long canvasVerticalLowerQuarter = canvasHeight*3/4;

            mYOffsetTime = canvasVerticalUpperQuarter + 30.0f;
            mYOffsetDate = canvasVerticalUpperQuarter + 60.0f;
            mYStartLine = canvasVerticalCenter;
            mYEndLine = canvasVerticalCenter;
            mYOffsetWeather = canvasVerticalLowerQuarter;
            mYOffsetWeatherImage = canvasVerticalLowerQuarter;

            mXOffsetTime = bounds.centerX() - (mTextTimePaint.measureText(text))/2;
            mXOffsetDate = bounds.centerX() - (mTextDatePaint.measureText(date))/2;
            mXStartLine = canvasHorizontalLeftQuarter;
            mXEndLine = canvasHorizontalRightQuarter;
            mXOffsetWeatherHi = bounds.centerX() - (mTextWeatherHiPaint.measureText(mWeatherHi))/2;
            mXOffsetWeatherLo = mXOffsetWeatherHi + 80.0f;
            mXOffsetWeatherImage = mXOffsetWeatherHi - 70.0f;

            canvas.drawText(text, mXOffsetTime, mYOffsetTime, mTextTimePaint);
            canvas.drawText(date, mXOffsetDate, mYOffsetDate, mTextDatePaint);
            canvas.drawLine(mXStartLine, mYStartLine,mXEndLine,mYEndLine, mLinePaint);
            fetchDataElements();
            if(mShowWeather) {
                canvas.drawText(mWeatherHi, mXOffsetWeatherHi, mYOffsetWeather, mTextWeatherHiPaint);
                canvas.drawText(mWeatherLo, mXOffsetWeatherLo, mYOffsetWeather, mTextWeatherLoPaint);
                if(!isInAmbientMode()) {
                    mYOffsetWeatherImage = mYOffsetWeatherImage - (weatherImage.getHeight() * 0.90f);
                    canvas.drawBitmap(weatherImage,mXOffsetWeatherImage, mYOffsetWeatherImage, null);
                }
            }
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }

        private void fetchDataElements(){
            mShowWeather = false;
            SharedPreferences prefs = getSharedPreferences(SUNSHINE_PREF, MODE_PRIVATE);
            mWeatherHi = prefs.getString("weatherHi", "");
            mWeatherLo = prefs.getString("weatherLo", "");
            mWeatherId = prefs.getInt("weatherId", 0);
            mWeatherTS = prefs.getLong("weatherTS",0L);
            long timeDiff = (System.currentTimeMillis() - mWeatherTS)/1000;
            if(!mWeatherHi.equals("") && !mWeatherLo.equals("") && mWeatherId > 0 && timeDiff >= 0 && timeDiff <= WEATHER_ALLOWED_INTERVAL){
                mShowWeather = true;
            }else{
                //consider as old data expired or data missing
                mShowWeather = false;
            }

            weatherImage = BitmapFactory.decodeResource(SunshineWatchFace.this.getResources(),getIconResourceForWeatherCondition(mWeatherId));
        }

        private int getIconResourceForWeatherCondition(int weatherId) {
            // Based on weather code data found at:
            // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
            if (weatherId >= 200 && weatherId <= 232) {
                return R.drawable.weather_storm;
            } else if (weatherId >= 300 && weatherId <= 321) {
                return R.drawable.weather_light_rain;
            } else if (weatherId >= 500 && weatherId <= 504) {
                return R.drawable.weather_rain;
            } else if (weatherId == 511) {
                return R.drawable.weather_snow;
            } else if (weatherId >= 520 && weatherId <= 531) {
                return R.drawable.weather_rain;
            } else if (weatherId >= 600 && weatherId <= 622) {
                return R.drawable.weather_snow;
            } else if (weatherId >= 701 && weatherId <= 761) {
                return R.drawable.weather_fog;
            } else if (weatherId == 761 || weatherId == 781) {
                return R.drawable.weather_storm;
            } else if (weatherId == 800) {
                return R.drawable.weather_clear;
            } else if (weatherId == 801) {
                return R.drawable.weather_light_clouds;
            } else if (weatherId >= 802 && weatherId <= 804) {
                return R.drawable.weather_cloudy;
            }
            return -1;
        }


    }


}
