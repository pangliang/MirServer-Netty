import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by liangwei on 16/2/18.
 */
public class DBCreateTest {

	@Test
	public void createTest(){
		Session session  = DB.getSession();
		String  userId = "userId_"+new Date().toString()+"_"+new Random().nextInt(1000000);
		User    player   = new User();
		player.loginId = userId;

		session.save(player);
		session.flush();

		Criteria criteria = session.createCriteria(User.class);

		List<User> list = criteria.add(Restrictions.eq("loginId",userId)).list();
		for(User p: list)
		{
			System.out.println(p);
		}
	}
}
