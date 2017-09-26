package com.example.abinath.styleomega.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.abinath.styleomega.model.Product;
import com.example.abinath.styleomega.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Abinath on 26-Aug-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    public static DatabaseHelper instance = null;

    private static final int DATABASE_VERSION = 1;
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "UserManager.db";

    private static final String TABLE_USER = "user";
    private static final String TABLE_PRODUCT = "product";
    private static final String TABLE_CART = "cart";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_ADDRESS = "user_address";
    private static final String COLUMN_USER_PHONE = "user_phone";

    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";
    private static final String COLUMN_PRODUCT_IMAGE = "product_image";
    private static final String COLUMN_PRODUCT_GENDER = "product_gender";
    private static final String COLUMN_PRODUCT_TYPE = "product_type";
    private static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";

    private static final String COLUMN_CART_PRODUCT_ID = "product_id";
    private static final String COLUMN_CART_USER_ID = "user_id";
    private static final String COLUMN_CART_PRODUCT_QUANTITY = "product_quantity";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_ADDRESS+ " TEXT,"
            + COLUMN_USER_PHONE+ " TEXT"
            + ")";

    private String CREATE_PRODUCGT_TABLE = "CREATE TABLE " + TABLE_PRODUCT+ "("
            + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PRODUCT_NAME + " TEXT,"
            + COLUMN_PRODUCT_PRICE + " DOUBLE,"
            + COLUMN_PRODUCT_IMAGE + " TEXT,"
            + COLUMN_PRODUCT_GENDER + " TEXT,"
            + COLUMN_PRODUCT_TYPE + " TEXT,"
            + COLUMN_PRODUCT_QUANTITY + " INTEGER"
            + ")";

    private String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART+ "("
            + COLUMN_CART_PRODUCT_ID + " INTEGER,"
            + COLUMN_CART_USER_ID + " INTEGER,"
            + COLUMN_CART_PRODUCT_QUANTITY+ " INTEGER,"
            + "PRIMARY KEY ("
                +COLUMN_CART_PRODUCT_ID+","
                +COLUMN_CART_USER_ID+"),"
            + "FOREIGN KEY("+COLUMN_CART_USER_ID+") REFERENCES "+TABLE_USER+"("+COLUMN_USER_ID+"),"
            + "FOREIGN KEY("+COLUMN_CART_PRODUCT_ID+") REFERENCES "+TABLE_PRODUCT+"("+COLUMN_PRODUCT_ID+")"
            + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + TABLE_PRODUCT;
    private String DROP_CART_TABLE = "DROP TABLE IF EXISTS " + TABLE_CART;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context){
        if(instance==null)
            instance = new DatabaseHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCGT_TABLE);
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_PRODUCT_TABLE);
        db.execSQL(DROP_CART_TABLE);
        onCreate(db);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        values.put(COLUMN_USER_PHONE, user.getPhone());

        db.update(TABLE_USER,values,COLUMN_USER_ID + " = ?", new String[]{ Integer.toString(user.getId())});
    }

    public void addProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_IMAGE, product.getImage());
        values.put(COLUMN_PRODUCT_PRICE , product.getPrice());
        values.put(COLUMN_PRODUCT_GENDER , product.getGender());
        values.put(COLUMN_PRODUCT_TYPE , product.getType());
        values.put(COLUMN_PRODUCT_QUANTITY , product.getQuantity());

        db.insert(TABLE_PRODUCT, null, values);
        db.close();
    }

    public void addToCart(User user, Product product,int quantity){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {
                COLUMN_CART_PRODUCT_ID,
                COLUMN_CART_USER_ID
        };
        String selection = COLUMN_CART_USER_ID + " = ? AND "+COLUMN_CART_PRODUCT_ID + " = ?";
        String[] selectionArgs = { Integer.toString(user.getId()), Integer.toString(product.getId())};
        Cursor cursor = db.query(TABLE_CART,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();

        if(cursorCount > 0){
            if(quantity == 0){
                db.delete(TABLE_CART, COLUMN_CART_USER_ID + " = ? AND "+COLUMN_CART_PRODUCT_ID + " = ?", new String[]{ Integer.toString(user.getId()), Integer.toString(product.getId())});
            }else{
                ContentValues values = new ContentValues();
                values.put(COLUMN_CART_USER_ID, user.getId());
                values.put(COLUMN_CART_PRODUCT_ID, product.getId());
                values.put(COLUMN_CART_PRODUCT_QUANTITY, quantity);

                db.update(TABLE_CART,values,COLUMN_CART_USER_ID + " = ? AND "+COLUMN_CART_PRODUCT_ID + " = ?", new String[]{ Integer.toString(user.getId()), Integer.toString(product.getId())});
            }
        }else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_USER_ID, user.getId());
            values.put(COLUMN_CART_PRODUCT_ID, product.getId());
            values.put(COLUMN_CART_PRODUCT_QUANTITY, quantity);

            db.insert(TABLE_CART, null, values);
        }
        db.close();
    }

    public HashMap<Product, Integer> getAllCartProducts(User user){
        HashMap<Product, Integer> products = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_CART+" WHERE "+COLUMN_CART_USER_ID+"="+user.getId(),
                null);
        int cursorCount = cursor.getCount();
        Log.d(TAG, "getAllProducts: "+cursorCount);
        while(cursor.moveToNext()){
            Product product = getProductForID(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_PRODUCT_ID)));
            int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_PRODUCT_QUANTITY));
            products.put(product,quantity);
        }
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return products;
        }
        return null;
    }

    public ArrayList<Product> getAllProducts(){
        ArrayList<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_PRODUCT,
                null);
        int cursorCount = cursor.getCount();
        Log.d(TAG, "getAllProducts: "+cursorCount);
        while(cursor.moveToNext()){
            products.add(new Product(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_GENDER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY))
            ));
        }
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return products;
        }
        return null;
    }

    public Product getProductForID(int id){
        Product p = null;
        String[] columns = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_GENDER,
                COLUMN_PRODUCT_TYPE,
                COLUMN_PRODUCT_QUANTITY
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = { Integer.toString(id)};

        Cursor cursor = db.query(TABLE_PRODUCT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();
        Log.d(TAG, "addProductForGender: "+cursorCount);
        while(cursor.moveToNext()){
            p = new Product(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_GENDER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY))
            );
        }
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return p;
        }
        return null;

    }

    public ArrayList<Product> getProductForGender(String gender){
        ArrayList<Product> products = new ArrayList<>();
        String[] columns = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_GENDER,
                COLUMN_PRODUCT_TYPE,
                COLUMN_PRODUCT_QUANTITY
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_PRODUCT_GENDER + " = ?";
        String[] selectionArgs = { gender };

        Cursor cursor = db.query(TABLE_PRODUCT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        Log.d(TAG, "addProductForGender: "+cursorCount);
        while(cursor.moveToNext()){
            products.add(new Product(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_GENDER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY))
            ));
        }
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return products;
        }
        return null;
    }

    public ArrayList<Product> getProductForType(String type){
        ArrayList<Product> products = new ArrayList<>();
        String[] columns = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_GENDER,
                COLUMN_PRODUCT_TYPE,
                COLUMN_PRODUCT_QUANTITY
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_PRODUCT_TYPE+ " = ?";
        String[] selectionArgs = { type };

        Cursor cursor = db.query(TABLE_PRODUCT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        Log.d(TAG, "addProductForType: "+cursorCount);
        while(cursor.moveToNext()){
            products.add(new Product(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_GENDER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY))
            ));
        }
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return products;
        }
        return null;
    }

    public ArrayList<String> getProductTypes(String gender){
        ArrayList<String> types = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT "+COLUMN_PRODUCT_TYPE+" FROM "+TABLE_PRODUCT+" WHERE "+COLUMN_PRODUCT_GENDER+"='"+gender+"'",
                null);
        int cursorCount = cursor.getCount();
        Log.d(TAG, "getProductTypes: "+cursorCount);
        while(cursor.moveToNext()){
            types.add(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE)));
        }
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return types;
        }
        return null;
    }

    public ArrayList<String> getProductGender(){
        /*ArrayList<String> types = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"0",COLUMN_PRODUCT_ID,  COLUMN_PRODUCT_GENDER};
        String sqlTables = TABLE_PRODUCT;
        qb.setTables(sqlTables);
        qb.setDistinct(true);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        Log.d(TAG, "getProductGender: "+c.getCount());
        while(c.moveToNext()){
            types.add(c.getString(c.getColumnIndex(COLUMN_PRODUCT_GENDER)));
        }
        return types;*/

        ArrayList<String> types = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT "+COLUMN_PRODUCT_GENDER+" FROM "+TABLE_PRODUCT,
                null);
        int cursorCount = cursor.getCount();
        Log.d(TAG, "getProductTypes: "+cursorCount);
        while(cursor.moveToNext()){
            types.add(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_GENDER)));
        }
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return types;
        }
        return null;
    }

    public ArrayList<Product> getProductForArgs(String text, String gender, String type, double lowerPrice, double higherPrice){
        ArrayList<Product> products = new ArrayList<>();
        String[] columns = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_GENDER,
                COLUMN_PRODUCT_TYPE,
                COLUMN_PRODUCT_QUANTITY
        };
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = "";
        ArrayList<String> selectionArgs = new ArrayList<>();
        if(text!=null && !text.isEmpty()) {
            selection = COLUMN_PRODUCT_NAME + " LIKE ?";
            selectionArgs.add(text);
        }
        if(gender != null && !gender.isEmpty()) {
            selection += selection.isEmpty() ? COLUMN_PRODUCT_GENDER + " = ?" : " and " + COLUMN_PRODUCT_GENDER + " = ?";
            selectionArgs.add(gender);
        }
        if(type!=null && !type.isEmpty()) {
            selection += selection.isEmpty() ? COLUMN_PRODUCT_TYPE + " = ?" : " and " + COLUMN_PRODUCT_TYPE + " = ?";
            selectionArgs.add(type);
        }
        if(lowerPrice>0 && higherPrice>0) {
            selection += selection.isEmpty() ? COLUMN_PRODUCT_PRICE + " between ? and ?" : " and " + COLUMN_PRODUCT_PRICE + " between ? and ?";
            selectionArgs.add(Integer.toString((int)lowerPrice));
            selectionArgs.add(Integer.toString((int)higherPrice));
        }


        String[] argsAsArray = Arrays.copyOf(selectionArgs.toArray(), selectionArgs.toArray().length, String[].class);
        for(String s : argsAsArray)
            Log.d(TAG, "getProductForArgs: "+s);
        Cursor cursor = db.query(TABLE_PRODUCT,
                columns,
                selection,
                argsAsArray,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        Log.d(TAG, "getProductForArgs: "+cursorCount);
        while(cursor.moveToNext()){
            products.add(new Product(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_GENDER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY))
            ));
        }
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return products;
        }
        return null;
    }

    public ArrayList<Product> getProductForPrice(double lowerPrice, double higherPrice){
        ArrayList<Product> products = new ArrayList<>();
        String[] columns = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_GENDER,
                COLUMN_PRODUCT_TYPE,
                COLUMN_PRODUCT_QUANTITY
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_PRODUCT_PRICE + " between ? and ?";
        String[] selectionArgs = {Double.toString(lowerPrice), Double.toString(higherPrice) };

        Cursor cursor = db.query(TABLE_PRODUCT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        Log.d(TAG, "getProductForArgs: "+cursorCount);
        while(cursor.moveToNext()){
            products.add(new Product(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_GENDER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY))
            ));
        }
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return products;
        }
        return null;
    }

    public boolean checkUser(String email){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public User getUsserForID(String email){
        User user = null;
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_ADDRESS,
                COLUMN_USER_PHONE,
                COLUMN_USER_PASSWORD
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();


        if (cursorCount > 0){
            cursor.moveToNext();
            user = new User(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)),
                                            cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                                            cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)),
                                            cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)),
                                            cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)),
                                            cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
            cursor.close();
            db.close();
            return user;
        }
        cursor.close();
        db.close();
        return null;
    }

    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public void checkOut(Product product, User user, Integer quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_IMAGE, product.getImage());
        values.put(COLUMN_PRODUCT_PRICE , product.getPrice());
        values.put(COLUMN_PRODUCT_GENDER , product.getGender());
        values.put(COLUMN_PRODUCT_TYPE , product.getType());
        values.put(COLUMN_PRODUCT_QUANTITY , product.getQuantity() - quantity);
        int result = db.update(TABLE_PRODUCT,values,COLUMN_PRODUCT_ID+" = ?",new String[]{ Integer.toString(product.getId())});
        db.delete(TABLE_CART, COLUMN_CART_USER_ID + " = ? AND "+COLUMN_CART_PRODUCT_ID + " = ?", new String[]{ Integer.toString(user.getId()), Integer.toString(product.getId())});
        Log.d(TAG, "checkOut: "+result);
        db.close();
    }
}
