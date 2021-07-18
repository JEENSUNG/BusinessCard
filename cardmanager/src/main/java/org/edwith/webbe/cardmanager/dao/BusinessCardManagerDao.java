package org.edwith.webbe.cardmanager.dao;

import org.edwith.webbe.cardmanager.dto.BusinessCard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BusinessCardManagerDao {
	private static String dburl = "jdbc:mysql://localhost:3306/connectdb?useSSL=false&&serverTimezone=UTC";
	private static String dbUser = "connectuser";
	private static String dbPasswd = "connect123!@#";
	private static String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS businesscard_db("
			+ "name VARCHAR(10), "
			+ "phone VARCHAR(15), "
			+ "companyName VARCHAR(20), "
			+ "createDate DATETIME DEFAULT CURRENT_TIMESTAMP);";
	public static void createTable() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPasswd);
				Statement stmt = conn.createStatement()) {
			stmt.execute(CREATE_TABLE_SQL);
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    public List<BusinessCard> searchBusinessCard(String keyword){
    	List<BusinessCard> list = new ArrayList<>();
		String sql = "SELECT * FROM businesscard_db WHERE name LIKE ?";
		try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPasswd);
				PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, "%"+keyword+"%");
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					String userName = rs.getString("name");
					String userPhone = rs.getString("phone");
					String userCompanyName = rs.getString("companyName");
					String date = rs.getString("createDate");
					BusinessCard businessCard = new BusinessCard(userName, userPhone, userCompanyName);
					list.add(businessCard);
				}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

    public BusinessCard addBusinessCard(BusinessCard businessCard){
    	String sql = "INSERT INTO businesscard_db (name, phone, companyName) VALUES (?, ?, ?)";
		
		try (Connection conn = DriverManager.getConnection(dburl, dbUser, dbPasswd);
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, businessCard.getCompanyName());
			ps.setString(2,  businessCard.getName());
			ps.setString(3,  businessCard.getPhone());
		
			int insertCount = ps.executeUpdate();
			System.out.println(insertCount + "개 등록하였습니다");
			ps.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return businessCard;
    }
}
