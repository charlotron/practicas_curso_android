package com.worldpcs.tiendecitas4.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
		//****** Table Stores ******
		public static final String TABLE_STORES="stores";
	
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_DESC = "desc";
        public static final String KEY_ADDRESS = "address";
        public static final String KEY_TELEPHONE = "telephone";
        public static final String KEY_TIMES = "times";
        public static final String KEY_WEBSITE = "website";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_GEO_LAT = "geo_lat";
        public static final String KEY_GEO_LONG = "geo_long";
        public static final String KEY_FAV_COUNT = "fav_count";
        
        private static final String TABLE_STORES_CREATE = 	"CREATE TABLE " + TABLE_STORES + "(" + 
        															KEY_ID + " integer primary key autoincrement, " +
        															KEY_NAME + " text, " + 
        															KEY_DESC + " text," +
        															KEY_ADDRESS + " text," +
        															KEY_TELEPHONE + " text," +
        															KEY_TIMES + " text," +
        															KEY_WEBSITE + " text," +
        															KEY_EMAIL + " text," +
        															KEY_GEO_LAT + " text," +
        															KEY_GEO_LONG + " text," +
        															KEY_FAV_COUNT + " integer)";        
        private static final String TABLE_STORES_DROP = "DROP TABLE IF EXISTS " + TABLE_STORES;
        
        
        //****** Table Stores Comments ********
		public static final String TABLE_STORES_COMMENTS="stores_comments";
       
        //public static final String KEY_ID = "id";
        public static final String KEY_STORE_ID = "store_id";
        public static final String KEY_COMMENT = "comment";
        

        private static final String TABLE_STORES_COMMENTS_CREATE = 	"CREATE TABLE " + TABLE_STORES_COMMENTS + "(" + 
	        															KEY_ID + " integer primary key autoincrement, " +
	        															KEY_STORE_ID + " integer, " + 
	        															KEY_COMMENT + " text)";    
        private static final String TABLE_STORES_COMMENTS_DROP = "DROP TABLE IF EXISTS " + TABLE_STORES_COMMENTS;

        //****** Table Stores Photos ********
		public static final String TABLE_STORES_PHOTOS="stores_photos";
       
        //public static final String KEY_STORE_ID = "store_id";
        public static final String KEY_PHOTO_ID = "photo_id";
        
        private static final String TABLE_STORES_PHOTOS_CREATE = 	"CREATE TABLE " + TABLE_STORES_PHOTOS + "(" + 
        																KEY_STORE_ID + " integer, " +
        																KEY_PHOTO_ID + " integer primary key)";          
        private static final String TABLE_STORES_PHOTOS_DROP = "DROP TABLE IF EXISTS " + TABLE_STORES_PHOTOS;

        //****** Table Photos ********
		public static final String TABLE_PHOTOS="photos";
       
        //public static final String KEY_ID = "id";
        public static final String KEY_URL = "URL";
        //public static final String KEY_DESC = "desc";
        //public static final String KEY_FAV_COUNT = "fav_count";
       
        private static final String TABLE_PHOTOS_CREATE = "CREATE TABLE " + TABLE_PHOTOS + "(" + 
        														KEY_ID + " integer primary key autoincrement, " +
        														KEY_URL + " text, " +
        														KEY_DESC + " text, " +
        														KEY_FAV_COUNT + " integer)";    
        private static final String TABLE_PHOTOS_DROP = "DROP TABLE IF EXISTS " + TABLE_PHOTOS;
        
        //****** Table Photos Comments ********
		public static final String TABLE_PHOTOS_COMMENTS="photos_comments";
       
        //public static final String KEY_ID = "id";
        //public static final String KEY_PHOTO_ID = "photo_id";
		//public static final String KEY_COMMENT = "comment";
       
        private static final String TABLE_PHOTOS_COMMENTS_CREATE = "CREATE TABLE " + TABLE_PHOTOS_COMMENTS + "(" + 
		        														KEY_ID + " integer primary key autoincrement, " +
		        														KEY_PHOTO_ID + " integer, " +
		        														KEY_COMMENT + " text)";    
        private static final String TABLE_PHOTOS_COMMENTS_DROP = "DROP TABLE IF EXISTS " + TABLE_PHOTOS_COMMENTS;
        /**
         * Flag que indica si acaba de ser creada la base de datos o no
         */
		private boolean emptyTables=false;
        
        /**
         * Ctor
         * @param context
         * @param name
         * @param factory
         * @param version
         */
        public DBHelper(Context context, String name, CursorFactory factory, int version) {
                super(context, name, factory, version);
        }
        /**
         * Creamos la base de datos
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_STORES_CREATE);
            db.execSQL(TABLE_STORES_COMMENTS_CREATE);
            db.execSQL(TABLE_STORES_PHOTOS_CREATE);
            db.execSQL(TABLE_PHOTOS_CREATE);
            db.execSQL(TABLE_PHOTOS_COMMENTS_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL(TABLE_STORES_DROP);        
                db.execSQL(TABLE_STORES_COMMENTS_DROP);        
                db.execSQL(TABLE_STORES_PHOTOS_DROP);        
                db.execSQL(TABLE_PHOTOS_DROP);
                db.execSQL(TABLE_PHOTOS_COMMENTS_DROP);
                
                onCreate(db);
                
                emptyTables=true;
        }
        /**
         * Getter
         * @return
         */
        public boolean isEmptyTables(){
        	return emptyTables;
        }
        /**
         * Setter
         * @param emptyTables
         */
		public void setEmptyTables(boolean emptyTables) {
			this.emptyTables = emptyTables;
		}
        
        
}