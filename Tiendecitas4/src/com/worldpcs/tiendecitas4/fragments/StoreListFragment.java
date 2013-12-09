package com.worldpcs.tiendecitas4.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.worldpcs.tiendecitas4.R;
import com.worldpcs.tiendecitas4.activities.StoreDetailActivity;
import com.worldpcs.tiendecitas4.data.DataProvider;
import com.worldpcs.tiendecitas4.data.pojo.Store;

public class StoreListFragment extends Fragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//Creamos un listado a partir de los datos de las tiendas
		Iterator<Store> it=DataProvider.getInstance().getStores().iterator();
		ArrayList<HashMap<String,String>> list=new  ArrayList<HashMap<String,String>>();
		HashMap<String, String> map;
		while(it.hasNext()){
			Store store=it.next();
			map = new HashMap<String,String>();
			map.put(DataProvider.TAG_NAME, store.getName());
			map.put(DataProvider.TAG_DESC, store.getDesc());
			list.add(map);
		}
		//Keys
		String[] keys=new String[]{
				DataProvider.TAG_NAME,
				DataProvider.TAG_DESC};

		//Creamos el adaptador
		SimpleAdapter adapter = new SimpleAdapter(	getActivity(),
													list,
													android.R.layout.simple_list_item_2,
													keys,
													new int[]{	android.R.id.text1,
																android.R.id.text2});
		//Lo establecemos como el definido
		ListView lv=((ListView) getActivity().findViewById(R.id.store_list));
		lv.setAdapter(adapter);
		//Creamos un listener para los elementos del listado
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				DataProvider.getInstance().setCurrentStore(position);
		        Intent intent = new Intent(l.getContext(),StoreDetailActivity.class);
		        startActivity(intent);
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_store_list,container,false);
	}


}
