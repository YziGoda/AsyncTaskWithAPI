package com.example.asynctaskwithapiexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asynctaskwithapiexample.utilities.ApiDataReader;
import com.example.asynctaskwithapiexample.utilities.AsyncDataLoader;
import com.example.asynctaskwithapiexample.utilities.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lvItems;
    public static EditText editText;
    public static TextView tvStatus;
    private ArrayAdapter listAdapter;
    private Switch swUseAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lvItems = findViewById(R.id.lv_items);
        this.tvStatus = findViewById(R.id.tv_status);
        this.swUseAsyncTask = findViewById(R.id.sw_use_async_task);
        this.editText = findViewById(R.id.etFilter);
        this.listAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        this.lvItems.setAdapter(this.listAdapter);
    }

    public void onBtnGetDataClick(View view)
    {
        this.tvStatus.setText(R.string.loading_data);
        if(this.swUseAsyncTask.isChecked())
        {
            getDataByAsyncTask();
            editText.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.msg_using_async_task, Toast.LENGTH_LONG).show();
        }
        else
        {
            getDataByThread();
            editText.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.msg_using_thread, Toast.LENGTH_LONG).show();
        }
    }

    public void getDataByAsyncTask()
    {
        new AsyncDataLoader()
        {
            @Override
            public void onPostExecute(String result)
            {
                tvStatus.setText(getString(R.string.data_loaded) + result);
            }
        }.execute(Constants.FLOATRATES_API_URL);
    }

    public void getDataByThread()
    {
        this.tvStatus.setText(R.string.loading_data);
        Runnable getDataAndDisplayRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final String result = ApiDataReader.getValuesFromApi(Constants.FLOATRATES_API_URL);
                    Runnable updateUIRunnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tvStatus.setText(getString(R.string.data_loaded) + result);
                        }
                    };
                    runOnUiThread(updateUIRunnable);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(getDataAndDisplayRunnable);
        thread.start();

    }
}
