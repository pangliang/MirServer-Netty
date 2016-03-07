package com.zhaoxiaodan.mirserver;

import com.zhaoxiaodan.mirserver.gameserver.engine.MerchantEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.gameserver.GameServer;
import com.zhaoxiaodan.mirserver.loginserver.LoginServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String line;
		try {
			while((line=in.readLine().trim())!=null){
				if("s".equals(line)){
					ScriptEngine.reload();
					MerchantEngine.reload();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}


	}
}
