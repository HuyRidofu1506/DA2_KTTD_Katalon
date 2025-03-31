package apartmentPackage

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

public class apartment_Delete {
	@Keyword
	def delete() {
		WebUI.click(findTestObject('Object Repository/apartment/add/btn_manageAparment'))

		String apartmentNumber = WebUI.getText(findTestObject('Object Repository/apartment/delete/txtShowNumApart'))
		println("🔍 Số phòng sẽ xóa: $apartmentNumber")
		
		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-apartment", false)

		WebUI.click(findTestObject('Object Repository/apartment/delete/btnDelete'))

		WebUI.waitForAlert(5) // Chờ tối đa 5 giây để alert xuất hiện
		String alertText = WebUI.getAlertText() // Lấy nội dung của alert

		if (alertText.contains('Are you sure you want to delete this apartment?')) {
			WebUI.dismissAlert() // Nhấn "Hủy"
		}

		WebUI.click(findTestObject('Object Repository/apartment/delete/btnDelete'))

		WebUI.waitForAlert(5)
		alertText = WebUI.getAlertText()

		if (alertText.contains('Are you sure you want to delete this apartment?')) {
			WebUI.acceptAlert() // Nhấn "OK"
		}

		WebUI.waitForAlert(5)
		String resultAlert = WebUI.getAlertText()
		
		if (resultAlert.contains('Apartment deleted successfully!')) {
			WebUI.acceptAlert() //  Nhấn "OK"
			println('Xóa căn hộ thành công')			
			verifyApartmentAfterDelete(apartmentNumber)
		}

		WebUI.delay(5)

	}
	
	@Keyword
	def verifyApartmentAfterDelete(String apartmentNumber) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"
	
		println("\n🔎 Đang kiểm tra căn hộ $apartmentNumber trong database...")
		
		Connection connection = null
		try {
			connection = DriverManager.getConnection(url, username, password)
			
			 // Kiểm tra trong bảng apartment
	        String query = "SELECT COUNT(*) as count FROM apartment WHERE number_of_apartment = ?"
	        PreparedStatement stmt = connection.prepareStatement(query)
	        stmt.setString(1, apartmentNumber)
	        
	        ResultSet rs = stmt.executeQuery()
	        rs.next()
	        int count = rs.getInt("count")
	        
	        if (count == 0) {
	            println("✅ Đã xóa thành công căn hộ $apartmentNumber khỏi database")
	        } else {
	            println("❌ Căn hộ $apartmentNumber vẫn tồn tại trong database")
			}
			
		} catch (SQLException e) {
			println("💥 Lỗi database: " + e.getMessage())
		} finally {
			connection?.close()
		}
	}
}
