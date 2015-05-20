package com.geo;
import android.support.v7.widget.*;
import android.view.*;
import java.util.*;
import android.content.*;
import android.widget.*;
import android.view.View.*;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchHolder>
{
		Context context;
		List<Location> locations;
		LayoutInflater inflater;
		OnResult on;

		public SearchRecyclerAdapter( Context context, OnResult on, List<Location> locations )
		{
				this.context = context;
				this.locations = locations;
				inflater = LayoutInflater.from(context);
				this.on = on;
		}
		
		@Override
		public SearchRecyclerAdapter.SearchHolder onCreateViewHolder( ViewGroup p1, int p2 )
		{
				return new SearchHolder(inflater.inflate(R.layout.card_view,p1,false));
		}

		@Override
		public void onBindViewHolder( SearchRecyclerAdapter.SearchHolder p1, int p2 )
		{
				Location loc = locations.get(p2);
				String web = loc.getWeb();
				String ip =loc.getIp();
				String main= String.format("%s, %s (%s), %s, %s (%s)", loc.getCity(), loc.getRegion_name(),loc.getRegion_code(), loc.getZip_code(), loc.getCountry_name(), loc.getCountry_code());
				String sub =String.format("Time Zone: %s, Metrocode: %s\nLat: %s, Long: %s",loc.getTime_zone(),loc.getMetro_code(), loc.getLatitude(), loc.getLongitude());
				p1.sub.setText(sub);
				p1.important.setText(main);
				p1.ip.setText(ip);
				p1.web.setText(web);
				p1.img.setImageResource(R.mipmap.ic_maps);
		}

		@Override
		public int getItemCount( )
		{
				return locations.size();
		}

		public void InsertItem(Location loc){
				locations.add(loc);
				notifyItemInserted(locations.size());
		}
		
		public void RemoveItem(int index){				
				locations.remove(index);
				notifyItemRemoved(index);
		}
		
		public void Clear(){
				locations.clear();
				notifyDataSetChanged();
		}
		
		class SearchHolder extends RecyclerView.ViewHolder
		{			
				ImageView img;
				TextView ip, important,sub, web;
				public SearchHolder(View itemView){
						super(itemView);
					  img = (ImageView)itemView.findViewById(R.id.location_img);
						web = (TextView)itemView.findViewById(R.id.loc_web);
						ip =(TextView)itemView.findViewById(R.id.loc_ip);
						important =(TextView)itemView.findViewById(R.id.loc_city_rn_rc_zip_cn_cc);
						sub =(TextView)itemView.findViewById(R.id.loc_tz_lat_long_metro);					
				}
		}	
}


