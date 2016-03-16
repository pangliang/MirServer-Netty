import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.objects.BaseObject
import com.zhaoxiaodan.mirserver.db.objects.Monster
import com.zhaoxiaodan.mirserver.db.types.Direction

class 默认怪物 {
    void onTick(Monster monster, long now) {

        if (!monster.isAlive)
            return;

        if (attack(monster, now)
                || searchTarget(monster, now)
                || moveToTarget(monster, now)
        ) return;
    }

    boolean moveToTarget(Monster monster, long now) {
        if(monster.target == null)
            return false;

        if(!monster.checkLastActionTime("move",monster.stdMonster.walkSpeed,1000))
            return true;

        Direction direction = monster.directionTo(monster.target);
        monster.walk(direction);
    }

    boolean searchTarget(Monster monster, long now) {
        int minDistance = Integer.MAX_VALUE;
        BaseObject minDistanceTarget = null;
        for (BaseObject target : monster.objectsInView.values()) {
            if (!target.isAlive)
                continue;
            int dis = Math.abs(monster.currMapPoint.x - target.currMapPoint.x) + Math.abs(monster.currMapPoint.y - target.currMapPoint.y);
            if (dis < minDistance) {
                minDistanceTarget = target;
                minDistance = dis;
            }
        }

        if (minDistanceTarget == null) {
            // 没有目标, 中断后面的ai
            return true
        }

        monster.target = minDistanceTarget;
        return false;
    }

    boolean attack(Monster monster, long now) {
        if (monster.target == null)
            return false;

        if (!monster.target.isAlive)
            return false;

        // 距离太远, 攻击不到
        if (Math.abs(monster.currMapPoint.x - monster.target.currMapPoint.x) > 1
                || Math.abs(monster.currMapPoint.y - monster.target.currMapPoint.y) > 1)
            return false;

        // 攻击间隔未到, 则中断后续ai, 等待攻击
        if(!monster.checkLastActionTime("attack",monster.stdMonster.attackSpeed,0))
            return true;

        Direction direction = monster.directionTo(monster.target);
        monster.hit(direction);
        monster.target.damage(monster, monster.getPower());
        return true;
    }

    void onDamage(Monster monster, Player player, int damage) {

    }
}

