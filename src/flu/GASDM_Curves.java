package flu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class GASDM_Curves {

	static ArrayList<String> resultList = new ArrayList<String>();
	static HashMap<String, String> userLocMap = null;
	static String[] regionVector = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	static Connection conn = null;
	static Statement stmt = null;
	
	private static void initDB(){
		String url = "jdbc:mysql://localhost:3306/gasdm?user=root&password=&useUnicode=true&characterEncoding=UTF8";
		try {  
			Class.forName("com.mysql.jdbc.Driver");  
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {  
			e.printStackTrace();  
		}
	}
	
	private static void initUserLocMap() {
		userLocMap = new HashMap<String, String>();
		try {
			stmt = conn.createStatement();
			String sql = "select distinct u_id, region from userlocflu";
			ResultSet rs;
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String u_id = rs.getString("u_id");
				String region = rs.getString("region");
				userLocMap.put(u_id, region);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void initResultList() {
		for (int i=0; i<226; i++) {
			String str = "";
			resultList.add(str);
		}
	}
	
	private static void readFile(String filePath) {
		File fin = new File(filePath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fin));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				int vector[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
				String result = "";
				String[] str = tempString.split("\\[");
				int week = Integer.parseInt(str[0]);
				String[] s = str[1].split("\\]")[0].split("\\,");
				for (int i=0; i<s.length; i++) {
//					String region = userLocMap.get(s[i]); 
//					vector[Integer.parseInt(region)-1]++;
					if (Integer.parseInt(s[i]) >= 1)
						vector[i] = 1;
				}
				result = result + Integer.toString(vector[0]);
				for (int i=1; i<10; i++) {
					result = result + " " + Integer.toString(vector[i]);
				}
				resultList.set(week-1, result);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void outputResultList(String filePath) {
		File fout = new File(filePath);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fout));
			for (int i=1; i<resultList.size(); i++) {
				writer.write(i+1 + " [ " + resultList.get(i) + " ]");
				writer.newLine();
				System.out.println(i+1 + " [ " + resultList.get(i) + " ]");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String inputFilePath = "C:\\Users\\Shuai\\Desktop\\APDM-flu-pdv\\GASDM-result-flu\\newres.txt";
		String outputFilePath = "C:\\Users\\Shuai\\Desktop\\APDM-flu-pdv\\GASDM-result-flu\\GASDM-result-flu_20160717_vector.txt";
//		initDB();
//		initUserLocMap();
		initResultList();
		readFile(inputFilePath);
		outputResultList(outputFilePath);
	}

}
