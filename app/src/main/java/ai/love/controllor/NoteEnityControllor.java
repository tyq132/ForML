package ai.love.controllor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import ai.love.model.DaoMaster;
import ai.love.model.DaoSession;
import ai.love.model.NoteEnity;
import ai.love.model.NoteEnityDao;


/**
 * Created by James Tang on 2020/10/2
 */
public class NoteEnityControllor {

    public static Long tempId = -1L;
    /**
     * Helper
     */
    private DaoMaster.DevOpenHelper mHelper;//获取Helper对象
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * DaoMaster
     */
    private DaoMaster mDaoMaster;
    /**
     * DaoSession
     */
    private DaoSession mDaoSession;
    /**
     * 上下文
     */
    private Context context;
    /**
     * dao
     */
    private NoteEnityDao noteDao;

    private static NoteEnityControllor mDbController;

    /**
     * 获取单例
     */
    public static NoteEnityControllor getInstance(Context context) {
        if (mDbController == null) {
            synchronized (NoteEnityControllor.class) {
                if (mDbController == null) {
                    mDbController = new NoteEnityControllor(context);
                }
            }
        }
        return mDbController;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public NoteEnityControllor(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        mDaoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        noteDao = mDaoSession.getNoteEnityDao();
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     *
     * @return
     */
    private SQLiteDatabase getWritableDatabase() {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db;
    }

    /**
     * 会自动判定是插入还是替换
     *
     * @param enity
     */
    public void insertOrReplace(NoteEnity enity) {
        noteDao.insertOrReplace(enity);
    }

    /**
     * 插入一条记录，表里面要没有与之相同的记录
     *
     * @param enity
     */
    public long insert(NoteEnity enity) {
        return noteDao.insert(enity);
    }

    /**
     * 更新数据
     *
     * @param enity
     */
    public void update(Long id, NoteEnity enity) {
        NoteEnity mOldNoteEnity = noteDao.queryBuilder().where(NoteEnityDao.Properties.Id.eq(id)).build().unique();//拿到之前的记录
        if (mOldNoteEnity != null) {
            mOldNoteEnity.setContent(enity.getContent());
            mOldNoteEnity.setLable(enity.getLable());
            mOldNoteEnity.setTime(enity.getTime());
            mOldNoteEnity.setTitle(enity.getTitle());
            noteDao.update(mOldNoteEnity);
        }
    }

    /**
     * 按条件(title)查询数据
     */
    public List<NoteEnity> searchByTitle(String title) {
        List<NoteEnity> enitys = (List<NoteEnity>) noteDao.queryBuilder().where(NoteEnityDao.Properties.Title.eq(title)).build().list();
        return enitys;
    }

    /**
     * 按条件(content)查询数据
     */
    public List<NoteEnity> searchByContent(String content) {
        List<NoteEnity> enitys = (List<NoteEnity>) noteDao.queryBuilder().where(NoteEnityDao.Properties.Content.in(content)).build().list();
        return enitys;
    }

    /**
     * 按条件(content)查询数据
     */
    public NoteEnity searchById (Long id) {
        NoteEnity enity = noteDao.queryBuilder().where(NoteEnityDao.Properties.Id.eq(id)).build().unique();
        return enity;
    }
    /**
     * 查询所有数据
     */
    public List<NoteEnity> searchAll() {
        return noteDao.queryBuilder().build().list();
    }

    /**
     * 删除数据
     */
    public void delete(Long id) {
        noteDao.queryBuilder().where(NoteEnityDao.Properties.Id.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
