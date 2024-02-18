package com.example.newproject.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;

import com.example.newproject.helper.Expense;
import com.example.newproject.helper.ExpenseManager;
import com.example.newproject.helper.Type;

import java.text.ParseException;
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
    private Context context;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
                        " DATE, " +
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

    public void insertData(Expense expense) throws SQLiteException{
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues expContent = new ContentValues();
        expContent.put(TITLE_FIELD, expense.getTitle());
        expContent.put(COST_FIELD, expense.getCost());
        expContent.put(TYPE_FIELD, String.valueOf(expense.getType()));
        expContent.put(DATE_FIELD, dateFormat.format(expense.getDate()));
        expContent.put(DESC_FIELD, expense.getDescription());
        try {
            db.insert(TABLE_NAME, null, expContent);
        }catch(SQLiteException e){
            throw new SQLiteException();
        }
        listAllExpenses();
    }
    public void updateExpense(Expense expense) throws SQLiteException{
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, expense.getId());
        contentValues.put(TITLE_FIELD, expense.getTitle());
        contentValues.put(COST_FIELD, expense.getCost());
        contentValues.put(TYPE_FIELD, getStringFromType(expense.getType()));
        contentValues.put(DATE_FIELD, dateFormat.format(expense.getDate()));
        contentValues.put(DESC_FIELD, expense.getDescription());
        try {
            db.update(TABLE_NAME, contentValues,
                    ID_FIELD + " =? ", new String[]{String.valueOf(expense.getId())});
            listAllExpenses();
        }catch(SQLiteException e){
            throw new SQLiteException();
        }
    }
    public boolean deleteExpense(Expense expense) throws SQLiteException{
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            db.delete(TABLE_NAME,
                    ID_FIELD + " =? ", new String[]{String.valueOf(expense.getId())});
            listAllExpenses();
            return true;
        }catch(SQLiteException e){
            throw new SQLiteException();
        }
    }
    public void listAllExpenses() throws RuntimeException{
        SQLiteDatabase db = this.getReadableDatabase();
        ExpenseManager.expenseList.clear();
        try (Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    String title = result.getString(result.getColumnIndexOrThrow(TITLE_FIELD));
                    double cost = result.getDouble(result.getColumnIndexOrThrow(COST_FIELD));
                    Type type = getTypeFromString(result
                            .getString(result.getColumnIndexOrThrow(TYPE_FIELD)));
                    Date date = dateFormat.parse(result.getString(result
                            .getColumnIndexOrThrow(DATE_FIELD)));
                    String desc = result.getString(result.getColumnIndexOrThrow(DESC_FIELD));
                    Expense expense = new Expense(title, cost, type, date, desc);
                    expense.setId(result.getLong(result.getColumnIndexOrThrow(ID_FIELD)));
                    ExpenseManager.expenseList.add(expense);
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private String getStringFromType(Type type){
        return type.toString();
    }
    private Type getTypeFromString(String type){
        return Type.valueOf(type);
    }

}
