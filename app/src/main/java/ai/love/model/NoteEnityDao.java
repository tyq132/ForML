package ai.love.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NOTE_ENITY".
*/
public class NoteEnityDao extends AbstractDao<NoteEnity, Long> {

    public static final String TABLENAME = "NOTE_ENITY";

    /**
     * Properties of entity NoteEnity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Time = new Property(3, java.util.Date.class, "time", false, "TIME");
        public final static Property Lable = new Property(4, String.class, "lable", false, "LABLE");
        public final static Property ImgResUrl = new Property(5, String.class, "imgResUrl", false, "IMG_RES_URL");
    }


    public NoteEnityDao(DaoConfig config) {
        super(config);
    }
    
    public NoteEnityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTE_ENITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: Id
                "\"TITLE\" TEXT," + // 1: title
                "\"CONTENT\" TEXT," + // 2: content
                "\"TIME\" INTEGER," + // 3: time
                "\"LABLE\" TEXT," + // 4: lable
                "\"IMG_RES_URL\" TEXT);"); // 5: imgResUrl
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTE_ENITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NoteEnity entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(4, time.getTime());
        }
 
        String lable = entity.getLable();
        if (lable != null) {
            stmt.bindString(5, lable);
        }
 
        String imgResUrl = entity.getImgResUrl();
        if (imgResUrl != null) {
            stmt.bindString(6, imgResUrl);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NoteEnity entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(4, time.getTime());
        }
 
        String lable = entity.getLable();
        if (lable != null) {
            stmt.bindString(5, lable);
        }
 
        String imgResUrl = entity.getImgResUrl();
        if (imgResUrl != null) {
            stmt.bindString(6, imgResUrl);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NoteEnity readEntity(Cursor cursor, int offset) {
        NoteEnity entity = new NoteEnity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // time
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // lable
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // imgResUrl
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NoteEnity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTime(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setLable(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setImgResUrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NoteEnity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NoteEnity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NoteEnity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
