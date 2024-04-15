package Hospital_Management_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Patient {
	private Connection con;
	private Scanner scan;
	
	public Patient(Connection con, Scanner scan) {
		this.con=con;
		this.scan=scan;
	}
	
	public void addPatient() {
		System.out.println("Enter Patient Name : ");
		String name=scan.next();
		System.out.println("Enter Patient Age : ");
		int age=scan.nextInt();
		System.out.println("Enter Patient Gender : ");
		String gender=scan.next();
		
		try {
			String query="Insert into patients(name, age, gender) values(?, ?, ?)";
			PreparedStatement prepstmt=con.prepareStatement(query);
			prepstmt.setString(1, name);
			prepstmt.setInt(2, age);
			prepstmt.setString(3, gender);
			int affectedrows=prepstmt.executeUpdate();
			if(affectedrows>0) {
				System.out.println("Patient added successfully");
			}
			else {
				System.out.println("Failed to add Patient");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void viewPatients() {
		String query="select * from patients";
		try {
			PreparedStatement prepstmt=con.prepareStatement(query);
			ResultSet resset=prepstmt.executeQuery();
			System.out.println("Patients : ");
			System.out.println("+------------+---------------+-----+--------+");
			System.out.println("| Patient Id | Name          | Age | Gender |");
			System.out.println("+------------+---------------+-----+--------+");
			while (resset.next()) {
				int id = resset.getInt("id");
				String name = resset.getString("name");
				int age = resset.getInt("age");
				String gender = resset.getString("gender");
				System.out.printf("|%-12s|%-15s|%-5s|%-8s|\n", id, name, age, gender);
				System.out.println("+------------+---------------+-----+--------+");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getPatientById(int id) {
		String query = "select * from patients where id = ?";
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
