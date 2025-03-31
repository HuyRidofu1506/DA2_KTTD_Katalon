package employeePackage

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

public class employee_Delete {
	@Keyword
	def delete() {
		WebUI.click(findTestObject('employee/addEmployee/btn_manageEmployee'))

		String employeePhone = WebUI.getText(findTestObject('Object Repository/employee/deleteEmployee/txtShowPhone'))
		String employeeCCCD = WebUI.getText(findTestObject('Object Repository/employee/deleteEmployee/txtShowCCCD'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-employee", false)

		WebUI.click(findTestObject('Object Repository/employee/deleteEmployee/btnDelete'))

		WebUI.waitForAlert(5) // Chờ tối đa 5 giây để alert xuất hiện
		String alertText = WebUI.getAlertText() // Lấy nội dung của alert

		if (alertText.contains('Are you sure you want to delete this employee?')) {
			WebUI.dismissAlert() // Nhấn "Hủy"
		}

		WebUI.click(findTestObject('Object Repository/employee/deleteEmployee/btnDelete'))

		WebUI.waitForAlert(5)
		alertText = WebUI.getAlertText()

		if (alertText.contains('Are you sure you want to delete this employee?')) {
			WebUI.acceptAlert() // Nhấn "OK"
		}

		WebUI.waitForAlert(5)
		String resultAlert = WebUI.getAlertText()

		if (resultAlert.contains('Employee deleted successfully!')) {
			WebUI.acceptAlert() //  Nhấn "OK"
			println('Xóa cư dân thành công')
			verifyEmployeeAfterDelete(employeePhone, employeeCCCD)
		}

		WebUI.delay(5)
	}

	@Keyword
	def verifyEmployeeAfterDelete(String phoneNumber, String cccd) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"

		println("Bắt đầu verify với SĐT: $phoneNumber, CCCD: $cccd")

		Connection connection = null
		try {
			// Kết nối database
			connection = DriverManager.getConnection(url, username, password)

			// Kiểm tra trong bảng employee
			String employeeQuery = """
            SELECT COUNT(*) as employee_count 
            FROM employee 
            WHERE phone_number = ? OR cccd = ?
        """

			PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery)
			employeeStmt.setString(1, phoneNumber)
			employeeStmt.setString(2, cccd)

			ResultSet employeeRs = employeeStmt.executeQuery()
			employeeRs.next()
			int employeeCount = employeeRs.getInt("employee_count")

			// 3. Kiểm tra trong bảng deleted_employee (nếu có)
			String deletedQuery = """
            SELECT COUNT(*) as deleted_count 
            FROM employee 
            WHERE phone_number = ? OR cccd = ?
        """

			PreparedStatement deletedStmt = connection.prepareStatement(deletedQuery)
			deletedStmt.setString(1, phoneNumber)
			deletedStmt.setString(2, cccd)

			ResultSet deletedRs = deletedStmt.executeQuery()
			deletedRs.next()
			int deletedCount = deletedRs.getInt("deleted_count")

			// 4. Đánh giá kết quả
			if (employeeCount == 0) {
				if (deletedCount > 0) {
					println("✅ Nhân viên đã được chuyển vào bảng deleted_employee")
					return true
				} else {
					println("✅ Nhân viên đã được xóa hoàn toàn khỏi hệ thống")
					return true
				}
			} else {
				println("❌ LỖI: Vẫn tìm thấy $employeeCount bản ghi trong bảng employee")
			}
		} catch (SQLException e) {
			println("Lỗi khi truy vấn database: " + e.getMessage())
			return false
		} finally {
			if (connection != null) {
				connection.close()
			}
		}
	}
}
