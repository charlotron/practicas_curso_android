package com.worldpcs.tiendecitas2.activities;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.worldpcs.tiendecitas2.R;
import com.worldpcs.tiendecitas2.dataSource.ShopDataProvider;

public class CommentsActionBarActivity extends android.support.v7.app.ActionBarActivity {

	public static final String DETAIL = "detail";
	public static final String PHOTO = 	"photo";
	public static final String TYPE = "type";
	
	protected String comment_type=null;
	
	public CommentsActionBarActivity() {
		super();
	}
	//Envía un comentario
	protected void sendComment() {
		//Recuperamos el texto del edittext
		EditText et;
		if(comment_type==DETAIL)
			et=(EditText)findViewById(R.id.sendDetailComment);
		else
			et=(EditText)findViewById(R.id.sendShotComment);
	
		
		if(et.getText().toString().trim().equals("")) return;
		if(comment_type==DETAIL)
			ShopDataProvider.getInstance().addShopDetailComment(et.getText().toString());
		else
			ShopDataProvider.getInstance().addShopPhotoComment(et.getText().toString());
		setCommentAdapter();
		et.setText(null);
		
	}


	public void setCommentAdapter(){
		//Ya sabemos el tipo, recuperamos el array de comentarios
		ArrayList<String> comments=null;
		//FrameLayout container=null;
		if(comment_type==DETAIL){
			comments=ShopDataProvider.getInstance().getShopDetailComment();
			//container=(FrameLayout) findViewById(R.id.shop_detail_comments_container);
		}
		else{
			comments=ShopDataProvider.getInstance().getShopPhotoComment();
			//container=(FrameLayout) getActivity().findViewById(R.id.shop_photo_comments_container);
		}
		//Creamos el adaptador
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(	this, 
																R.layout.fragment_shop_comment_item,
																comments);
		
		//Lo establecemos como el definido
		ListView lv=((ListView) findViewById(R.id.comment_list));
		lv.setAdapter(adapter);
		//Estu suple un problema de poner un ListView dentro de un ScrollView
		setListViewHeightBasedOnChildren(lv);
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
	
}
