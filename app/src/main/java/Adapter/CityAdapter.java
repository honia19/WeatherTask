package Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tabian.tabfragments.R;

import java.util.ArrayList;

import OrmModel.Cities;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>
{
    private ArrayList<Cities> city;

    public CityAdapter(ArrayList<Cities> city)
    {
        this.city = city;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_rv_City;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tv_rv_City = (TextView) itemView.findViewById(R.id.tv_rv_City);
        }
    }

    public ArrayList<Cities> getCity()
    {
        return city;
    }

    public void setCity(ArrayList<Cities> city)
    {
        this.city = city;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Cities cities = city.get(position);
        holder.tv_rv_City.setText(cities.getCityName());
    }


    @Override
    public int getItemCount()
    {
        return city.size();
    }

    public void updateList(ArrayList<Cities> list)
    {
        this.city.clear();
        this.city.addAll(list);
        this.notifyDataSetChanged();
    }
}
