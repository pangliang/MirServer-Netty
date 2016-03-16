package com.zhaoxiaodan.mirserver.gameserver.types;

/**
 * 0
 * 7 |  1
 * 6 --|--  2
 * 5 |  3
 * 4
 */
public enum Direction {
	UP,
	UPRIGHT,
	RIGHT,
	DOWNRIGHT,
	DOWN,
	DOWNLEFT,
	LEFT,
	UPLEFT;

	public Direction reverse() {
		switch (this) {
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			case RIGHT:
				return LEFT;
			case LEFT:
				return RIGHT;
			case UPRIGHT:
				return DOWNLEFT;
			case DOWNLEFT:
				return UPRIGHT;
			case UPLEFT:
				return DOWNRIGHT;
			case DOWNRIGHT:
				return UPLEFT;
			default:
				return null;
		}
	}

	/**
	 * 转向, UP.turn(1) => UPRIGHT  , DOWN.turn(-1) => DOWNRIGHT
	 *
	 * @param turn 顺时针是正值, 逆时针转是负值
	 *
	 * @return
	 */
	public Direction turn(int turn) {
		int size = Direction.values().length;
		turn = turn % size ;
		int curr = this.ordinal();
		if (curr + turn >= size)
			turn = (curr + turn) - size;
		else if (curr + turn < 0)
			turn = size + (curr + turn);
		else
			turn = curr + turn;
		return Direction.values()[turn];
	}
}
