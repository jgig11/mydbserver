package com.xfonlineclass.mydb.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

	public static Connection getConn() throws SQLException,
			ClassNotFoundException {

		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql:///mydb", "root",
				"123456");

	}

	// ע��MD5�ļ�
	public static void regOSFileInfo(String md5, String path) throws Exception {
		Connection conn = null;
		try {
			conn = getConn();

			PreparedStatement pst = conn.prepareStatement("INSERT INTO "
					+ "osfile VALUES(?,?)");

			pst.setString(1, md5);
			pst.setString(2, path);

			if (pst.executeUpdate() <= 0) {
				throw new Exception();
			}

		} catch (Exception e) {
			throw new Exception();
		}
	}

	// ע���ϴ����ļ�����
	public static void regFileInfo(String filename, String md5,
			String username, long size) throws Exception {
		Connection conn = null;
		try {
			conn = getConn();

			PreparedStatement pst = conn
					.prepareStatement("INSERT INTO "
							+ "files(username,filename,filesize,filemd5) VALUES(?,?,?,?)");

			pst.setString(1, username);
			pst.setString(2, filename);
			pst.setLong(3, size);
			pst.setString(4, md5);

			if (pst.executeUpdate() <= 0) {
				throw new Exception();
			}

		} catch (Exception e) {
			throw new Exception();
		}
	}

	// �����ļ��Ƿ�����
	public static void sofile(String filemd5) throws Exception {
		Connection conn = null;
		try {
			conn = getConn();

			PreparedStatement pst = conn
					.prepareStatement("SELECT * FROM osfile WHERE filemd5=?");
			pst.setString(1, filemd5);
			if (!pst.executeQuery().next()) {
				throw new Exception();
			}

		} catch (Exception e) {
			throw new Exception();
		}

	}

	// ��¼��֤
	public static void login(String username, String password) throws Exception {

		Connection conn = null;
		try {
			conn = getConn();
			// Statement st=conn.createStatement();
			PreparedStatement pst = conn
					.prepareStatement("SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?");
			pst.setString(1, username);
			pst.setString(2, password);
			if (!pst.executeQuery().next()) {
				throw new Exception();
			}

		} catch (Exception e) {
			throw new Exception();
		}

	}

	public static void main(String[] args) throws Exception {
		regOSFileInfo("a", "A");
		// regFileInfo("aaa.zip", "VVVV", "123456", 15);
		// login("lisi", "123456");
		// sofile("1111");
		// System.out.println(getConn());

	}

}
