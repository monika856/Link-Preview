package com.example.monikasaini.preview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.monikasaini.preview.adapter.PreviewAdapter;
import com.example.monikasaini.preview.events.EventDataFetched;
import com.example.monikasaini.preview.models.PageData;
import com.example.monikasaini.preview.service.GetDataService;
import com.example.monikasaini.preview.utility.PreviewUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREF = "shared_pref";
    public static final String KEY_DATA = "key_data";
    public SharedPreferences sharedPref;
    RecyclerView recyclerView;
    PreviewAdapter previewAdapter;
    ArrayList<PageData> dataList = new ArrayList<>();
    ArrayList<String> urlList = new ArrayList<>();
    Loader loader;
    Gson gson = new Gson();
    private EditText editText;
    private Button submitButton, randomButton;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.input);
        submitButton = (Button) findViewById(R.id.action_go);
        randomButton = (Button) findViewById(R.id.random);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        previewAdapter = new PreviewAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(previewAdapter);
        addDefaultUrls();
        /**
         * this is done to get url from different application by sharing
         */
        if (getIntent().getExtras() != null) {
            String shareVia = (String) getIntent().getExtras().get(Intent.EXTRA_TEXT);
            if (shareVia != null) {
                editText.setText(shareVia);
            }
        }
        if (getIntent().getAction() == Intent.ACTION_VIEW) {
            Uri data = getIntent().getData();
            String scheme = data.getScheme();
            String host = data.getHost();
            List<String> params = data.getPathSegments();
            String builded = scheme + "://" + host + "/";

            for (String string : params) {
                builded += string + "/";
            }

            if (data.getQuery() != null && !data.getQuery().equals("")) {
                builded = builded.substring(0, builded.length() - 1);
                builded += "?" + data.getQuery();
            }

            editText.setText(builded);

        }

        sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String json = sharedPref.getString(KEY_DATA, null);
        editor = sharedPref.edit();

        dataList = gson.fromJson(json, new TypeToken<List<PageData>>() {
        }.getType());
        if (dataList == null)
            dataList = new ArrayList<>();

        if (dataList != null && dataList.size() > 0) {
            previewAdapter.notifyDataSetChanged();
            previewAdapter.setDataList(dataList);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText() != null && URLUtil.isValidUrl(editText.getText().toString())) {
                    loader = PreviewUtility.getLoaderOfType(MainActivity.this, Loader.PROGRESS_DIALOG, "", "Please wait");
                    loader.showLoader();
                    Intent intent = new Intent(MainActivity.this, GetDataService.class);
                    intent.putExtra("url", editText.getText().toString());
                    startService(intent);
                } else
                    Toast.makeText(MainActivity.this, "Please enter a valid url", Toast.LENGTH_LONG).show();


            }
        });
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlList != null && urlList.size() > 0) {
                    loader = PreviewUtility.getLoaderOfType(MainActivity.this, Loader.PROGRESS_DIALOG, "", "Please wait");
                    loader.showLoader();
                    Intent intent = new Intent(MainActivity.this, GetDataService.class);
                    intent.putExtra("url", urlList.get(0));
                    startService(intent);
                    urlList.remove(0);
                } else
                    addDefaultUrls();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventDataFetched(EventDataFetched event) {
        PageData data = event.getData();
        if (data.isSuccess()) {
            dataList.add(data);
            previewAdapter.setDataList(dataList);
            previewAdapter.notifyDataSetChanged();
        }
        editor.putString(KEY_DATA, gson.toJson(dataList));
        editor.commit();
        loader.dismissLoader(null);

    }

    void addDefaultUrls() {
        urlList.add("https://blog.google/products/android/2bn-milestone/");
        urlList.add("http://www.androidauthority.com/google-photos-one-billion-installs-777740/");
        urlList.add("http://www.androidauthority.com/best-android-phones-568001/");

    }


}
