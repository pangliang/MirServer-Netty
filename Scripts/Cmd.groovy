import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.types.MapPoint

void test(Player player, List<String> args) {

    player.sendSysMsg(Arrays.toString(args.toArray()));
}

void move(Player player, List<String> args) {
    if (args.size() >= 3) {
        MapPoint moveTo = new MapPoint();
        moveTo.mapId = args[0];
        moveTo.x = Short.parseShort(args[1]);
        moveTo.y = Short.parseShort(args[2]);
        player.enterMap(moveTo);
    } else if (args.size() == 1) {
        player.enterMap(args[0]);
    }

}