package com.worldpcs.tiendecitas2.fragments;

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

import com.worldpcs.tiendecitas2.R;
import com.worldpcs.tiendecitas2.activities.ShopDetailActivity;
import com.worldpcs.tiendecitas2.dataSource.ShopDataProvider;

public class ShopListFragment extends Fragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);	
		
		
		//Creamos el adaptador
		SimpleAdapter adapter = new SimpleAdapter(	getActivity(),
													ShopDataProvider.getInstance().getShop_data(),
													android.R.layout.simple_list_item_2,
													new String[]{ShopDataProvider.SHOP_NAME,ShopDataProvider.SHOP_DESC},
													new int[]{	android.R.id.text1,
																android.R.id.text2});
		//Lo establecemos como el definido
		ListView lv=((ListView) getActivity().findViewById(R.id.shop_list));
		lv.setAdapter(adapter);
		//Creamos un listener para los elementos del listado
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		        ShopDataProvider.getInstance().setCurrentShopId(position);
		        Intent intent = new Intent(l.getContext(),ShopDetailActivity.class);
		        startActivity(intent);
			}
		});
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_shop_list,container,false);
	}
	

}
