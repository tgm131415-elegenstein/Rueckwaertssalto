import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.mysql.jdbc.*;

/**
 * @author Erik
 *
 */
public class MetadataCollector {
	static final String JDBC = "com.mysql.jdbc.Driver";
	private String host = null, uname = null, pwd = null, dbname = null;
	private DatabaseMetaData dbmd = null;
	private Connection conn = null;
	public ArrayList<String> tablenames= new ArrayList<>();
	public HashMap<String, String> fieldPFK = new HashMap<>();
	public void createConnection(String host, String username, String password,
			String databasename) {
		this.host = host;
		this.uname = username;
		this.pwd = password;
		this.dbname = databasename;
		createConnection();
	}

	public void createConnection() {
		try {
			Class.forName(JDBC);
		} catch (ClassNotFoundException e) {
			System.err
			.println("JDBC can't be located. Make sure it is available");
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + this.host
					+ "/" + dbname, uname, pwd);
		} catch (SQLException e) {
			System.err
			.println("Failed to connect to database\n Check credials and the dbname or hostname/ip");
			e.printStackTrace();
		}
	}

	public String[] getTabNames(){
		if(conn == null){
			System.err.println("Got no Connection");
		}else{
			try {
				dbmd=conn.getMetaData();
			} catch (SQLException e) {
				System.err.println("can't get Database-Metadata");
				e.printStackTrace();
			}
			if(dbmd!=null){
				ResultSet rs=null;
				try {
					rs = dbmd.getTables(null, null, "%", null);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(rs != null){
						while (rs.next()) {
							System.out.println(rs.getString(3));
							tablenames.add(rs.getString(3));
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	public void getColumnNames(String tablename){
		try {
			ResultSet rs = dbmd.getColumns(null, null, tablename, null);
//			rs.next();
			while(rs.next()){
				for(int i = 0; i< foreign(tablename).length; i++)
					if()
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void destory(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String[] foreign(String tablename){
		try {
			ResultSet rs;
			rs=dbmd.getImportedKeys(null, null, tablename);
//			rs = dbmd.getPrimaryKeys(null, null, tablename);
			ArrayList<String> tmp = new ArrayList<>();
			for(int i=1;rs.next();i++){
//			rs.next();
//				for(int i = 1; i<=1;i++ ){
					tmp.add(rs.getString("FKCOLUMN_NAME"));
//				}
			}
			return (String[])tmp.toArray();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		MetadataCollector mdc = new MetadataCollector();
		mdc.createConnection("192.168.222.132","root","root","timetool");
		mdc.getTabNames();
		System.out.println("-------------");
		mdc.foreign("comment");
		System.out.println("-------------");
		mdc.getColumnNames("comment");
		mdc.destory();
	}

}
