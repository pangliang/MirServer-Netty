package com.zhaoxiaodan.mirserver.gameserver.types;

/**
 * 角色类型
 */
public enum Race {

	/**
	 * 玩家
	 */
	Player(0),
	/**
	 * 人形怪
	 */
	PlayerMoster(150),
	Animal(50),
	Npc(10),
	/**
	 * 怪
	 */
	Monster(80),
	/**
	 * 商店Npc, 也就是可以点击的
	 */
	Merchant(Animal.id);

	public final byte id;

	Race(int id) {
		this.id = (byte)id;
	}
}
