// This class was generated from android.androidVNC.IMetaKey by a tool
// Do not edit this file directly! PLX THX
package android.vnc.android.androidVNC;

import android.androidVNC.IMetaKey;

public abstract class AbstractMetaKeyBean extends com.antlersoft.android.dbimpl.IdImplementationBase implements IMetaKey {

    public static final String GEN_TABLE_NAME = "META_KEY";
    public static final int GEN_COUNT = 8;

    // Field constants
    public static final String GEN_FIELD__ID = "_id";
    public static final int GEN_ID__ID = 0;
    public static final String GEN_FIELD_METALISTID = "METALISTID";
    public static final int GEN_ID_METALISTID = 1;
    public static final String GEN_FIELD_KEYDESC = "KEYDESC";
    public static final int GEN_ID_KEYDESC = 2;
    public static final String GEN_FIELD_METAFLAGS = "METAFLAGS";
    public static final int GEN_ID_METAFLAGS = 3;
    public static final String GEN_FIELD_MOUSECLICK = "MOUSECLICK";
    public static final int GEN_ID_MOUSECLICK = 4;
    public static final String GEN_FIELD_MOUSEBUTTONS = "MOUSEBUTTONS";
    public static final int GEN_ID_MOUSEBUTTONS = 5;
    public static final String GEN_FIELD_KEYSYM = "KEYSYM";
    public static final int GEN_ID_KEYSYM = 6;
    public static final String GEN_FIELD_SHORTCUT = "SHORTCUT";
    public static final int GEN_ID_SHORTCUT = 7;

    // SQL Command for creating the table
    public static String GEN_CREATE = "CREATE TABLE META_KEY (" +
    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
    "METALISTID INTEGER," +
    "KEYDESC TEXT," +
    "METAFLAGS INTEGER," +
    "MOUSECLICK INTEGER," +
    "MOUSEBUTTONS INTEGER," +
    "KEYSYM INTEGER," +
    "SHORTCUT TEXT" +
    ")";

    // Members corresponding to defined fields
    private long gen__Id;
    private long gen_metaListId;
    private java.lang.String gen_keyDesc;
    private int gen_metaFlags;
    private boolean gen_mouseClick;
    private int gen_mouseButtons;
    private int gen_keySym;
    private java.lang.String gen_shortcut;


    public String Gen_tableName() { return GEN_TABLE_NAME; }

    // Field accessors
    public long get_Id() { return gen__Id; }
    public void set_Id(long arg__Id) { gen__Id = arg__Id; }
    public long getMetaListId() { return gen_metaListId; }
    public void setMetaListId(long arg_metaListId) { gen_metaListId = arg_metaListId; }
    public java.lang.String getKeyDesc() { return gen_keyDesc; }
    public void setKeyDesc(java.lang.String arg_keyDesc) { gen_keyDesc = arg_keyDesc; }
    public int getMetaFlags() { return gen_metaFlags; }
    public void setMetaFlags(int arg_metaFlags) { gen_metaFlags = arg_metaFlags; }
    public boolean isMouseClick() { return gen_mouseClick; }
    public void setMouseClick(boolean arg_mouseClick) { gen_mouseClick = arg_mouseClick; }
    public int getMouseButtons() { return gen_mouseButtons; }
    public void setMouseButtons(int arg_mouseButtons) { gen_mouseButtons = arg_mouseButtons; }
    public int getKeySym() { return gen_keySym; }
    public void setKeySym(int arg_keySym) { gen_keySym = arg_keySym; }
    public java.lang.String getShortcut() { return gen_shortcut; }
    public void setShortcut(java.lang.String arg_shortcut) { gen_shortcut = arg_shortcut; }

    public android.content.ContentValues Gen_getValues() {
        android.content.ContentValues values=new android.content.ContentValues();
        values.put(GEN_FIELD__ID,Long.toString(this.gen__Id));
        values.put(GEN_FIELD_METALISTID,Long.toString(this.gen_metaListId));
        values.put(GEN_FIELD_KEYDESC,this.gen_keyDesc);
        values.put(GEN_FIELD_METAFLAGS,Integer.toString(this.gen_metaFlags));
        values.put(GEN_FIELD_MOUSECLICK,(this.gen_mouseClick ? "1" : "0"));
        values.put(GEN_FIELD_MOUSEBUTTONS,Integer.toString(this.gen_mouseButtons));
        values.put(GEN_FIELD_KEYSYM,Integer.toString(this.gen_keySym));
        values.put(GEN_FIELD_SHORTCUT,this.gen_shortcut);
        return values;
    }

    /**
     * Return an array that gives the column index in the cursor for each field defined
     * @param cursor Database cursor over some columns, possibly including this table
     * @return array of column indices; -1 if the column with that id is not in cursor
     */
    public int[] Gen_columnIndices(android.database.Cursor cursor) {
        int[] result=new int[GEN_COUNT];
        result[0] = cursor.getColumnIndex(GEN_FIELD__ID);
        // Make compatible with database generated by older version of plugin with uppercase column name
        if (result[0] == -1) {
            result[0] = cursor.getColumnIndex("_ID");
        }
        result[1] = cursor.getColumnIndex(GEN_FIELD_METALISTID);
        result[2] = cursor.getColumnIndex(GEN_FIELD_KEYDESC);
        result[3] = cursor.getColumnIndex(GEN_FIELD_METAFLAGS);
        result[4] = cursor.getColumnIndex(GEN_FIELD_MOUSECLICK);
        result[5] = cursor.getColumnIndex(GEN_FIELD_MOUSEBUTTONS);
        result[6] = cursor.getColumnIndex(GEN_FIELD_KEYSYM);
        result[7] = cursor.getColumnIndex(GEN_FIELD_SHORTCUT);
        return result;
    }

    /**
     * Populate one instance from a cursor 
     */
    public void Gen_populate(android.database.Cursor cursor,int[] columnIndices) {
        if ( columnIndices[GEN_ID__ID] >= 0 && ! cursor.isNull(columnIndices[GEN_ID__ID])) {
            gen__Id = cursor.getLong(columnIndices[GEN_ID__ID]);
        }
        if ( columnIndices[GEN_ID_METALISTID] >= 0 && ! cursor.isNull(columnIndices[GEN_ID_METALISTID])) {
            gen_metaListId = cursor.getLong(columnIndices[GEN_ID_METALISTID]);
        }
        if ( columnIndices[GEN_ID_KEYDESC] >= 0 && ! cursor.isNull(columnIndices[GEN_ID_KEYDESC])) {
            gen_keyDesc = cursor.getString(columnIndices[GEN_ID_KEYDESC]);
        }
        if ( columnIndices[GEN_ID_METAFLAGS] >= 0 && ! cursor.isNull(columnIndices[GEN_ID_METAFLAGS])) {
            gen_metaFlags = (int)cursor.getInt(columnIndices[GEN_ID_METAFLAGS]);
        }
        if ( columnIndices[GEN_ID_MOUSECLICK] >= 0 && ! cursor.isNull(columnIndices[GEN_ID_MOUSECLICK])) {
            gen_mouseClick = (cursor.getInt(columnIndices[GEN_ID_MOUSECLICK]) != 0);
        }
        if ( columnIndices[GEN_ID_MOUSEBUTTONS] >= 0 && ! cursor.isNull(columnIndices[GEN_ID_MOUSEBUTTONS])) {
            gen_mouseButtons = (int)cursor.getInt(columnIndices[GEN_ID_MOUSEBUTTONS]);
        }
        if ( columnIndices[GEN_ID_KEYSYM] >= 0 && ! cursor.isNull(columnIndices[GEN_ID_KEYSYM])) {
            gen_keySym = (int)cursor.getInt(columnIndices[GEN_ID_KEYSYM]);
        }
        if ( columnIndices[GEN_ID_SHORTCUT] >= 0 && ! cursor.isNull(columnIndices[GEN_ID_SHORTCUT])) {
            gen_shortcut = cursor.getString(columnIndices[GEN_ID_SHORTCUT]);
        }
    }

    /**
     * Populate one instance from a ContentValues 
     */
    public void Gen_populate(android.content.ContentValues values) {
        gen__Id = values.getAsLong(GEN_FIELD__ID);
        gen_metaListId = values.getAsLong(GEN_FIELD_METALISTID);
        gen_keyDesc = values.getAsString(GEN_FIELD_KEYDESC);
        gen_metaFlags = (int)values.getAsInteger(GEN_FIELD_METAFLAGS);
        gen_mouseClick = (values.getAsInteger(GEN_FIELD_MOUSECLICK) != 0);
        gen_mouseButtons = (int)values.getAsInteger(GEN_FIELD_MOUSEBUTTONS);
        gen_keySym = (int)values.getAsInteger(GEN_FIELD_KEYSYM);
        gen_shortcut = values.getAsString(GEN_FIELD_SHORTCUT);
    }
}
