package residentPackage

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

public class resident_Delete {
	@Keyword
	def delete() {
		// Lấy thông tin cư dân trước khi xóa (cần thêm code để lấy các giá trị này)
		String residentPhone = WebUI.getText(findTestObject('Object Repository/resident/deleteResident/txtShowPhone'))
		String residentCCCD = WebUI.getText(findTestObject('Object Repository/resident/deleteResident/txtShowCCCD'))

		WebUI.click(findTestObject('resident/deleteResident/btnDelete'))

		WebUI.waitForAlert(5) // Chờ tối đa 5 giây để alert xuất hiện
		String alertText = WebUI.getAlertText() // Lấy nội dung của alert

		if (alertText.contains('Are you sure you want to delete this resident?')) {
			WebUI.dismissAlert() // Nhấn "Hủy"
		}

		WebUI.click(findTestObject('resident/deleteResident/btnDelete'))

		WebUI.waitForAlert(5)
		alertText = WebUI.getAlertText()

		if (alertText.contains('Are you sure you want to delete this resident?')) {
			WebUI.acceptAlert() // Nhấn "OK"
		}

		WebUI.waitForAlert(5)
		String resultAlert = WebUI.getAlertText()

		if (resultAlert.contains('Resident deleted successfully!')) {
			WebUI.acceptAlert() //  Nhấn "OK"
			println('Xóa cư dân thành công')
			verifyResidentAfterDelete(residentPhone, residentCCCD)
		}

		WebUI.delay(5)
	}

	@Keyword
	def verifyResidentAfterDelete(String phoneNumber, String cccd) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"

		Connection connection = null
		try {
			// Kết nối database
			connection = DriverManager.getConnection(url, username, password)

			// Truy vấn kiểm tra cư dân đã bị xóa
			String query = """
            SELECT COUNT(*) as count 
            FROM resident 
            WHERE phone_number = ? OR cccd = ?
        """

			PreparedStatement statement = connection.prepareStatement(query)
			statement.setString(1, phoneNumber)
			statement.setString(2, cccd)

			ResultSet resultSet = statement.executeQuery()
			resultSet.next()

			int recordCount = resultSet.getInt("count")

			if (recordCount == 0) {
				println("✅ Cư dân đã được xóa thành công khỏi database")
				return true
			} else {
				println("❌ LỖI: Vẫn tìm thấy ${recordCount} bản ghi trong database")
				return false
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
