package com.blues.gallery.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import com.blues.gallery.Adaptors.ImageModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ritesh Shakya on 8/17/2016.
 */

public class DatabaseContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOLEAN_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CollectionEntry.TABLE_NAME + " (" +
                    CollectionEntry._ID + " INTEGER PRIMARY KEY," +
                    CollectionEntry.COLUMN_NAME_COLLECTION_TITLE + TEXT_TYPE + COMMA_SEP +
                    CollectionEntry.COLUMN_NAME_PATH + TEXT_TYPE + COMMA_SEP +
                    CollectionEntry.COLUMN_NAME_KEYWORD + TEXT_TYPE + COMMA_SEP +
                    CollectionEntry.COLUMN_NAME_JPEG_CHECK + BOOLEAN_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CollectionEntry.TABLE_NAME;

    public DatabaseContract() {
    }

    static abstract class CollectionEntry implements BaseColumns {
        static final String TABLE_NAME = "collection";
        static final String COLUMN_NAME_COLLECTION_TITLE = "collection_title";
        static final String COLUMN_NAME_PATH = "image_path";
        static final String COLUMN_NAME_KEYWORD = "keyword";
        static final String COLUMN_NAME_JPEG_CHECK = "jpeg_check";
    }

    public static class DbHelper extends SQLiteOpenHelper {

        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "JpegPlusBrowser.db";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        public void onInsert(ArrayList<ImageModel> data, String titleText) {
            SQLiteDatabase db = getWritableDatabase();
            onDelete(titleText);
            for (ImageModel imageModel : data) {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.CollectionEntry.COLUMN_NAME_COLLECTION_TITLE, titleText);
                values.put(DatabaseContract.CollectionEntry.COLUMN_NAME_PATH, imageModel.getUrl());
                values.put(DatabaseContract.CollectionEntry.COLUMN_NAME_KEYWORD, titleText);
                values.put(DatabaseContract.CollectionEntry.COLUMN_NAME_JPEG_CHECK, imageModel.isCheckJpeg());

                db.insert(
                        DatabaseContract.CollectionEntry.TABLE_NAME,
                        null,
                        values);
            }
        }

        public void onDelete(String titleText) {
            SQLiteDatabase db = getWritableDatabase();
            String selection = CollectionEntry.COLUMN_NAME_COLLECTION_TITLE + " = ?";
            String[] selectionArgs = {String.valueOf(titleText)};
            db.delete(CollectionEntry.TABLE_NAME, selection, selectionArgs);
        }

        public ArrayList<ImageModel> onSelect(String titleText) {
            SQLiteDatabase db = getReadableDatabase();
            ArrayList<ImageModel> data = new ArrayList<>();

            String[] projection = {
                    CollectionEntry.COLUMN_NAME_PATH,
                    CollectionEntry.COLUMN_NAME_JPEG_CHECK
            };
            String selection = CollectionEntry.COLUMN_NAME_COLLECTION_TITLE + " LIKE ?";
            String sortOrder =
                    CollectionEntry.COLUMN_NAME_PATH + " DESC";

            String[] selectionArgs = {"%" + titleText + "%"};

            Cursor res = db.query(
                    true,
                    CollectionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder,
                    null
            );
            res.moveToFirst();

            while (!res.isAfterLast()) {
                ImageModel imageModel = new ImageModel();
                File directory = new File(res.getString(res.getColumnIndex(CollectionEntry.COLUMN_NAME_PATH)));
                imageModel.setName(directory.getName());
                imageModel.setUrl(directory.getAbsolutePath());
                imageModel.setCheckJpeg(res.getInt(res.getColumnIndex(CollectionEntry.COLUMN_NAME_JPEG_CHECK)) == 1);
                data.add(imageModel);
                res.moveToNext();
            }
            return data;
        }

        public String[] getTitles() {
            SQLiteDatabase db = getReadableDatabase();
            ArrayList<String> data = new ArrayList<>();
            Cursor res = db.rawQuery("SELECT DISTINCT " +
                            CollectionEntry.COLUMN_NAME_COLLECTION_TITLE +
                            " FROM " + CollectionEntry.TABLE_NAME,
                    null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                data.add(res.getString(res.getColumnIndex(CollectionEntry.COLUMN_NAME_COLLECTION_TITLE)));
                res.moveToNext();
            }
            String[] stockArr = new String[data.size()];
            stockArr = data.toArray(stockArr);
            return stockArr;
        }
    }


    public static class InsertFiles extends AsyncTask<String, String, String> {

        public InsertFiles(Context context, ArrayList<ImageModel> data, String titleText) {
            this.context = context;
            this.data = data;
            this.titleText = titleText;
        }

        private Context context;
        private ArrayList<ImageModel> data;
        private String titleText;

        @Override
        protected String doInBackground(String... strings) {
            DatabaseContract.DbHelper mDbHelper = new DatabaseContract.DbHelper(context);
            mDbHelper.onInsert(data, titleText);
            return null;
        }
    }

    public static class ReadFiles extends AsyncTask<String, String, ArrayList<ImageModel>> {

        public ReadFiles(Context context, String titleText) {
            this.context = context;
            this.titleText = titleText;
        }

        private Context context;
        private String titleText;

        @Override
        protected ArrayList<ImageModel> doInBackground(String... strings) {
            DbHelper mDbHelper = new DbHelper(context);
            return mDbHelper.onSelect(titleText);
        }
    }

    public static class DeleteFiles extends AsyncTask<String, String, String> {

        public DeleteFiles(Context context, String titleText) {
            this.context = context;
            this.titleText = titleText;
        }

        private Context context;
        private String titleText;

        @Override
        protected String doInBackground(String... strings) {
            DbHelper mDbHelper = new DbHelper(context);
            mDbHelper.onDelete(titleText);
            return null;
        }
    }

}
