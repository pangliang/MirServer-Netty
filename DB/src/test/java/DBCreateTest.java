import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
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
		String  userId = "userId_"+new Date().toString()+"_"+new Random().nextInt(1000000);
		User    player   = new User();
		player.loginId = userId;

		DB.save(player);

		List<User> list = DB.query(User.class,Restrictions.eq("loginId",userId));
		for(User p: list)
		{
			System.out.println(p);
		}
	}
}
