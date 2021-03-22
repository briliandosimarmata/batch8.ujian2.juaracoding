package DataPinjaman;

import java.awt.Desktop;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

	static final String driverSQL = "com.mysql.cj.jdbc.Driver";
	static String db_name = "batch8_ujian2";
	static final String database = "jdbc:mysql://localhost/"+db_name+"?allowMultiQueries=true";
	static final String user = "root";
	static final String pass = "";
	
	static Connection conn;
	static Statement stat;
	static ResultSet set;
	
	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.print("Masukkan nama database: ");
		db_name = scan.next();
		// TODO Auto-generated method stub
		try {
			Class.forName(driverSQL);
			conn = DriverManager.getConnection(database, user, pass);
			stat = conn.createStatement();
			
			while (!conn.isClosed()) {
				menu();
			}
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	static void menu () {
		
		System.out.println("====================================");
		System.out.println("PROGRAM MENGHITUNG ANGSURAN PINJAMAN");
		System.out.println("====================================");
		System.out.println("1. Tampilkan Data Rincian Angsuran");
		System.out.println("2. Masukkan Data Angsuran");
		System.out.println("3. Keluar");
		System.out.print("Masukkan pilihan (1/2/3): ");
		int select = scan.nextInt();
		
		switch (select) {
		case 1: {
			System.out.println();
			showRincianAngsuran();
			break;
		}
		case 2: {
			System.out.println();
			insertUlangBulan();
			break;
		}
		case 3: {
			System.exit(0);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + select);
		}
		
	}
	
	static void insertUlangBulan() {
		try {
			System.out.print("Masukkan nama peminjam: ");
			String name = scan.next();
			System.out.print("Masukkan tanggal meminjam (yyyy-mm-dd): ");
			LocalDate date = LocalDate.parse(scan.next());
			System.out.print("Masukkan jumlah uang dipinjam: ");
			int platfon = scan.nextInt();
			System.out.print("Masukkan nilai bunga: ");
			double rate = scan.nextDouble();
			System.out.print("Masukkan jangka waktu peminjaman: ");
			int period = scan.nextInt();
			
			String query = "INSERT INTO `ulangbulan`(`nama`, `dateFrom`, `platfon`, `bunga`, `lamapinjaman`) VALUES ('%s', '%s', %d, %6.2f, %d)";
			query = String.format(query, name, date, platfon, rate, period);
			stat.execute(query);
			
			String query2 = "SELECT * FROM ulangbulan WHERE nama = '%s'";
			query2 = String.format(query2, name);
			set = stat.executeQuery(query2);
			
			while (set.next()) {
				String getName = set.getString("nama");
				String getDate = set.getString("dateFrom");
				int getPlatfon = set.getInt("platfon");
				double getRate = set.getDouble("bunga");
				int getPeriod = set.getInt("lamapinjaman");
				System.out.println();
				System.out.println("Data berhasil dimasukkan!");
				System.out.println("nama => "+getName+", dateFrom => "+getDate+", platfon => "+getPlatfon+", bunga => "+getRate+", lamapinjaman => "+getPeriod);
				System.out.println();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	static void showRincianAngsuran() {
		try {
			System.out.print("Masukkan nama kreditur: ");
			String nama = scan.next();
			
			String queryDate = "SELECT DATE(dateFrom) AS dateFrom FROM ulangbulan WHERE nama = '%s'";
			queryDate = String.format(queryDate, nama);
			set = stat.executeQuery(queryDate);
			set.next();
			LocalDate dateFrom = LocalDate.parse(set.getString("datefrom"));
			
			String query0 = "SELECT * FROM ulangbulan WHERE nama = '%s'";
			query0 = String.format(query0, nama);
			set = stat.executeQuery(query0);
			set.next();
			int platfon = set.getInt("platfon");
			double bunga = set.getDouble("bunga");
			int lamapinjaman = set.getInt("lamapinjaman");
			
			
			String query = "SELECT a/(1-POWER(x,y)) AS totalAngsuran FROM (SELECT platfon*(bunga/12) AS a, 1+(bunga/12) AS x, -(lamapinjaman/12)*12 as y FROM ulangbulan WHERE nama = '%s') totalAngsuran";
			query = String.format(query, nama);
			set = stat.executeQuery(query);
			set.next();
			
			double totalAngsuran = set.getDouble("totalAngsuran");
			double consBunga = bunga/360*30;
			double sisaPinjaman = 0;
			
			System.out.println();
			dateFrom = dateFrom.minusMonths(1);
			
			String queryDropTable = "DROP TABLE IF EXISTS `angsuran`";
			stat.execute(queryDropTable);
			String queryTable = "CREATE TABLE angsuran ( angsuranke INT AUTO_INCREMENT PRIMARY KEY, tanggal DATE, totalAngsuran DOUBLE, angsuranPokok DOUBLE, angsuranBunga DOUBLE, sisaPinjaman DOUBLE )";
			stat.execute(queryTable);
			
			for (int i = 0; i < lamapinjaman; i++) {
				dateFrom = dateFrom.plusMonths(1);
				double angsuranBunga;
				if (i<1) {
					angsuranBunga = consBunga * platfon;
				} else {
					angsuranBunga = consBunga * sisaPinjaman;
				}
				double angsuranPokok = totalAngsuran - angsuranBunga;
				
				if (i<1) {
					sisaPinjaman = platfon - angsuranPokok;
				} else {
					sisaPinjaman = sisaPinjaman - angsuranPokok;
				}
				
				String queryAngsuran = "INSERT INTO `angsuran`(`tanggal`, `totalAngsuran`, `angsuranPokok`, `angsuranBunga`, `sisaPinjaman`) VALUES ('%s', %f, %f, %f, %f)";
				queryAngsuran = String.format(queryAngsuran, dateFrom, totalAngsuran, sisaPinjaman, angsuranBunga, angsuranPokok);
				
				stat.execute(queryAngsuran);
				
				System.out.println(i+1+". tanggal = "+dateFrom+" | totalAngsuran = "+totalAngsuran+String.format(" | sisaPinjaman = %2f",sisaPinjaman)+" | angsuranBunga = "+angsuranBunga+" | angsuranPokok = "+angsuranPokok);
			}
			
			System.out.println();
			System.out.print("Ingin menampilkan data di PHPMyAdmin? (Y/T) ");
			String browse = scan.next().toUpperCase();
			switch (browse) {
			case "Y": {
				Desktop desktop = java.awt.Desktop.getDesktop();
				String url = "http://localhost/phpmyadmin/index.php?route=/sql&server=1&db="+db_name+"&table=angsuran&pos=0";
				URI oURL = new URI(url);
				desktop.browse(oURL);
				break;
			}case "T": {
				System.exit(0);
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + browse);
			}
			System.out.println();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
