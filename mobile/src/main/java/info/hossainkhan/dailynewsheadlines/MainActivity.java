package info.hossainkhan.dailynewsheadlines;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.ApiCore;

import info.hossainkhan.android.core.CoreConfig;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: " + ApiCore.TEST + CoreConfig.NAME);
    }
}