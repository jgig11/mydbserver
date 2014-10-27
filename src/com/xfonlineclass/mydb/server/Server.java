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

	public void run() { // ����ͻ��˵�����

		try {
			// ���Ǹ��ͻ���˵����
			InputStream in = socket.getInputStream(); // ���ͻ���˵ʲô
			OutputStream out = socket.getOutputStream();// ���ͻ���˵��

			byte[] b = new byte[1024];
			in.read(b);
			// / lisi 123456 MD5 �ļ����� ��С
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
				} catch (Exception e) {// û���ظ���
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

					// ����MD5 ���ļ�����ʵ�ϴ�·��
					DBManager.regOSFileInfo(filemd5, f.getPath());
					// ����ͻ����ļ�����
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

		// / �û������������ ERRORID

		// / MC

		// / OK

	}

	public static void openServer() {// �򿪷����� 8088

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
