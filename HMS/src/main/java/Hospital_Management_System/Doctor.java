package Hospital_Management_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Doctor {
	private Connection con;
	private Scanner scan;
	
	public Doctor(Connection con, Scanner scan) {
		this.con=con;
		this.scan=scan;
	}
	
	public void addDoctor() {
		System.out.println("Enter Doctor Name : ");
		String name=scan.next();
		System.out.println("Enter Doctor Specialization : ");
		String special=scan.next();
		
		try {
			String query="Insert into doctors(name, specialization) values(?, ?)";
			PreparedStatement prepstmt=con.prepareStatement(query);
			prepstmt.setString(1, name);
			prepstmt.setString(2, special);
			int affectedrows=prepstmt.executeUpdate();
			if(affectedrows>0) {
				System.out.println("Doctor added successfully ");
			}
			else {
				System.out.println("Failed to add Doctor!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void viewDoctors() {
		String query="select * from doctors";
		try {
			PreparedStatement prepstmt=con.prepareStatement(query);
			ResultSet resset=prepstmt.executeQuery();
			System.out.println("Doctors : ");
			System.out.println("+-----------+---------------+----------------+");
			System.out.println("| Doctor Id | Name          | Specialization |");
			System.out.println("+-----------+---------------+----------------+");
			while (resset.next()) {
				int id = resset.getInt("id");
				String name = resset.getString("name");
				String special = resset.getString("specialization");
				System.out.printf("|%-11s|%-15s|%-16s|\n", id, name, special);
				System.out.println("+-----------+---------------+----------------+");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getDoctorById(int id) {
		String query = "select * from doctors where id = ?";
		try {
			PreparedStatement prepstmt=con.prepareStatement(query);
			prepstmt.setInt(1, id);
			ResultSet resset=prepstmt.executeQuery();
			if(resset.next()) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
