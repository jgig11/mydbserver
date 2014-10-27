package com.xfonlineclass.mydb.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Thread {

	private Socket socket;

	public Server(Socket socket) {
		this.socket = socket;
	}

	public void run() { // 处理客户端的请求

		try {
			// 就是跟客户端说话的
			InputStream in = socket.getInputStream(); // 听客户端说什么
			OutputStream out = socket.getOutputStream();// 跟客户端说话

			byte[] b = new byte[1024];
			in.read(b);
			// / lisi 123456 MD5 文件名称 大小
			String cmm = new String(b).trim();
			String[] cs = cmm.split("	");
			String username = cs[0];
			String password = cs[1];
			String filemd5 = cs[2];
			String filename = cs[3];
			long filesize = Long.parseLong(cs[4]);

			try {
				DBManager.login(username, password);
				try {
					DBManager.sofile(filemd5);

					DBManager
							.regFileInfo(filename, filemd5, username, filesize);

					out.write("MC".getBytes());
					out.flush();
				} catch (Exception e) {// 没有重复过
					out.write("OK".getBytes());
					out.flush();

					File f = new File("c:\\mydb\\", new Date().getTime() + "");

					FileOutputStream fout = new FileOutputStream(f);

					byte[] bb = new byte[1024 * 8];
					int len = 0;
					while ((len = in.read(bb)) != -1) {
						fout.write(bb, 0, len);
						 
					}
					fout.close();

					// 保存MD5 和文件的真实上传路径
					DBManager.regOSFileInfo(filemd5, f.getPath());
					// 保存客户的文件资料
					DBManager
							.regFileInfo(filename, filemd5, username, filesize);

				}

			} catch (Exception e) {
				out.write("ERRORID".getBytes());
				out.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// / 用户名或密码错误 ERRORID

		// / MC

		// / OK

	}

	public static void openServer() {// 打开服务器 8088

		try {
			ServerSocket server = new ServerSocket(8088);
			while (true) {
				new Server(server.accept()).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		openServer();

	}

}
