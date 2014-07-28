package jim.acronym;

import android.content.ContentValues;
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
    //has one to many relationship with words table
    private static final String ACRONYM_TABLE_NAME = "acronyms";
    private static final String ACRONYM_TABLE_KEY_ID = "id";
    private static final String ACRONYM_TABLE_KEY_ACRONYM = "acr";
    private static final String ACRONYM_TABLE_KEY_DESCRIPTION = "description";
    private static final String ACRONYM_CREATE_TABLE = "CREATE TABLE " + ACRONYM_TABLE_NAME + " (" +
            ACRONYM_TABLE_KEY_ID + " INTEGER PRIMARY KEY ASC, " +
            ACRONYM_TABLE_KEY_ACRONYM + " TEXT UNIQUE, " +
            ACRONYM_TABLE_KEY_DESCRIPTION + " TEXT); ";

    //WORD TABLE CREATE AND KEYS
    //has many to many relationship with acronyms
    private static final String WORD_TABLE_NAME = "words";
    private static final String WORD_TABLE_KEY_ID = "id";
    private static final String WORD_TABLE_KEY_WORD = "word";
    private static final String WORD_TABLE_KEY_DEFINITION = "definition";
    private static final String WORD_TABLE_KEY_FIRST_LETTER = "first_letter";
    private static final String WORD_CREATE_TABLE = "CREATE TABLE " + WORD_TABLE_NAME + " (" +
            WORD_TABLE_KEY_ID + " INTEGER PRIMARY KEY ASC, " +
            WORD_TABLE_KEY_WORD + " TEXT UNIQUE, " +
            WORD_TABLE_KEY_FIRST_LETTER + " CHARACTER(1), " +
            WORD_TABLE_KEY_DEFINITION + " TEXT); ";

    //BINDINGS CREATE AND KEYS
    //facilitates the many to one and many to many relationship between acronyms and words
    private static final String BINDINGS_TABLE_NAME = "bindings";
    private static final String BINDINGS_TABLE_ID = "id";
    private static final String BINDINGS_TABLE_FOREIGN_KEY_ACRONYM = "acr";
    private static final String BINDINGS_TABLE_FOREIGN_KEY_WORD = "word";
    private static final String BINDINGS_CREATE_TABLE = "CREATE TABLE " + BINDINGS_TABLE_NAME + " (" +
            BINDINGS_TABLE_ID + " INTEGER PRIMARY KEY ASC, " +
            BINDINGS_TABLE_FOREIGN_KEY_ACRONYM + " INTEGER, " +
            BINDINGS_TABLE_FOREIGN_KEY_WORD + " INTEGER, " +
            "FOREIGN KEY(" + BINDINGS_TABLE_FOREIGN_KEY_ACRONYM +") REFERENCES " +
            ACRONYM_TABLE_NAME + "(" + ACRONYM_TABLE_KEY_ID + "), " +
            "FOREIGN KEY(" + BINDINGS_TABLE_FOREIGN_KEY_WORD +") REFERENCES " +
            WORD_TABLE_NAME + "(" + WORD_TABLE_KEY_ID +")); ";

    public AcronymDatabase(Context context) {
        super(context, ACRONYM_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ACRONYM_CREATE_TABLE);
        sqLiteDatabase.execSQL(WORD_CREATE_TABLE);
        sqLiteDatabase.execSQL(BINDINGS_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    /**
     * inserts a word into the database
     * @param word the word to be insert
     * @return the id of the inserted row or -1 if an error occurred
     */
    public long insertWord(Word word){
        ContentValues vals = new ContentValues();
        vals.put(WORD_TABLE_KEY_WORD, word.word);
        vals.put(WORD_TABLE_KEY_FIRST_LETTER, word.firstLetter);
        vals.put(WORD_TABLE_KEY_DEFINITION, word.definition);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(WORD_TABLE_NAME, null, vals);
    }

    /**
     * inserts an acronym into the database
     * @param acr the Acronym to be inserted
     * @return the id of the inserted row or -1 if an error occurred
     */
    public long insertAcronym(Acronym acr){
        return -1;
    }

    /**
     * inserts a binding into the database, this associates a word with an acronym
     * @param acr the Acronym to be bound to
     * @param word the Word that will be associated with the acr
     * @return
     */
    public long makeBinding(Acronym acr, Word word) {
        return -1;
    }

    /**
     * gets a word from the database
     * @param word the word used to recover the Word from the database
     * @return the new Word object or null if word did not match anything
     */
    public Word getWord(String word){
        return null;
    }

    /**
     * given the string acronym, recovers all words associated with that acronym
     * @param acr the string to be used to recover the Words
     * @return an array of Words that are associated with acr, or null if an error occurred
     */
    public Word[] getWordsFromAcr(String acr) {
        return null;
    }


    /**
     * gets an acronym from the database
     * @param acr the acronym to be used to recover the Acronym from the database
     * @return the new Acronym object or null if acr did not match anything
     */
    public Acronym getAcronym(String acr){
        return null;
    }

    /**
     * given a word, recovers all acronyms associated with that word
     * @param word the string to be used to recover the Acronyms
     * @return an array of Acronyms that are associated with word, or null if an error occurred
     */
    public Acronym[] getAcrsFromWord(Word word) {
        return null;
    }
}
