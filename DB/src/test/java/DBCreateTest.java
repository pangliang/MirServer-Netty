import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Player;
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
		Session session       = DB.getSession();
		String  username = "username_"+new Date().toString()+"_"+new Random().nextInt(1000000);
		Player  player   = new Player();
		player.setUsername(new Date().toString());
		session.save(player);

		Criteria criteria = session.createCriteria(Player.class);

		List<Player> list = criteria.add(Restrictions.eq("username",username)).list();
		for(Player p: list)
		{
			System.out.println(p);
		}
	}
}
