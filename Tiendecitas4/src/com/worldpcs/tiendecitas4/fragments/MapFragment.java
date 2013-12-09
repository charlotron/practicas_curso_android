package com.worldpcs.tiendecitas4.fragments;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.worldpcs.tiendecitas4.R;
import com.worldpcs.tiendecitas4.activities.StoreDetailActivity;
import com.worldpcs.tiendecitas4.data.DataProvider;
import com.worldpcs.tiendecitas4.data.pojo.Store;

public class MapFragment extends SupportMapFragment implements 	InfoWindowAdapter, 
																OnInfoWindowClickListener{
	/**
	 * Mapa que se mostrará
	 */
	private GoogleMap map=null;
	/**
	 * Mapa de marcadores
	 */
	private HashMap<Marker,Store> marker_map=new HashMap<Marker,Store>();
	/**
	 * Cordenadas de centrado de mapa
	 */
	private static final LatLng DEFAULT_CAM_POS= new LatLng(40.42,-3.69);
	/**
	 * Zoom inicial
	 */
	private static final Float DEFAULT_CAM_ZOOM= 5.7f;	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupMap();
	}

	/**
	 * Configura el mapa
	 */
	private void setupMap() {
		if(map==null){
			map=getMap();
			map.getUiSettings().setZoomControlsEnabled(false);
			if(map!=null){
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_CAM_POS, DEFAULT_CAM_ZOOM));
				map.setMyLocationEnabled(true);
				setMarkers();
				map.setInfoWindowAdapter(this);
				map.setOnInfoWindowClickListener(this);
			}
		}
	}
	/**
	 * Marca las tiendas
	 */
	private void setMarkers() {
		clearMarkers();
		for(Iterator<Store> it=DataProvider.getInstance().getStores().iterator();it.hasNext();){
			Store store=it.next();
			String title = store.getName();
			String address = store.getAddress();
			LatLng geopos=new LatLng(	Float.parseFloat(store.getGeo_position_lat()),
										(Float.parseFloat(store.getGeo_position_long())));
			createMarker(title,address,geopos, store);
		}		

	}
	/**
	 * Crea un marcador
	 * @param title
	 * @param address
	 * @param geopos
	 */
	private void createMarker(String title, String address, LatLng geopos, Store store) {
		MarkerOptions options= new MarkerOptions();
		options.position(geopos);
		options.title(title);
		options.snippet(address);
		options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
		Marker marker=map.addMarker(options);
		marker_map.put(marker,store);
	}

	/**
	 * Borra todos los marcadores
	 */
	public void clearMarkers() {
		for(Iterator<Marker> it=marker_map.keySet().iterator();it.hasNext();)
			it.next().remove();
		marker_map.clear();
	}
	/**
	 * Contenido del infowindow
	 */
	@Override
	public View getInfoContents(Marker marker) {
		View v=getActivity().getLayoutInflater().inflate(R.layout.fragment_map_infowindow, null);
		((TextView)v.findViewById(R.id.infowindow_title)).setText(marker.getTitle());
		((TextView)v.findViewById(R.id.infowindow_snippet)).setText(marker.getSnippet());
		return v;
	}
	/**
	 * Estilo del infowindow
	 */
	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}
	/**
	 * Cambia el mapa al tipo indicado
	 * @param menuItemId
	 */
	public void setMapType(int mapType) {
		switch(mapType){
		case R.id.set_map_normal:
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case R.id.set_map_satellite:
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.set_map_hibrid:
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case R.id.set_map_terrain:
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
			
		}
		
	}
	/**
	 * Implementa los clicks en las infowindow
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		Store store=marker_map.get(marker);
		DataProvider.getInstance().setCurrentStore(store);
        Intent intent = new Intent(getActivity(),StoreDetailActivity.class);
        startActivity(intent);
	}
}
