import com.zhaoxiaodan.mirserver.gameserver.entities.Player
import com.zhaoxiaodan.mirserver.gameserver.objects.Monster
import com.zhaoxiaodan.mirserver.gameserver.types.Direction
import com.zhaoxiaodan.mirserver.utils.NumUtil

class 和平怪物{
    void onTick(Monster monster, long now) {

        if (!monster.isAlive)
            return;

        if(monster.checkLastActionTime("move",monster.stdMonster.walkSpeed,1000)){
            monster.direction = Direction.values()[NumUtil.nextRandomInt(Direction.values().length)];
            monster.walk(monster.direction);
        }
    }

    void onDamage(Monster monster, Player player, int damage) {

    }
}

