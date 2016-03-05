import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import org.junit.Assert;
import org.junit.Test;

public class LoadMapInfoTest {

	@Test
	public void loadTest() throws Exception {
		MapEngine.reload();

		MapEngine.MapInfo info = MapEngine.getMapInfo("G003");
		for (short y = 0; y < info.height; y++) {
			for (short x = 0; x < info.width; x++) {

				if (info.tileCanWalkFlags[x][y])
					System.out.print("***");
				else
					System.out.print(" + ");
			}
			System.out.println();
		}

		Assert.assertEquals(1, 1);
	}
}
