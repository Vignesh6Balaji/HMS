package Hospital_Management_System;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class HMS {
	private static final String url="jdbc:mysql://localhost:3306/hospital";
	private static final String userName="root";
	private static final String password="admin";
	
	public static void main(String[] args) {
		Scanner scan=new Scanner(System.in);
		try {
			Connection con=DriverManager.getConnection(url, userName, password);
			Patient patient=new Patient(con, scan);
			Doctor doctor=new Doctor(con, scan);
			while(true) {
				System.out.println("Welcome to Hospital Management System ");
				System.out.println("1. Add Patients \n2. View Patients \n3. Add Doctors \n4. View Doctors \n5. Book Appointments \n6. exit");
				System.out.println("Enter your choice : ");
				int choice = scan.nextInt();
				switch(choice) {
				case 1 :
					patient.addPatient();
					System.out.println();
					break;
					//Add Patient
				case 2 :
					patient.viewPatients();
					System.out.println();
					break;
					//View Patient
				case 3 :
					doctor.addDoctor();
					System.out.println();
					break;
					//Add Doctor
				case 4 :
					doctor.viewDoctors();
					System.out.println();
					break;
					//View Doctor
				case 5 :
					bookAppointment(patient, doctor, con, scan);
					System.out.println();
					break;
					//Book Appointment
				case 6 :
					System.out.println("Thank you for using Hospital Management System");
					return;
					//Exit
				default :
					System.out.println("Enter valid choice!!!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bookAppointment(Patient pat, Doctor doc, Connection con, Scanner scan) {
		System.out.println("Enter Patient Id : ");
		int patientId = scan.nextInt();
		System.out.println("Enter Doctor Id : ");
		int doctorId = scan.nextInt();
		System.out.println("Enter apointment date (YYYY-MM-DD) : ");
		String appointmentDate = scan.next();
		if(pat.getPatientById(patientId) && doc.getDoctorById(doctorId)) {
			if(checkDoctorAvailability(doctorId, appointmentDate, con)) {
				String appointmentQuery = "Insert into appointments(patient_id, doctor_id, appointment_date) values(?, ?, ?)";
				try {
					PreparedStatement prepstmt = con.prepareStatement(appointmentQuery);
					prepstmt.setInt(1, patientId);
					prepstmt.setInt(2, doctorId);
					prepstmt.setString(3, appointmentDate);
					int affectedRow = prepstmt.executeUpdate();
					if(affectedRow>0) {
						System.out.println("Appointment Booked Successfully");
					}
					else {
						System.out.println("Failed to book Appointment!!!");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Doctor is not available in this date!!!");
			}
		}
		else {
			System.out.println("Either patient or doctor is not available !!!");
		}
	}
	
	public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection con) {
		String query="select count(*) from appointments where doctor_id = ? and appointment_date = ?";
		try {
			PreparedStatement prepstmt = con.prepareStatement(query);
			prepstmt.setInt(1, doctorId);
			prepstmt.setString(2, appointmentDate);
			ResultSet resset = prepstmt.executeQuery();
			if(resset.next()) {
				int count = resset.getInt(1);
				if(count==0) {
					return true;
				}
				else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
