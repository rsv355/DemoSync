package parsedemo.com.demosync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String KEY_ROWID = "_id";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "demo.db";
    private static final String TABLE_TEST = "test";


    private static final String DATABASE_PATH = "/data/data/parsedemo.com.demosync/databases/";
    private Context context;
    private SQLiteDatabase myDataBase = null;

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (dbExist) {
//            Log.v("log_tag", "database does exist");
        } else {
//            Log.v("log_tag", "database does not exist");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase() {

        File folder = new File(DATABASE_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    public boolean openDataBase() throws SQLException {
        String mPath = DATABASE_PATH + DATABASE_NAME;
        //Log.v("mPath", mPath);
        myDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return myDataBase != null;

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    // Constructor
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




    public void saveMovie(ArrayList<Movie> menu) {
        myDataBase = this.getWritableDatabase();
        myDataBase.delete(TABLE_TEST, null, null);

        ContentValues values = new ContentValues();

            for (int i = 0; i < menu.size(); i++) {

                values.put("item1", menu.get(i).getTitle());
                values.put("item2",  menu.get(i).getGenre());
                values.put("item3", menu.get(i).getYear());

                myDataBase.insert(TABLE_TEST, null, values);
        }
    }

    public ArrayList<Movie> getMovie(){
        ArrayList<Movie> MENU = new ArrayList<>();

        SQLiteDatabase sql=this.getReadableDatabase();

        String query = "SELECT * FROM MenuDetails";

        Cursor c = sql.rawQuery(query, null);
        while(c.moveToNext()) {
            Movie item = new Movie();
            item.setTitle(c.getString(c.getColumnIndexOrThrow("item1")));
            item.setGenre(c.getString(c.getColumnIndexOrThrow("item2")));
            item.setYear(c.getString(c.getColumnIndexOrThrow("item3")));

            MENU.add(item);
        }
        c.close();

        return MENU;
    }

//end of main class
}
