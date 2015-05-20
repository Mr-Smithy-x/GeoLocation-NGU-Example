package com.geo;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.*;
import android.view.View.*;
import android.graphics.drawable.*;
import android.content.*;

public class PopupInfo extends DialogFragment implements OnClickListener
{
		Location location;
		TextView ip, info,extra, close, copy;
		ImageView img;

		public PopupInfo(Location location)
		{
				this.location = location;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
				// TODO: Implement this method
				CardView view = (CardView)inflater.inflate(R.layout.popup, container, false);
				ip = (TextView)view.findViewById(R.id.popup_ip);
				info = (TextView)view.findViewById(R.id.popup_city);
				extra = (TextView)view.findViewById(R.id.popup_extra);
				close = (TextView)view.findViewById(R.id.popup_close);
				copy = (TextView)view.findViewById(R.id.popup_copy);
				close.setOnClickListener(this);
				copy.setOnClickListener(this);
				img = (ImageView)view.findViewById(R.id.popup_icon);
				ip.setText(location.getIp());
				info.setText(String.format("%s, %s (%s), %s, %s (%s)", location.getCity(), location.getRegion_name(), location.getRegion_code(), location.getZip_code(), location.getCountry_name(), location.getCountry_code()));
				extra.setText(String.format("Time Zone: %s\nMetro Code: %s\nLatitude: %s\nLongitude: %s", location.getTime_zone(), location.getMetro_code(), location.getLatitude(), location.getLongitude()));
				img.setImageResource(R.mipmap.ic_maps);
				getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
				getDialog().getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
				getDialog().getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
				getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
				return view;
		}

		@Override
		public void onClick(View p1)
		{
				switch (p1.getId())
				{
						case R.id.popup_close:
								dismiss();
								return;
						case R.id.popup_copy:
								ClipboardManager cm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
								cm.setPrimaryClip(cm.getPrimaryClip().newPlainText("Geo", String.format("IP: %s\nCountry Code: %s\nCountry Name: %s\nRegion Code: %s\nRegion Name: %s\nCity: %s\nZip Code: %s\nTime Zone: %s\nLatitude: %s\nLongitude: %s\nMetro Code: %s", location.getIp(), location.getCountry_code(), location.getCountry_name(), location.getRegion_code(), location.getRegion_name(), location.getCity(), location.getZip_code(), location.getTime_zone(), location.getLatitude(), location.getLongitude(), location.getMetro_code())));
								Toast.makeText(getActivity(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
								return;
				}
		}
}
