package com.zhaoxiaodan.mirserver.db.types;

/**
 *     0
 *   7 |  1
 * 6 --|--  2
 *   5 |  3
 *     4
 *
 *
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

	public Direction reverse(){
		switch (this){
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
}
