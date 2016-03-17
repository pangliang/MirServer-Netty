package com.zhaoxiaodan.mirserver.tools;

import com.zhaoxiaodan.mirserver.db.DB;
import org.hibernate.Session;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ImportDB {

    Session db;

    public Map<String, String> tables = new HashMap<String, String>() {
        {
            put("STDITEM", "数据文件/MIR2_PUBLIC_SERVERINFO.csv");
            put("STDITEM", "数据文件/MIR2_PUBLIC_STDITEM.csv");
            put("STDMONSTER", "数据文件/MIR2_PUBLIC_STDMONSTER.csv");
            put("STDMAGIC", "数据文件/MIR2_PUBLIC_STDMAGIC.csv");
        }
    };

    /**
     * 因为Hibernate创建的表的字段顺序问题, 利用反射导入原DBC数据库导出的文件
     *
     * @throws Exception
     */
    public void importAll() {
        try {
            DB.init();


            for (String tableName : tables.keySet()) {
                db = DB.getSession();
                db.getTransaction().begin();

                String dataFile = tables.get(tableName);
                doImport(tableName, dataFile);
                System.out.println("导入 " + dataFile + " 成功 !!!");

                db.getTransaction().commit();
            }


        } catch (Exception e) {
            e.printStackTrace();
            if (db.isOpen())
                db.getTransaction().rollback();

        }
    }


    public void doImport(String tableName, String fileName) throws Exception {

        String deleteSql = "delete from " + tableName + ";";
        db.createSQLQuery(deleteSql).executeUpdate();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), Charset.forName("UTF-8")));
        String line = reader.readLine();
        if (null == line)
            throw new Exception("文件为空");

        // 第一行为列名, 对应表字段名
        String[] fields = line.trim().split(",");

        while ((line = reader.readLine()) != null) {
            StringBuffer sb = new StringBuffer("insert into ").append(tableName).append(" set ");
            line = line.trim();
            String[] parts = line.split(",");
            if (parts.length < fields.length)
                throw new Exception("数据文件列数太少 :" + line);
            for (int i = 0; i < fields.length; i++) {
                sb.append(fields[i]).append("='").append(parts[i]).append("',");
            }

            String sql = sb.substring(0, sb.length() - 1) + ";";
//            System.out.println(sql);
            db.createSQLQuery(sql).executeUpdate();
        }
    }

    public static void main(String[] args) {
        ImportDB importDB = new ImportDB();
        importDB.importAll();

    }
}
