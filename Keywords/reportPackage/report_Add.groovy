package reportPackage

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
import java.sql.*
import java.text.SimpleDateFormat

public class report_Add {
	@Keyword
	def addSuccess() {
		WebUI.click(findTestObject('Object Repository/report/add/btn_manageReport'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-report", false)

		WebUI.click(findTestObject('Object Repository/report/add/btnAdd'))

		// Tạo dữ liệu random
		String reportName = "Báo cáo " + new Random().nextInt(12) + 1
		String reportContent = "Test báo cáo"

		// Random ngày, tháng, năm
		int randomDay = new Random().nextInt(28) + 1 // Chọn từ 1 - 28 (tránh lỗi tháng 2)
		int randomMonth = new Random().nextInt(12) + 1 // Chọn từ 1 - 12
		int randomYear = 1970 + new Random().nextInt(45 // Chọn từ 1980 - 2025
				)

		// Định dạng ngày tháng (thêm số 0 nếu cần)
		String formattedDay = randomDay < 10 ? '0' + randomDay : randomDay.toString()
		String formattedMonth = randomMonth < 10 ? '0' + randomMonth : randomMonth.toString()

		// Nhập ngày vào ô DatePicker
		String randomDate = (((formattedDay + '/') + formattedMonth) + '/') + randomYear
		WebUI.setText(findTestObject('Object Repository/report/add/txtName'), reportName)

		WebUI.setText(findTestObject('Object Repository/report/add/txtDate'), randomDate)

		WebUI.setText(findTestObject('Object Repository/report/add/txtContent'), reportContent)

		WebUI.click(findTestObject('Object Repository/report/add/btnSubmit'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()
		if (alertText.contains("Report added successfully!")) {
			WebUI.acceptAlert()
			println("Đã thêm báo cáo thành công")
			verifyReportInDatabase(reportName, reportContent)
		}

		WebUI.delay(3)
	}

	@Keyword
	def verifyReportInDatabase(String reportName, String content) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"

		Connection connection = null
		try {
			connection = DriverManager.getConnection(url, username, password)
	
	        // Query kiểm tra
	        String query = """
	            SELECT * FROM report 
	            WHERE name = ? 
	            AND content = ?
	            ORDER BY id DESC 
	            LIMIT 1
	        """
	
	        PreparedStatement statement = connection.prepareStatement(query)
	        statement.setString(1, reportName)
	        statement.setString(2, content)


			ResultSet resultSet = statement.executeQuery()

			if (resultSet.next()) {
	            println("\n=== THÔNG TIN BÁO CÁO TRONG DATABASE ===")
	            println("ID: " + resultSet.getInt("id"))
	            println("Tên báo cáo: " + resultSet.getString("name"))
	            println("Nội dung: " + resultSet.getString("content"))
	            return true

			} else {
				println("❌ Không tìm thấy báo cáo trong database")
				return false
			}
		} catch (SQLException e) {
			println("Lỗi khi truy vấn database: " + e.getMessage())
			return false
		} finally {
			connection?.close()
		}
	}

	@Keyword
	def addRequired() {
		WebUI.click(findTestObject('Object Repository/report/add/btn_manageReport'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-report", false)

		WebUI.click(findTestObject('Object Repository/report/add/btnAdd'))

		WebUI.click(findTestObject('Object Repository/report/add/btnSubmit'))

		WebUI.verifyElementText(findTestObject('Object Repository/report/add/requiredName'), 'Name is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/report/add/txtDate'), 'Date is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/report/add/txtContent'), 'Content is required.')
	}
}
