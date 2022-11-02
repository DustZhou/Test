package com.mdrj.wlt.utils;

import android.content.ContentValues;

import com.mdrj.wlt.APP;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteException;
import com.tencent.wcdb.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Wesley on 2017-01-05.
 */

public class AppDataDBHelper extends SQLiteOpenHelper {

    /**DBHelper实例对象*/
    private static AppDataDBHelper mInstance = null;
    /**数据库DB_NAME文件名包含路径*/
    private static String DB_NAME = "DB_NAME.db";
    /** DB对象 **/
    private static SQLiteDatabase mDb = null;
    /**数据库版本*/
    private static final int DB_VERSION = 1;




    //***************************DBHelper方法***************************

    /**
     * 初始数据库文件
     * 第一次赋值，切换数据库赋值
     * @param dbNAME：包含路径数据库文件名
     */
    public static void InitDBNAME(String dbNAME) {
        if (mDb!=null){
//            mDb.close();
        }
        mInstance = null;
        DB_NAME=dbNAME;
    }

    /**
     * 得到数据库对象
     * @throws SQLiteException
     */
    public AppDataDBHelper() throws SQLiteException {
        super(APP.Companion.getInstance(),DB_NAME, null, DB_VERSION);
        // 得到数据库对象
        try {
            mDb = getWritableDatabase();
        } catch (Exception e) {
//            mDb=getReadableDatabase();
        }
    }

    /**
     * 单例模式
     */
    public static AppDataDBHelper getInstance() {
        if (mInstance == null) {
            synchronized (AppDataDBHelper.class) {
                if (mInstance == null) {
                    mInstance = new AppDataDBHelper();
                }
            }
            return mInstance;
        }
        return null;
    }

    public static SQLiteDatabase getDatabase(){
        return mDb;
    }

    /**
     * onCreate
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //新建用户配置表
        db.beginTransaction();
        db.execSQL("CREATE TABLE UserConfig(\n" +
                "[SysID] BIGINT IDENTITY(1,1) NOT NULL,--自增长ID\n" +
                "[ZTCode] VARCHAR(100) DEFAULT '', --账套Code\n" +
                "[LoginID] VARCHAR(100) DEFAULT '', --操作员ID\n" +
                "[CfgName] VARCHAR(100) DEFAULT '',--配置键名\n" +
                "[CfgValue] VARCHAR(2000) DEFAULT '',--配置键值\n" +
                "[XiwaMaxTime] VARCHAR(50) DEFAULT '',--最后更新时间（手机设置后写时间、在线更新后写接口给的时间）：yyyy-MM-dd HH:mm:ss.fff\n" +
                "[AppModify] INT DEFAULT 0,--App更新修改：0未修改（无需上次），1修改过（需要上传）\n" +
                "[Comment] VARCHAR(1000) DEFAULT '',--备注内容\n" +
                "PRIMARY KEY(ZTCode,LoginID,CfgName) --复核主键\n" +
                ")");
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * onUpgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(DROP_TABLE_LOGIN);
    }

    //***************************query查询方法***************************
    /**
     * 查询返回List
     * @param sql
     * @return List<Map>
     */
    public synchronized List<HashMap> queryListMap(String sql){
        ArrayList list = new ArrayList();

        Cursor Cur = null;
        mDb.beginTransaction();
        try {
            Cur=mDb.rawQuery(sql, null);
            mDb.setTransactionSuccessful();
            int columnCount = Cur.getColumnCount();
            while (Cur.moveToNext()){
                HashMap item = new HashMap();
                for (int i=0;i<columnCount;++i){
                    item.put(Cur.getColumnName(i),Cur.getString(i));
                   /* int type = Cur.getType(i);
                    switch (type){
                        case 0:
                            item.put(Cur.getColumnName(i),null);
                            break;
                        case 1:
                            item.put(Cur.getColumnName(i), Cur.getInt(i));
                            break;
                        case 2:
                            item.put(Cur.getColumnName(i),Cur.getFloat(i));
                            break;
                        case 3:
                            item.put(Cur.getColumnName(i),Cur.getString(i));
                            break;
                    }*/
                }
                list.add(item);
            }
            Cur.close();
        } catch (Exception e) {
        }finally{
            mDb.endTransaction();

        }
        return list;
    }

    /**
     * 查询单条数据返回map
     * @param sql
     * @return Map
     */
    public synchronized HashMap queryItemMap(String sql){
        HashMap map = new HashMap();
        Cursor Cur = null;
        mDb.beginTransaction();
        try {
            Cur=mDb.rawQuery(sql, null);
            mDb.setTransactionSuccessful();

            if (Cur.moveToNext()){
                for (int i = 0;i < Cur.getColumnCount();++i){
                    map.put(Cur.getColumnName(i),Cur.getString(i));
                    /*int type = Cur.getType(i);
                    switch (type){
                        case 0:
                            com.map.put(Cur.getColumnName(i),null);
                            break;
                        case 1:
                            com.map.put(Cur.getColumnName(i),Cur.getInt(i));
                            break;
                        case 2:
                            com.map.put(Cur.getColumnName(i),Cur.getFloat(i));
                            break;
                        case 3:
                            com.map.put(Cur.getColumnName(i),Cur.getString(i));
                            break;
                    }*/
                }
            }
            Cur.close();
        } catch (Exception e) {
        }finally{
            mDb.endTransaction();
        }

        return map;
    }

    //***************************insert插入记录方法***************************
    /**
     * 根据数组的列和值进行insert
     * @param tableName
     * @param columns
     * @param values
     * @return long
     */
    public synchronized long insert(String tableName,String[] columns,Object[] values){
        ContentValues contentValues = new ContentValues();
        for (int rows = 0; rows < columns.length;++rows){
            ContentValuesPut(contentValues,columns[rows],values[rows]);
        }
        // long rowId = this.mDb.insert(tableName,null,contentValues);
        return insert(tableName,contentValues);
    }
    /**
     * 根据ContentValues来insert一条记录
     *
     * @param table
     * @param values
     * @return
     */
    public synchronized long insert(String table, List<ContentValues> values) {
        long insert = 0;
        mDb.beginTransaction();//开启事务
        try {
            for (int i = 0; i < values.size(); i++) {
                insert = mDb.insert(table, null, values.get(i));
            }
            mDb.setTransactionSuccessful();//设置事务的标志为True
        } catch (Exception e) {
            insert = -1;
        } finally {
            mDb.endTransaction();
            //结束事务,有两种情况：commit,rollback,
            //事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
        }
        return insert;
    }
    /**
     * 根据map来进行insert
     * @param tableName
     * @param columnValues
     * @return
     */
    public synchronized long  insert(String tableName,Map<String,Object> columnValues){
        ContentValues contentValues = new ContentValues();
        Iterator iterator = columnValues.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            this.ContentValuesPut(contentValues,key,columnValues.get(key));
        }

        return insert(tableName,contentValues);
    }


    /**
     * 根据ContentValues来insert一条记录
     * @param table
     * @param values
     * @return
     */
    public synchronized long insert(String table, ContentValues values) {
        long insert = 0;
        mDb.beginTransaction();//开启事务
        try {
            insert=mDb.insert(table, null, values);
            mDb.setTransactionSuccessful();//设置事务的标志为True
        } catch (Exception e) {
        }finally{
            mDb.endTransaction();
            //结束事务,有两种情况：commit,rollback,
            //事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
        }
        return insert;
    }

    //***************************update更新记录方法***************************
    /**
     * 根据数组条件来update
     * @param tableName
     * @param columns
     * @param values
     * @param whereColumns
     * @param whereArgs
     * @return：-1执行出错
     */
    public  synchronized long update(String tableName,String[] columns,Object[] values,String[] whereColumns,String[] whereArgs){
        ContentValues contentValues = new ContentValues();
        for (int i=0;i<columns.length;++i){
            this.ContentValuesPut(contentValues,columns[i],values[i]);
        }
        String whereClause = this.initWhereSqlFromArray(whereColumns);
        return update(tableName,contentValues,whereClause,whereArgs) ;
    }


    /**
     * 根据map值来进行update
     * @param tableName
     * @param columnValues
     * @param whereParam
     * @return：-1执行出错
     */
    public synchronized long update(String tableName,Map<String,Object> columnValues,Map<String,String> whereParam){
        ContentValues contentValues = new ContentValues();
        Iterator iterator = columnValues.keySet().iterator();

        String columns;
        while (iterator.hasNext()){
            columns = (String) iterator.next();
            ContentValuesPut(contentValues,columns,columnValues.get(columns));
        }

        Map map = this.initWhereSqlFromMap(whereParam);
        String whereClause=(String)map.get("whereSql");
        String[] whereArgs=(String[]) map.get("whereSqlParam");
        return update(tableName,contentValues,whereClause,whereArgs) ;
    }


    /**
     * 更新某条数据update
     * @return：-1执行出错
     */
    public synchronized long update(String table, ContentValues values, String whereClause,
                                    String[] whereArgs) {
        long upd = 0;
        mDb.beginTransaction();
        try {
            upd=mDb.update(table, values, whereClause, whereArgs);
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            upd=-1;
        }finally{
            mDb.endTransaction();
        }
        return upd;
    }

    /**
     * 根据数组条件进行delete
     * @param tableName
     * @param whereColumns
     * @param whereParam
     * @return：-1执行出错
     */
    public synchronized long delete(String tableName,String[] whereColumns,String[] whereParam){
        String whereStr = this.initWhereSqlFromArray(whereColumns);
        return delete(tableName,whereStr,whereParam);
    }

    //***************************delete删除记录方法***************************
    /**
     * 根据map来进行delete
     * @param tableName
     * @param whereParams
     * @return：-1执行出错
     */
    public synchronized long delete(String tableName,Map<String,String> whereParams){
        Map map = this.initWhereSqlFromMap(whereParams);
        String whereClause=(String)map.get("whereSql");
        String[] whereArgs=(String[]) map.get("whereSqlParam");

        return delete(tableName,whereClause,whereArgs);
    }

    /**
     * 删除数据，根据条件
     * @return：-1执行出错
     */
    public synchronized long delete(String table, String whereClause, String[] whereArgs) {
        long del = 0;
        mDb.beginTransaction();
        try {
            del=mDb.delete(table, whereClause, whereArgs);
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            del=-1;
        }finally{
            mDb.endTransaction();
        }
        return del;
    }

    /**
     * 删除所有数据
     * @param tablename
     * @return:-1执行出错
     */
    public synchronized long deleteAllData(String tablename) {
        long delAll = 0;
        mDb.beginTransaction();
        try {
            delAll=mDb.delete(tablename, null, null);
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            delAll=-1;
        }finally{
            mDb.endTransaction();
        }
        return delAll;
    }

    //***************************执行SQL语句***************************
    /**
     * 执行sql语句
     * @return
     */
    public synchronized void execSQL(String sql) {
        mDb.beginTransaction();
        try {
            mDb.execSQL(sql);
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
    }
    /**
     * 批量执行sql语句
     *
     * @return
     */
    public synchronized void execSQL(List<String> sqlList) {
        mDb.beginTransaction();
        try {
            for (int i = 0; i <sqlList.size() ; i++) {
                mDb.execSQL(sqlList.get(i));
            }
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            mDb.endTransaction();
        }
    }
//***************************获取表字段是否存在***************************

    /**
     * 判断表是否存在
     * @param table
     * @return
     */
    public synchronized Boolean GetTableisExist(String table) {
        Boolean isExist = false;
        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append(" SELECT count(*) as counts FROM sqlite_master ");
        sBuilder.append(" WHERE type='table' AND name='").append(table).append("';");
        HashMap map= queryItemMap(sBuilder.toString());
        Integer RowCout =(int)map.get("counts");
        if (RowCout > 0) {
            isExist = true;
        }
        return isExist;
    }

    /**
     * 判断字段是否存在
     * @param table
     * @param field
     */
    public synchronized Boolean GetFieldisExist(String table,String field) {
        Boolean isExist = false;
        StringBuilder sBuilder=new StringBuilder();
        sBuilder.append(" select count(*) as counts  from sqlite_master");
        sBuilder.append(" where name='").append(table).append("' ");
        sBuilder.append(" and sql like '% ").append(field).append(" %';");

        HashMap map= queryItemMap(sBuilder.toString());
        Integer RowCout =(int)map.get("counts");
        if (RowCout > 0) {
            isExist = true;
        }
        return isExist;
    }

    //***************************内部私有方法***************************

    /**
     * 统一对ContentValues处理
     * @param contentValues
     * @param key
     * @param value
     */
    private void ContentValuesPut(ContentValues contentValues,String key,Object value){
        if (value==null){
            contentValues.put(key,"");
        }else{
            String className = value.getClass().getName();
            if (className.equals("java.lang.String")){
                contentValues.put(key,value.toString());
            } else if (className.equals("java.lang.Integer")){
                contentValues.put(key,Integer.valueOf(value.toString()));
            } else if (className.equals("java.lang.Float")){
                contentValues.put(key,Float.valueOf(value.toString()));
            } else if (className.equals("java.lang.Double")){
                contentValues.put(key,Double.valueOf(value.toString()));
            } else if (className.equals("java.lang.Boolean")){
                contentValues.put(key,Boolean.valueOf(value.toString()));
            } else if (className.equals("java.lang.Long")){
                contentValues.put(key,Long.valueOf(value.toString()));
            } else if (className.equals("java.lang.Short")){
                contentValues.put(key,Short.valueOf(value.toString()));
            }
        }
    }

    /**
     * 统一对数组where条件进行拼接
     * @param whereColumns
     * @return
     */
    private String initWhereSqlFromArray(String[] whereColumns){
        StringBuffer whereStr = new StringBuffer();
        for (int i=0;i<whereColumns.length;++i){
            whereStr.append(whereColumns[i]).append(" = ? ");
            if (i<whereColumns.length-1){
                whereStr.append(" and ");
            }
        }
        return whereStr.toString();
    }

    /**
     * 统一对map的where条件和值进行处理
     * @param whereParams
     * @return
     */
    private Map<String,Object> initWhereSqlFromMap(Map<String,String> whereParams){
        Set set = whereParams.keySet();
        String[] temp = new String[whereParams.size()];
        int i = 0;
        Iterator iterator = set.iterator();
        StringBuffer whereStr = new StringBuffer();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            whereStr.append(key).append(" = ? ");
            temp[i] = whereParams.get(key);
            if (i<set.size()-1){
                whereStr.append(" and ");
            }
            i++;
        }
        HashMap result = new HashMap();
        result.put("whereSql",whereStr);
        result.put("whereSqlParam",temp);
        return result;
    }


}
