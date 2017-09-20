package com.tabian.tabfragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Model.List;
import Model.MyPojo;
import Model.Weather;


public class Tab1Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    private TextView tv_city;
    private TextView tv_temp;
    private TextView tv_weather;
    private ImageView img_icon_weather;
    private String cityName;

    SwipeRefreshLayout mSwipeRefreshLayout;

    MyPojo myPojo;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_weather = (TextView) view.findViewById(R.id.tv_weather);
        tv_temp = (TextView) view.findViewById(R.id.tv_temp);
        img_icon_weather = (ImageView) view.findViewById(R.id.img_icon_weather);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.pink,R.color.indigo,R.color.lime);

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("Tokyo");
        return view;
    }

    public AsyncTask<String, Void, MyPojo> city()
    {
        return new MyAsyncTask().execute(getCityName());
    }
    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    @Override
    public void onRefresh()
    {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                city();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 4000);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, MyPojo>
    {
        @Override
        protected void onPostExecute(MyPojo myPojo)
        {
            super.onPostExecute(myPojo);
            for(List l : myPojo.getList())
            {
                tv_temp.setText("" + Math.round((Double.parseDouble(l.getMain().getTemp()) - 273.15)) + " \u2103");
                for (Weather w : l.getWeather())
                {
                    tv_weather.setText(w.getDescription());
                    Picasso.with(getContext()).load("http://openweathermap.org/img/w/"+w.getIcon()+".png").placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher).into(img_icon_weather);
                }
            }
            tv_city.setText(myPojo.getCity().getName());
        }

        @Override
        protected MyPojo doInBackground(String... param)
        {
            String str = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL("http://api.openweathermap.org/data/2.5/forecast?q="+param[0]+"&appid=a0b864a6d7a59953f1fc87e4b7faa1a7");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                int data = isr.read();
                while(data != -1)
                {
                    char current = (char) data;
                    data = isr.read();
                    str += current;
                }
                Log.e("My JSON String",str);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                myPojo = gson.fromJson(str,MyPojo.class);
                Log.e("Response",myPojo.toString());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
            }
            return myPojo;
        }
    }
}
