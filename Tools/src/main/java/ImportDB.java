import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.StdItem;
import org.hibernate.Session;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;

public class ImportDB {

	Session dbSession ;

	/**
	 * 因为Hibernate创建的表的字段顺序问题, 利用反射导入原DBC数据库导出的文件
	 * @throws Exception
	 */
	public void importAll(){
		try{
			DB.init();
			dbSession = DB.getSession();
			dbSession.getTransaction().begin();
			doImport(StdItem.class,"数据文件/物品数据库.txt");
			dbSession.getTransaction().commit();
		}catch (Exception e){
			if(dbSession.isOpen())
				dbSession.getTransaction().rollback();

			e.printStackTrace();
		}
	}


	public void doImport(Class entiyClass,String fileName) throws Exception{

		String deleteSql = "delete from "+entiyClass.getSimpleName() +";";
		dbSession.createSQLQuery(deleteSql).executeUpdate();

		Field[] fields = StdItem.class.getDeclaredFields();

		BufferedReader reader     = new BufferedReader(new FileReader(fileName));
		String         line;
		while ((line = reader.readLine()) != null) {
			StringBuffer sb = new StringBuffer("insert into ").append(entiyClass.getSimpleName()).append(" set ");
			line = line.trim();
			String[] parts = line.split(";");
			if(parts.length < fields.length)
				throw new Exception("数据文件列数太少, 改行为:"+line);
			for(int i=0;i<fields.length;i++){
				sb.append(fields[i].getName()).append("='").append(parts[i]).append("',");
			}

			String sql = sb.substring(0,sb.length()-1)+";";
			System.out.println(sql);
			dbSession.createSQLQuery(sql).executeUpdate();
		}
	}

	public static void main(String[] args){
		ImportDB importDB = new ImportDB();
		importDB.importAll();

	}
}
