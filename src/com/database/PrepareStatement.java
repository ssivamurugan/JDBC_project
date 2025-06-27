package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PrepareStatement {

	private static Connection connection = null;
	private static Scanner scanner = null;
	private static String insertQuery = "insert into `students`(`s_id`, `name`, `age`, `email`) values (?, ?, ?, ?)";
	private static String deleteQuery = "delete from `students` where `name` = ? ";
	private static PreparedStatement preStatement;
	private static int[] records;
	private static Statement statement;
	private static int[] insert;
	private static int[] update;
	private static int[] delete;

	public static void main(String[] args) {
		char charAt = '\u0000';
		scanner = new Scanner(System.in);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			connection = DriverManager.getConnection(Credentials.url, Credentials.name, Credentials.pwd);
			
			System.out.println("To insert, update, delete (i, u, d): ");
			charAt = scanner.nextLine().toLowerCase().charAt(0);
			
			while(charAt == 'i' || charAt == 'u' || charAt == 'd') {
				
				switch(charAt) {
				
				case 'i':
					insert = insert(connection);
				break;
				
				case 'u':
					update = update(connection);
				break;
				
				case 'd':
					delete = delete(connection);
				break;
									
					
			}
				
				System.out.println("To insert, update, delete (i, u, d): ");
				charAt = scanner.nextLine().toLowerCase().charAt(0);
				
			}
			
			int insertLength = (insert == null)? 0 : insert.length;
			int updateLength = (update == null)? 0 : update.length;
			int deleteLength = (delete == null)? 0 : delete.length;
			int totalLength = insertLength + updateLength + deleteLength;

			records = new int[totalLength];
			int index = 0;

			for (int i = 0; i < insertLength; i++) {
			    records[index++] = insert[i];
			}

			for (int i = 0; i < updateLength; i++) {
			    records[index++] = update[i];
			}

			for (int i = 0; i < deleteLength; i++) {
			    records[index++] = delete[i];
			}

			
			for(int r : records) {
				System.out.print(r + ",");
			}
			
			System.out.println("Do you want to see table: (y/n)");
			charAt = scanner.nextLine().toLowerCase().charAt(0);
			
			if(charAt == 'y') {
				statement = connection.createStatement();
				ResultSet set =statement.executeQuery("select `s_id`, `name`, `age`, `email` from `students` ");
				
				while(set.next()) {
					System.out.printf("| %-3d | %-20s | %-3d | %-30s |\n", set.getInt(1), set.getString(2), set.getInt(3), set.getString(4));
				}
				
				
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			try {
				if(preStatement != null) {
					preStatement.close();
				}
				if(statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			scanner.close();
		}
		
		
	}

	private static int[] delete(Connection connection2) throws SQLException {
		 preStatement = connection.prepareStatement(deleteQuery);
		 char ch = '\u0000';
		 String name = null;
		 do {
			 System.out.println("Enter name of the student to delete record: ");
			 name = scanner.nextLine();
			 
			 preStatement.setString(1, name);
			 
			 preStatement.addBatch();
			 
			 System.out.println("Do you want to delete record again (y/n): ");
			 ch = scanner.nextLine().toLowerCase().charAt(0);
		 }while(ch == 'y');
		 return preStatement.executeBatch();
		
	}

	private static int[] update(Connection connection2) throws SQLException {
		char ch = '\u0000';
		String fieldName = null;
		
		System.out.println("which field you want to change: ");
		fieldName = scanner.nextLine();
		
		String updateQuery = "update `students` set "+ "`" +fieldName + "`" +" = ? where `name` = ? ";
		preStatement = connection.prepareStatement(updateQuery);
		 do {
			 System.out.println("Enter name of the student to update record: ");
			 preStatement.setString(2, scanner.nextLine());
			 
			 if(fieldName.equals("s_id")|| fieldName.equals("age")) {
				 System.out.println("Enter "+fieldName+" value to update: ");
				 preStatement.setInt(1, scanner.nextInt());
				 scanner.nextLine();
			 }
			 else {
				 System.out.println("Enter "+fieldName+" value to update: ");
				 preStatement.setString(1, scanner.nextLine());
			 }
			 
			 preStatement.addBatch();
			 
			 System.out.println("Do you want to update record again (y/n): ");
			 ch = scanner.nextLine().toLowerCase().charAt(0);
		 }while(ch == 'y');
		
		 return preStatement.executeBatch();
	}

	private static int[] insert(Connection connection2) throws SQLException {
		 char ch = '\u0000';
		 preStatement = connection.prepareStatement(insertQuery);
		do {
			 System.out.println("Enter the values: ");
			 System.out.println("Enter the s_id: ");
			 preStatement.setInt(1, scanner.nextInt());
			 scanner.nextLine();
			 
			 System.out.println("Enter the name: ");
			 preStatement.setString(2, scanner.nextLine());
			 
			 System.out.println("Enter the age: ");
			 preStatement.setInt(3, scanner.nextInt());
			 scanner.nextLine();
			 
			 System.out.println("Enter the email: ");
			 preStatement.setString(4, scanner.nextLine());
			 
			 preStatement.addBatch();
			 
			 System.out.println("Do you want to insert record again (y/n): ");
			 ch = scanner.nextLine().toLowerCase().charAt(0);
		 }while(ch == 'y');
		return preStatement.executeBatch();
		
	}

}
