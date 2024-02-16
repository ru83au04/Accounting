package com.example.newproject.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import com.example.newproject.helper.Expense;
import com.example.newproject.helper.ExpenseManager;
import com.example.newproject.helper.Type;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

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
    private ExpenseManager expenseManager;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    public DatabaseHelper(Context context, ExpenseManager expenseManager) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.expenseManager = expenseManager;
        this.context = context;
    }

    public static synchronized DatabaseHelper instanceOfDatabase(Context context,
                                                                 ExpenseManager expenseManager){
        if(databaseHelper == null){
            databaseHelper = new DatabaseHelper(context, expenseManager);
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

    public ExpenseManager getExpenseManager(){
        return expenseManager;
    }

    public void insertData(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues expContent = new ContentValues();
        expContent.put(TITLE_FIELD, expense.getTitle());
        expContent.put(COST_FIELD, expense.getCost());
        expContent.put(TYPE_FIELD, String.valueOf(expense.getType()));
        expContent.put(DATE_FIELD, dateFormat.format(expense.getDate()));
        expContent.put(DESC_FIELD, expense.getDescription());
        db.insert(TABLE_NAME, null, expContent);
    }

    public void listAllExpenses(){
        expenseManager.getExpenseList().clear();
        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    String title = result.getString(result.getColumnIndexOrThrow(TITLE_FIELD));
                    double cost = result.getDouble(result.getColumnIndexOrThrow(COST_FIELD));
                    Type type = getTypeFromString(result
                            .getString(result.getColumnIndexOrThrow(TYPE_FIELD)));
                    Date date = dateFormat.parse(result.getString(result
                            .getColumnIndexOrThrow(DATE_FIELD)));
                    String desc = result.getString(result.getColumnIndexOrThrow(DESC_FIELD));
                    Expense expense = new Expense(title, cost, type, date, desc);
                    expense.setId(result.getLong(result.getColumnIndexOrThrow(ID_FIELD)));
                    expenseManager.addExpense(expense);
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateExpenseInDB(Expense expense){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_FIELD, expense.getTitle());
        contentValues.put(COST_FIELD, expense.getCost());
        contentValues.put(TYPE_FIELD, getStringFromType(expense.getType()));
        contentValues.put(DATE_FIELD, dateFormat.format(expense.getDate()));
        contentValues.put(DESC_FIELD, expense.getDescription());

        db.update(TABLE_NAME, contentValues,
                ID_FIELD + " =? ", new String[]{String.valueOf(expense.getId())});
    }

    private String getStringFromType(Type type){
        return type.toString();
    }
    private Type getTypeFromString(String type){
        return Type.valueOf(type);
    }

}
