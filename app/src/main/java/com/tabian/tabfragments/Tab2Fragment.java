package com.tabian.tabfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;

import Adapter.CityAdapter;
import OrmModel.Cities;
import OrmModel.DBOrmHelper;


public class Tab2Fragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener
{
    Button btn_accept;
    Button btn_favorites;

    EditText et_enter_city;
    Dao<Cities,Integer> cityDao = null;
    DBOrmHelper ormHelper = null;

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView rv;
    CityAdapter cityAdapter;
    Cities cities = new Cities();
    ArrayList<Cities> citiesArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);

        btn_accept = (Button) view.findViewById(R.id.btn_accept);
        btn_favorites = (Button) view.findViewById(R.id.btn_favorites);
        et_enter_city = (EditText) view.findViewById(R.id.et_enter_city);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.pink,R.color.indigo,R.color.lime);

        rv = (RecyclerView) view.findViewById(R.id.rv);

        btn_accept.setOnClickListener(this);
        btn_favorites.setOnClickListener(this);

        return view;
    }

    public interface OnSelectedButtonListener
    {
        void transitContent(String str);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_accept:

                String str_city = et_enter_city.getText().toString();
                OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
                listener.transitContent(str_city);
                Toast.makeText(getActivity(),"You can see the weather in "+str_city,Toast.LENGTH_LONG).show();
                clearEditText();
                break;
            case R.id.btn_favorites:

                connectionToDB();
                showRecycleViewContent();

                if (ormHelper!= null)
                {
                    cities = setCity(cities);
                    addFavorCity(cities);
                    Log.e("btnORM", "Connect accept");
                    cityAdapter.updateList(showCities());
                    clearEditText();
                }
                else
                {
                    Toast.makeText(getActivity(), "Connect null", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Log.e("Frag1", "btnError");
                break;
        }
    }

    private void clearEditText()
    {
        et_enter_city.setText("");
    }

    private void showRecycleViewContent()
    {
        citiesArrayList = showCities();
        cityAdapter = new CityAdapter(citiesArrayList);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager1);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(cityAdapter);
    }

    private Cities setCity(Cities c)
    {
        c.setCityName(et_enter_city.getText().toString());
        return c;
    }

    private void connectionToDB()
    {
        ormHelper = new DBOrmHelper(getContext());
        try
        {
            cityDao = DaoManager.createDao(ormHelper.getConnectionSource(),Cities.class);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private void addFavorCity(Cities c)
    {
        try
        {
            cityDao.create(c);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private ArrayList<Cities> showCities()
    {
        ArrayList<Cities> cc = new ArrayList<>();
        try
        {
            cc = (ArrayList<Cities>) cityDao.queryForAll();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return cc;
    }

    @Override
    public void onRefresh()
    {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                connectionToDB();
                showRecycleViewContent();
                showCities();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }
}
