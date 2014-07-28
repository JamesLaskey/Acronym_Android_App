package jim.acronym;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jim on 7/27/14.
 */
public class AcronymDatabase extends SQLiteOpenHelper {

    //GENERAL DATABASE DATA
    private static final int DATABASE_VERSION = 1;
    private static final String ACRONYM_DATABASE_NAME = "acronym";

    //ACRONYM TABLE AND KEYS
    private static final String ACRONYM_TABLE_ID = "id";
    private static final String ACRONYM_TABLE_NAME = "acronyms";
    private static final String ACRONYM_TABLE_KEY_ACRONYM = "acr";
    private static final String ACRONYM_TABLE_KEY_DESCRIPTION = "description";
    private static final String ACRONYM_CREATE_TABLE = "CREATE TABLE " + ACRONYM_TABLE_NAME + " (" +
            ACRONYM_TABLE_ID + " INTEGER PRIMARY KEY ASC, " +
            ACRONYM_TABLE_KEY_ACRONYM + " TEXT UNIQUE, " +
            ACRONYM_TABLE_KEY_DESCRIPTION + " TEXT); ";

    //WORD TABLE CREATE AND KEYS
    private static final String WORD_TABLE_ID = "id";
    private static final String WORD_TABLE_NAME = "acronyms";
    private static final String WORD_TABLE_FOREIGN_KEY_ACRONYM = "acr";
    private static final String WORD_TABLE_KEY_WORD = "word";
    private static final String WORD_TABLE_KEY_DEFINITION = "definition";
    private static final String WORD_TABLE_KEY_FIRST_LETTER = "first_letter";
    private static final String WORD_CREATE_TABLE = "CREATE TABLE " + WORD_TABLE_NAME + " (" +
            WORD_TABLE_ID + " INTEGER PRIMARY KEY ASC, " +
            WORD_TABLE_FOREIGN_KEY_ACRONYM + " TEXT, " +
            WORD_TABLE_KEY_WORD + " TEXT UNIQUE, " +
            WORD_TABLE_KEY_FIRST_LETTER + " CHARACTER(1), " +
            WORD_TABLE_KEY_DEFINITION + " TEXT); ";


    public AcronymDatabase(Context context) {
        super(context, ACRONYM_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ACRONYM_CREATE_TABLE);
        sqLiteDatabase.execSQL(WORD_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public boolean insertWord(Word word){
        return false;
    }

    public boolean insertAcronym(Acronym acr){
        return false;
    }

    public Word getWord(String word){
        return null;
    }

    public Acronym getAcronym(String acr){
        return null;
    }
}
