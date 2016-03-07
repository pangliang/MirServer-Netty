


import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.types.MapPoint
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine
import com.zhaoxiaodan.mirserver.gameserver.engine.MessageEngine

void test(Player player, List<String> args){

    player.session.sendPacket(MessageEngine.createMessage(player.inGameId, Arrays.toString(args.toArray())));
}


void move(Player player, List<String> args){
    if (args.size() > 1){
        MapPoint moveTo = MapPoint();
        moveTo.mapId = args[0];
        moveTo.x = Short.parseShort(args[1]);
        moveTo.y = Short.parseShort(args[2]);
        MapEngine.enter(player,moveTo);
    }else if (args.size() == 1){
        MapEngine.enter(player,args[0]);
    }

}