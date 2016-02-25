package com.zhaoxiaodan.mirserver;

import com.zhaoxiaodan.mirserver.gameserver.GameServer;
import com.zhaoxiaodan.mirserver.loginserver.LoginServer;

public class Main {
	public static void main(String[] args){

		new Thread(){
			public void run(){
				try {
					new LoginServer(7000).run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		new Thread(){
			public void run(){
				try {
					new GameServer(7400).run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();


	}
}
