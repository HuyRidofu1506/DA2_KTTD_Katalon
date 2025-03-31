package database

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

public class DBConnection {
	private static Connection connection = null

	// Hàm kết nối Database
	@Keyword
	def connectToDB(String dbUrl, String dbUser, String dbPassword) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver")
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
			println("✅ Kết nối MySQL thành công!")
		} catch (Exception e) {
			println("❌ Kết nối thất bại: " + e.getMessage())
		}
	}

	// Hàm thực thi truy vấn SELECT
	@Keyword
	def executeQuery(String query) {
		if (connection == null) {
			println("⚠️ Chưa kết nối Database!")
			return null
		}

		Statement statement = connection.createStatement()
		ResultSet resultSet = statement.executeQuery(query)
		return resultSet
	}

	// Hàm đóng kết nối Database
	@Keyword
	def closeDBConnection() {
		if (connection != null) {
			connection.close()
			println("🔌 Đóng kết nối Database!")
		}
	}
}
