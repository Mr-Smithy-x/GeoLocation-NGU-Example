package com.geo;
import org.json.*;
import android.view.*;

public interface OnResult
{
		public void OnCallBack(JSONObject obj);
		public void OnError(String error);
		public void OnItemClick(View view, int position, Object object);
		public void OnItemLongClick(View view, int position, Object object);
}
