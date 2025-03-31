package billPackage

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

public class bill_Detail_Delete {
	@Keyword
	def delete_Detail() {
		WebUI.click(findTestObject('Object Repository/bill/add/btn_manageBill'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-bill", false)

		WebUI.click(findTestObject('Object Repository/bill/detail_Delete/btnDetail'))
		WebUI.delay(2)
		WebUI.click(findTestObject('Object Repository/bill/add/btn_manageBill'))

		// Bước 1: Lấy thông tin hóa đơn trước khi xóa
		String billCode = WebUI.getText(findTestObject('Object Repository/bill/detail_Delete/txtShowBillCode'))
		println("🔍 Mã hóa đơn sẽ xóa: $billCode")

		WebUI.click(findTestObject('Object Repository/bill/detail_Delete/btnDelete'))

		WebUI.waitForAlert(5) // Chờ tối đa 5 giây để alert xuất hiện

		String alertText = WebUI.getAlertText() // Lấy nội dung của alert
		if (alertText.contains('Are you sure you want to delete this bill?')) {
			WebUI.dismissAlert() // Nhấn "Hủy"
		}

		WebUI.click(findTestObject('Object Repository/bill/detail_Delete/btnDelete'))
		WebUI.waitForAlert(5)

		if (alertText.contains('Are you sure you want to delete this bill?')) {
			WebUI.acceptAlert() // Nhấn "OK"
		}

		WebUI.waitForAlert(5)

		String resurlAlert = WebUI.getAlertText()
		if (resurlAlert.contains('Bill deleted successfully!')) {
			WebUI.acceptAlert() //  Nhấn "OK"
			println('Xóa hóa đơn thành công')
			verifyBillAfterDelete(billCode)
		}

		WebUI.delay(5)
	}

	@Keyword
	def verifyBillAfterDelete(String billCode) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"

		println("\n🔎 Đang kiểm tra hóa đơn $billCode trong database...")

		Connection connection = null
		try {
			connection = DriverManager.getConnection(url, username, password)

			// Kiểm tra trong bảng bill
			String query = "SELECT COUNT(*) as count FROM bill WHERE code_bill = ?"
			PreparedStatement stmt = connection.prepareStatement(query)
			stmt.setString(1, billCode)

			ResultSet rs = stmt.executeQuery()
			rs.next()
			int count = rs.getInt("count")

			if (count == 0) {
				println("✅ Đã xóa thành công hóa đơn $billCode khỏi database")
				return true
			} else {
				println("❌ Hóa đơn $billCode vẫn tồn tại trong database")
			}
		} catch (SQLException e) {
			println("💥 Lỗi khi truy vấn database: " + e.getMessage())
			return false
		} finally {
			connection?.close()
		}
	}
}
