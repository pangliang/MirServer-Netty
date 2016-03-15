import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.objects.Monster

void onTick(Monster monster, long now) {

}

void onDamage(Monster monster, Player player, int damage) {
    player.winExp(monster.stdMonster.exp);
}


