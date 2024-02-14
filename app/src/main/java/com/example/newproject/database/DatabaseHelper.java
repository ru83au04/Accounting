package com.example.newproject.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.newproject.helper.Expense;
import com.example.newproject.helper.Type;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;
    private static final String DATABASE_NAME = "Expense";
    private static final String TABLE_NAME = "ExpenseTable";
    private static final int DATABASE_VERSION = 2;
    private static final String ID_FIELD = "id";

    private static final String TITLE_FIELD = "title";
    private static final String COST_FIELD = "cost";
    private static final String TYPE_FIELD = "type";
    private static final String DATE_FIELD = "date";
    private static final String DESC_FIELD = "description";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper instanceOfDatabase(Context context){
        if(databaseHelper == null){
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate =
                "CREATE TABLE IF NOT EXISTS ExpenseTable" +
                        "(" +
                        ID_FIELD +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TITLE_FIELD +
                        " TEXT, " +
                        COST_FIELD +
                        " REAL, " +
                        TYPE_FIELD +
                        " TEXT, " +
                        DATE_FIELD +
                        " TEXT, " +
                        DESC_FIELD +
                        " TEXT)";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgradeTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;

        db.execSQL(upgradeTableQuery);
        onCreate(db);
    }

    public void insertData(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues expContent = new ContentValues();
        expContent.put(TITLE_FIELD, expense.getTitle());
        expContent.put(COST_FIELD, expense.getCost());
        expContent.put(TYPE_FIELD, String.valueOf(expense.getType()));
        expContent.put(DATE_FIELD, expense.getDate());
        expContent.put(DESC_FIELD, expense.getDescription());

        db.insert(TABLE_NAME, null, expContent);
    }

    public void populateExpenseListArray(){
        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    String title = result.getString(result.getColumnIndexOrThrow(TITLE_FIELD));
                    double cost = result.getDouble(result.getColumnIndexOrThrow(COST_FIELD));
                    Type type = getTypeFromString(result
                            .getString(result.getColumnIndexOrThrow(TYPE_FIELD)));
                    String date = result.getString(result
                            .getColumnIndexOrThrow(DATE_FIELD));
                    String desc = result.getString(result.getColumnIndexOrThrow(DESC_FIELD));
                    Expense expense = new Expense(title, cost, type, date, desc);
                    expense.setId(result.getLong(result.getColumnIndexOrThrow(ID_FIELD)));
                    Expense.expenseArrayList.add(expense);
                }
            }
        }
    }
    public void updateExpenseInDB(Expense expense){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_FIELD, expense.getTitle());
        contentValues.put(COST_FIELD, expense.getCost());
        contentValues.put(TYPE_FIELD, getStringFromType(expense.getType()));
        contentValues.put(DATE_FIELD, expense.getDate());
        contentValues.put(DESC_FIELD, expense.getDescription());

        db.update(TABLE_NAME, contentValues,
                ID_FIELD + " =? ", new String[]{String.valueOf(expense.getId())});
    }

    private String getStringFromDate(Date date) {
        if(date == null){
            return null;
        }
        return dateFormat.format(date);
    }
    private Date getDateFromString(String date){
        try{
            return dateFormat.parse(date);
        }catch(ParseException | NullPointerException e){
            return null;
        }
    }
    private String getStringFromType(Type type){
        return type.toString();
    }
    private Type getTypeFromString(String type){
        return Type.valueOf(type);
    }


}
