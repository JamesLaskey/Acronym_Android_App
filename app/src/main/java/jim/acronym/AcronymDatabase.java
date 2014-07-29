package jim.acronym;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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
            ACRONYM_TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
            WORD_TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
            BINDINGS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
        ContentValues vals = new ContentValues();
        vals.put(ACRONYM_TABLE_KEY_ACRONYM, acr.acronym);
        vals.put(ACRONYM_TABLE_KEY_DESCRIPTION, acr.desc);
        SQLiteDatabase db = getWritableDatabase();
        long acronymID = db.insert(ACRONYM_TABLE_NAME, null, vals);
        if(acronymID == -1) {
            return -1;
        }

        for(Word word : acr.words) {
            long wordID = insertWord(word);
            if(wordID == -1){
                return -1;
            }

            if(makeBinding(acronymID, wordID, db) == -1){
                return -1;
            }
        }

        return acronymID;
    }

    /**
     * inserts a binding into the database, this associates a word with an acronym
     * @param acrID the id of the Acronym to be bound to
     * @param wordID the id of the Word that will be associated with the acr
     * @param db, a db to insert the binding into
     * @return the id of the Binding
     */
    public long makeBinding(long acrID, long wordID, SQLiteDatabase db) {
        ContentValues bVal = new ContentValues();
        bVal.put(BINDINGS_TABLE_FOREIGN_KEY_ACRONYM, acrID);
        bVal.put(BINDINGS_TABLE_FOREIGN_KEY_WORD, wordID);
        return db.insert(BINDINGS_TABLE_NAME, null, bVal);
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
        SQLiteDatabase db = getReadableDatabase();
        Cursor acrCursor = db.query(ACRONYM_TABLE_NAME, null, ACRONYM_TABLE_KEY_ACRONYM + "=" + acr, null, null, null, null);
        acrCursor.moveToNext();
        int acrID = acrCursor.getInt(0);

        Cursor bindingCursor = db.query(BINDINGS_TABLE_NAME, null, BINDINGS_TABLE_FOREIGN_KEY_ACRONYM + "=" + acrID, null, null, null, null);
        bindingCursor.moveToNext();

        ArrayList<Word> words = new ArrayList<Word>();

        for(int i = 0; i < bindingCursor.getCount(); i++) {
            Cursor wordCursor = db.query(WORD_TABLE_NAME, null, WORD_TABLE_KEY_ID + "=" + bindingCursor.getInt(2), null, null, null, null);
            wordCursor.moveToNext();
            Word word = new Word(wordCursor.getString(1), wordCursor.getString(2), wordCursor.getString(3));
            words.add(word);
            bindingCursor.moveToNext();
        }

        Word[] ret = new Word[words.size()];
        words.toArray(ret);
        return ret;
    }

    /**
     * given the string acronym, recovers all words associated with that acronym
     * @param acrID the id of the acronym to be used to recover the Words
     * @return an array of Words that are associated with acr, or null if an error occurred
     */
    public Word[] getWordsFromAcr(int acrID) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor bindingCursor = db.query(BINDINGS_TABLE_NAME, null, BINDINGS_TABLE_FOREIGN_KEY_ACRONYM + "=" + acrID, null, null, null, null);
        bindingCursor.moveToNext();

        ArrayList<Word> words = new ArrayList<Word>();

        for(int i = 0; i < bindingCursor.getCount(); i++) {
            Cursor wordCursor = db.query(WORD_TABLE_NAME, null, WORD_TABLE_KEY_ID + "=" + bindingCursor.getInt(2), null, null, null, null);
            wordCursor.moveToNext();
            Word word = new Word(wordCursor.getString(1), wordCursor.getString(2), wordCursor.getString(3));
            words.add(word);
            bindingCursor.moveToNext();
        }

        Word[] ret = new Word[words.size()];
        words.toArray(ret);
        return ret;
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


    private int lastAcronymIDOffsetRead = -1;
    /**
     * gets limit number of Acronym objects from the database starting from offset
     * @param offset
     * @param limit
     * @return
     */
    public ArrayList<Acronym> getAllAcronyms(int offset, int limit) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ACRONYM_TABLE_NAME, null, ACRONYM_TABLE_KEY_ID + " > " + offset, null, null, null, ACRONYM_TABLE_KEY_ACRONYM + " ASC", "" + limit);
        cursor.moveToNext();
        ArrayList<Acronym> acronyms = new ArrayList<Acronym>();
        for(int i = 0; i < cursor.getCount(); i++) {
            Word[] words = getWordsFromAcr(cursor.getInt(0));
            acronyms.add(new Acronym(cursor.getString(1), words, cursor.getString(2)));
            cursor.moveToNext();
        }

        cursor.moveToLast();
        //lastAcronymIDOffsetRead = cursor.getInt(0);
        return acronyms;
    }

    /**
     * gets the id of the last acronym in the database read off from getAllAcronyms, allowing accurate positioning of offset on future calls
     * @return id of the last acronym returned by getAllAcronyms()
     */
    public int getLastAcronymOffset() {
        return lastAcronymIDOffsetRead;
    }
}
