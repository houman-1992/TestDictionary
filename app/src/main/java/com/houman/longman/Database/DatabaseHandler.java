package com.houman.longman.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.houman.longman.Models.MainModel;
import com.houman.longman.Models.mMemorize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
    {

    private String Path;
    private SQLiteDatabase Database;
    private Context mContext;
    private static final String dbMainName = "LongMan.db";
    private static final int ZeroColumn = 0;
    private static final int FirstColumn = 1;
    private static final int SecondColumn = 2;
    private static final int ThirdColumn = 3;
    private static final int FourthColumn = 4;

    private static int Limitation = 50;

    public DatabaseHandler(Context context)
        {
        super(context, dbMainName, null, 1);
        mContext = context;
        Path = context.getApplicationInfo().dataDir + "/databases/" + dbMainName;
        }

    @Override
    public void onCreate(SQLiteDatabase db)
        {
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        }

    private void openDatabase()
        {
        if (Database != null && Database.isOpen())
            return;
        Database = SQLiteDatabase.openDatabase(Path, null, SQLiteDatabase.OPEN_READWRITE);
        }

    private void closeDatabase()
        {
        if (Database != null)
            Database.close();
        }

    @SuppressLint("Recycle")
    public List<MainModel> getLongMan(String Search)
        {
        List<MainModel> Result = new ArrayList<>();
        openDatabase();
        if (! Database.isOpen())
            return null;
        try
            {
            String Query;
            Query = "SELECT id FROM searchinflections WHERE InflectionWords = '" + Search + "'";
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            if (cursor.getCount() > 0)
                {
                cursor.moveToFirst();
                String id = cursor.getString(ZeroColumn);
                Query = "SELECT HWD FROM core WHERE id = '" + id + "'";
                cursor = Database.rawQuery(Query, null);
                if (cursor.getCount() > 0)
                    {
                    cursor.moveToFirst();
                    String hwd = cursor.getString(ZeroColumn);
                    String fileName = USFileName(id);
                    Result.add(new MainModel(hwd, fileName));
                    }
                }
            //*************************************************
            Query = "SELECT HWD,id FROM core WHERE HWD LIKE '" + Search + "%' LIMIT " + Limitation;
            cursor = Database.rawQuery(Query, null);
            String prev = "";
            if (cursor.getCount() > 0)
                {
                cursor.moveToFirst();
                while (! cursor.isAfterLast())
                    {
                    String hwd, id, fileName;
                    hwd = cursor.getString(ZeroColumn);
                    if (! prev.equals(hwd))
                        {
                        id = cursor.getString(FirstColumn);
                        fileName = USFileName(id);
                        Result.add(new MainModel(hwd, fileName));
                        prev = hwd;
                        }
                    cursor.moveToNext();
                    }
                }
            cursor.close();
            closeDatabase();
            return Result;
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            return null;
            }
        }

    private String USFileName(String Id)
        {
        String Result = "";
        if (! Database.isOpen())
            return Result;
        try
            {
            String Query = "SELECT filename FROM ussound WHERE id = '" + Id + "'";
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            cursor.moveToFirst();
            Result = cursor.getString(ZeroColumn);
            cursor.close();
            return Result;
            }
        catch (Exception e)
            {
            e.printStackTrace();
            return null;
            }
        }

    public String getAmePronName(String Id)
        {
        openDatabase();
        if (! Database.isOpen())
            return "";
        String result = USFileName(Id);
        closeDatabase();
        return result;
        }

    public String getExaPronName(String id)
        {
        String Result = "";
        openDatabase();
        if (! Database.isOpen())
            return Result;
        try
            {
            String Query = "SELECT filename FROM exasound WHERE id = '" + id + "'";
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            cursor.moveToFirst();
            Result = cursor.getString(ZeroColumn);
            cursor.close();
            closeDatabase();
            return Result;
            }
        catch (Exception e)
            {
            e.printStackTrace();
            closeDatabase();
            return null;
            }
        }

//    public String getWordFa(String Search)
//        {
//        openDatabase();
//        if (! Database.isOpen())
//            {
//            return "";
//            }
//        try
//            {
//            String Query = "SELECT wmean FROM word WHERE wname = '" + Search + "' LIMIT 1";
//            Cursor cursor;
//            cursor = Database.rawQuery(Query, null);
//            cursor.moveToFirst();
//            String buff = cursor.getString(ZeroColumn);
//            cursor.close();
//            closeDatabase();
//            return buff;
//            }
//        catch (Exception e)
//            {
//            e.printStackTrace();
//            return "";
//            }
//        }

    public List<MainModel> getWordEn(String Search)
        {

        List<MainModel> Result = new ArrayList<>();
        openDatabase();
        if (! Database.isOpen())
            return Result;
        try
            {
            String Query = "SELECT CORE,id FROM core WHERE HWD = '" + Search + "' LIMIT 6";
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);

            if (cursor.getCount() > 0)
                {
                cursor.moveToFirst();
                while (! cursor.isAfterLast())
                    {//todo creat a model for this
                    String Core = cursor.getString(ZeroColumn);
                    String id = cursor.getString(FirstColumn);
                    new MainModel(Core, id);
                    Result.add(new MainModel(Core, id));
                    cursor.moveToNext();
                    }
                }

            cursor.close();
            closeDatabase();
            return Result;
            }
        catch (Exception e)
            {
            e.printStackTrace();
            closeDatabase();
            return Result;
            }
        }

    @SuppressLint("Recycle")
    public String getWordRoot(String fWord)
        {
        openDatabase();
        if (! Database.isOpen())
            return null;
        try
            {
            String Query;
            Query = "SELECT hwd FROM searchinflections WHERE InflectionWords = '" + fWord + "'";
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            if (cursor.getCount() > 0)
                {
                cursor.moveToFirst();
                return cursor.getString(ZeroColumn);
                }
            else
                return fWord;

            }
        catch (Exception e)
            {
            e.printStackTrace();
            closeDatabase();
            return null;
            }
        }

    public String getUSFileName(String Text)
        {
        String Result = "";
        openDatabase();
        if (! Database.isOpen())
            return Result;
        try
            {
            String Query = "SELECT filename FROM ussound WHERE text = '" + Text + "'";
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            if (cursor.getCount() > 0)
                {
                cursor.moveToFirst();
                Result = cursor.getString(ZeroColumn);
                }
            cursor.close();
            closeDatabase();
            return Result;
            }
        catch (Exception e)
            {
            e.printStackTrace();
            closeDatabase();
            return "";
            }
        }

    public void markWord(String Word, boolean mark)
        {
        openDatabase();
        if (! Database.isOpen())
            return;
        try
            {
            String Query;
            if (mark)
                Query = "INSERT INTO marked VALUES " + "('" + Word + "')";
            else
                Query = "DELETE FROM marked WHERE hwd = '" + Word + "'";

            Database.execSQL(Query);
            closeDatabase();
            }
        catch (Exception e)
            {
            e.printStackTrace();
            closeDatabase();
            }
        }

    public List<String> getMarked(String search)
        {
        List<String> Result = new ArrayList<>();
        openDatabase();
        if (! Database.isOpen())
            return null;
        try
            {
            String Query;
            Query = "SELECT hwd FROM marked WHERE hwd like '" + search + "%'";
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            if (cursor.getCount() > 0)
                {
                cursor.moveToFirst();
                while (! cursor.isAfterLast())
                    {
                    Result.add(cursor.getString(ZeroColumn));
                    cursor.moveToNext();
                    }
                }
            cursor.close();
            closeDatabase();
            return Result;
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            return null;
            }
        }

    public void RecentDelete(String Word)
        {
        openDatabase();
        if (! Database.isOpen())
            return;
        try
            {
            String Query = "DELETE FROM recent WHERE hwd = '" + Word + "'";
            Database.execSQL(Query);
            closeDatabase();
            }
        catch (Exception e)
            {
            e.printStackTrace();
            closeDatabase();
            }
        }

    public void RecentDeleteAll()
        {
        openDatabase();
        if (! Database.isOpen())
            return;
        try
            {
            String Query = "DELETE FROM recent";
            Database.execSQL(Query);
            closeDatabase();
            }
        catch (Exception e)
            {
            e.printStackTrace();
            closeDatabase();
            }
        }

    public void addRecently(String Word)
        {
        openDatabase();
        if (! Database.isOpen())
            return;
        try
            {
            String Query = "INSERT INTO recent VALUES " + "('" + Word + "')";

            Database.execSQL(Query);
            closeDatabase();
            }
        catch (Exception e)
            {
            e.printStackTrace();
            closeDatabase();
            }
        }

    public List<String> getRecent(String search)
        {
        List<String> Result = new ArrayList<>();
        openDatabase();
        if (! Database.isOpen())
            return null;
        try
            {
            String Query;
            Query = "SELECT hwd FROM recent WHERE hwd like '" + search + "%'";
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            if (cursor.getCount() > 0)
                {
                cursor.moveToFirst();
                while (! cursor.isAfterLast())
                    {
                    Result.add(cursor.getString(ZeroColumn));
                    cursor.moveToNext();
                    }
                }
            cursor.close();
            closeDatabase();
            return Result;
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            return null;
            }
        }

    public void MarkedDeleteAll()
        {
        openDatabase();
        if (! Database.isOpen())
            return;
        try
            {
            String Query = "DELETE FROM marked";
            Database.execSQL(Query);
            closeDatabase();
            }
        catch (Exception e)
            {
            e.printStackTrace();
            closeDatabase();
            }
        }

    public boolean CheckMark(String tempValue)
        {
        openDatabase();
        if (! Database.isOpen())
            return false;
        try
            {
            boolean result = false;
            String Query = "SELECT hwd FROM marked WHERE hwd = '" + tempValue + "'";
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            if (cursor.getCount() > 0)
                result = true;
            cursor.close();
            closeDatabase();
            return result;
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            return false;
            }
        }

    @SuppressLint("Recycle")
    public void CreateTable(String TableName)
        {
        final String QueryCreateTable = "CREATE TABLE '#' ('Word' TEXT NOT NULL, 'Mean' " +
                "TEXT, 'Step' TEXT, 'Right' TEXT, 'Wrong' TEXT, PRIMARY KEY ('Word'))";
        final String QueryAddItems = "INSERT INTO TablesName VALUES('#')";

        openDatabase();
        if (! Database.isOpen())
            return;
        try
            {
            String Query = QueryCreateTable.replace("#", TableName);
            Database.execSQL(Query);
            Query = QueryAddItems.replace("#", TableName);
            Database.execSQL(Query);
            closeDatabase();
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            }
        }

    public void AddVocab(String TableName, List<String> vocabList)
        {
        final String QueryAddItems = "INSERT INTO '" + TableName + "' ('Word') VALUES ('#')";

        openDatabase();
        if (! Database.isOpen())
            return;
        try
            {
            String Query;
            for (int i = 0; i < vocabList.size(); i++)
                {
                String Buff = vocabList.get(i);
                Query = QueryAddItems.replace("#", Buff);
                Database.execSQL(Query);
                }
            closeDatabase();
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            }
        }

    public List<String> getTablesName(String Search)
        {
        final String baseQuery = "SELECT Name FROM TablesName WHERE Name LIKE '#%'";
        List<String> Result = new ArrayList<>();
        openDatabase();
        if (! Database.isOpen())
            return null;
        try
            {
            String Query;
            Query = baseQuery.replace("#", Search);
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            cursor.moveToFirst();
            while (! cursor.isAfterLast())
                {
                Result.add(cursor.getString(ZeroColumn));
                cursor.moveToNext();
                }
            cursor.close();
            return Result;
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            return null;
            }
        }

    public List<String> getFlashCardItems(String TableName)
        {
        final String baseQuery = "SELECT Word FROM '#'";
        List<String> Result = new ArrayList<>();
        openDatabase();
        if (! Database.isOpen())
            return null;
        try
            {
            String Query;
            Query = baseQuery.replace("#", TableName);
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            cursor.moveToFirst();
            while (! cursor.isAfterLast())
                {
                Result.add(cursor.getString(ZeroColumn));
                cursor.moveToNext();
                }
            cursor.close();
            return Result;
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            return null;
            }
        }

    public void DeleteFlash(String TableName)
        {
        final String baseQuery1 = "DELETE FROM TablesName WHERE Name = '#'";
        final String baseQuery2 = "DROP TABLE '#'";
        openDatabase();
        if (! Database.isOpen())
            return ;
        try
            {
            String Query;
            Query = baseQuery1.replace("#", TableName);
            Database.execSQL(Query);
            Query = baseQuery2.replace("#", TableName);
            Database.execSQL(Query);
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            }
        }

    public void DeleteFlashItem(String TableName, String Item)
        {
        final String baseQuery = "DELETE FROM $ WHERE Word = '#'";
        openDatabase();
        if (! Database.isOpen())
            return ;
        try
            {
            String Query;
            Query = baseQuery.replace("$", TableName).replace("#",Item);
            Database.execSQL(Query);
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            }
        }

    public void AddMemorizeBox(String cate, String date)
        {
        String QueryAddItems = "INSERT INTO 'memorizeBox' ('Word','Date','Step','Cate') VALUES ('#','" + date + "','1','" + cate + "')";
        List<String> VocabList = new ArrayList<>(getFlashCardItems(cate));
        openDatabase();
        if (! Database.isOpen())
            return;
        try
            {
            String Query;
            for (int i = 0; i < VocabList.size(); i++)
                {
                String Buff = VocabList.get(i);
                Query = QueryAddItems.replace("#", Buff);
                Database.execSQL(Query);
                }
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            }
        }

    public List<mMemorize> getVocabMemoriseBox()
        {
        final String Query = "SELECT * FROM 'memorizeBox'";
        List<mMemorize> Result = new ArrayList<>();
        openDatabase();
        if (! Database.isOpen())
            return null;
        try
            {
            Cursor cursor;
            cursor = Database.rawQuery(Query, null);
            cursor.moveToFirst();
            while (! cursor.isAfterLast())
                {
                Result.add(new mMemorize(cursor.getString(ZeroColumn), cursor.getString(FirstColumn),cursor.getInt(SecondColumn),
                        cursor.getString(ThirdColumn)));
                cursor.moveToNext();
                }
            cursor.close();
            return Result;
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            return null;
            }
        }

    public void UpdateMemorizeBox(mMemorize mMemorize)
        {
        String word, date;
        int step;
        word = mMemorize.getWord();
        date = mMemorize.getDate();
        step = mMemorize.getStep();
        String Query = "UPDATE memorizeBox SET Date = '" + date + "', Step = '" + step + "' WHERE Word = '" + word + "'";
        openDatabase();
        if (! Database.isOpen())
            return;
        try
            {
            Database.execSQL(Query);
            }
        catch (Exception e)
            {
            closeDatabase();
            e.printStackTrace();
            }
        }
    }
