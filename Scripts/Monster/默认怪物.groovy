import com.zhaoxiaodan.mirserver.db.objects.Monster
import com.zhaoxiaodan.mirserver.db.types.Direction
import com.zhaoxiaodan.mirserver.utils.NumUtil

void onTick(Monster monster){

    if(!monster.isAlive)
        return ;

    long now =  NumUtil.getTickCount();
    Random random = new Random();

    monster.metaClass.lastWalkTime = 0;

    if(now > monster.lastWalkTime + 3000){
        monster.lastWalkTime = now + random.nextInt(1000);

        monster.direction = Direction.values()[random.nextInt(Direction.values().length)];
        monster.walk(monster.direction);

    }
}