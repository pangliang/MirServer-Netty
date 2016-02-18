import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.HashMap;

/**
 * Created by liangwei on 16/2/18.
 */
public class DBCreateTest {

	@Test
	public void createTest(){

		DB.init();
		EntityManager manager = DB.createEntityManager();

		Player player = new Player();
		player.setUsername("pangliang");

		manager.persist(player);

		manager = DB.createEntityManager();

		Player p = manager.find(Player.class,12L, new HashMap<String, Object>(){{put("username","pangliang");}});

		Assert.assertEquals(player.getUsername(),p.getUsername());

	}
}
