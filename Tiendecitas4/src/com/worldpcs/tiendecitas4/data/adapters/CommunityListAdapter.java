package com.worldpcs.tiendecitas4.data.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.worldpcs.tiendecitas4.App;
import com.worldpcs.tiendecitas4.R;
import com.worldpcs.tiendecitas4.data.DataProvider;
import com.worldpcs.tiendecitas4.data.pojo.Photo;

public class CommunityListAdapter extends ArrayAdapter<Photo> {

	public CommunityListAdapter(Context context, int resource,	int textViewResourceId, List<Photo> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public CommunityListAdapter(Context context, int resource, int textViewResourceId, Photo[] objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public CommunityListAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public CommunityListAdapter(Context context, int resource, List<Photo> objects) {
		super(context, resource, objects);
	}

	public CommunityListAdapter(Context context, int resource, Photo[] objects) {
		super(context, resource, objects);
	}

	public CommunityListAdapter(Context context, int resource) {
		super(context, resource);
	}

	public CommunityListAdapter(Context context) {
		super(context, R.layout.fragment_community_listview_row);
	}

	
	@Override
	public Photo getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public int getCount() {
		return DataProvider.getInstance().getPhotos().size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView =  LayoutInflater.from(getContext()).inflate(R.layout.fragment_community_listview_row, null, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		else 
			holder = (ViewHolder) convertView.getTag();

		//Ponemos la desc
		holder.getCommunity_photo_desc().setText(DataProvider.getInstance().getPhotos().get(position).getDesc());
		
		//Mostramos la foto
		NetworkImageView niv = holder.getCommunity_photo();
		//Le establecemos la url
		String url=DataProvider.getInstance().getPhotos().get(position).getURL();
		niv.setDefaultImageResId(R.drawable.ic_photo);
		niv.setErrorImageResId(R.drawable.error);
		niv.setImageUrl(	url, 
							((App)getContext().getApplicationContext()).imageLoader);

		return convertView;  
	}
	/**
	 * Usamos un patrón viewholder
	 * @author Xarly
	 *
	 */
	private class ViewHolder{
		/**
		 * La fila
		 */
		public View row=null;
		/**
		 * Texto de la fila
		 */
		public TextView community_photo_desc=null;
		/**
		 * Imagen de la fila
		 */
		public NetworkImageView community_photo=null;
		/**
		 * Ctor
		 * @param row la fila
		 */
		public ViewHolder(View row){
			this.row=row;
		}
		/**
		 * getter
		 * @return
		 */
		public TextView getCommunity_photo_desc() {
			if(this.community_photo_desc==null)
				community_photo_desc=(TextView) row.findViewById(R.id.community_photo_desc);
			return community_photo_desc;
		}
		/**
		 * getter
		 * @return
		 */
		public NetworkImageView getCommunity_photo() {
			if(this.community_photo==null)
				community_photo=(NetworkImageView) row.findViewById(R.id.community_photo);
			return community_photo;
		}
	}
	
}
