package ai.love.utils;

import android.content.Context;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by James Tang on 2020/8/23
 */
public class DiskLruCacheUtil {

    private DiskLruCache mDiskCache;
    private String cacheDir;

    public DiskLruCacheUtil(Context context){
        cacheDir = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||!isExternalStorageRemovable()
                ? context.getExternalCacheDir().getPath()
                : context.getCacheDir().getPath();
        try {
            mDiskCache = DiskLruCache.open(new File(cacheDir),1, 1, 1024 * 1024 * 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param key key
     * @param value value
     * @return 是否添加成功
     */
    public boolean addCache(String key, String value){
        try {
            DiskLruCache.Editor editor = mDiskCache.edit(key);
            editor.newOutputStream(0).write(value.getBytes());
            editor.commit();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param key
     * @return key对应的值
     */
    public String getCache(String key){

        try {
            Object temp = mDiskCache.get(key);
            if(temp==null){
                return "NONE";
            }else{
                return mDiskCache.get(key).getString(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "NONE";
        }
    }

    public void closeCache() {
        try {
            mDiskCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
