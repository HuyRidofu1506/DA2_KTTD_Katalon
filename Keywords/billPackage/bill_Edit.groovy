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
import org.openqa.selenium.Keys

public class bill_Edit {
	@Keyword
	def editSuccess() {
		WebUI.click(findTestObject('Object Repository/bill/add/btn_manageBill'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-bill", false)

		WebUI.click(findTestObject('Object Repository/bill/edit/btnEdit'))

		// Lấy danh sách apartment từ database
		List<String> apartmentList = getApartmentsFromDatabase()
		if (apartmentList.isEmpty()) {
			println("Không có apartment nào trong database")
			return
		}
		// Chọn random apartment
		String selectedNumOfApart = apartmentList[new Random().nextInt(apartmentList.size())]
		TestObject dropdownNum = findTestObject('Object Repository/bill/add/selectNumOfApart')
		WebUI.selectOptionByLabel(dropdownNum, selectedNumOfApart, false)
		println("Đã chọn phòng: $selectedNumOfApart")

		// Lấy danh sách resident từ database
		List<String> residentList = getResidentsFromDatabase()
		if (residentList.isEmpty()) {
			println("Không có resident nào trong database")
			return
		}

		// Chọn random resident
		String selectedName = residentList[new Random().nextInt(residentList.size())]
		TestObject dropdownRes = findTestObject('Object Repository/bill/add/selectResident')
		WebUI.selectOptionByLabel(dropdownRes, selectedName, false)
		println("Đã chọn resident: $selectedName")

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

		WebUI.setText(findTestObject('Object Repository/bill/add/txtDate'), randomDate)

		WebUI.setText(findTestObject('Object Repository/bill/edit/txtNumberElec'), "300")

		WebUI.setText(findTestObject('Object Repository/bill/edit/txtNumberWater'), "20")

		WebUI.setText(findTestObject('Object Repository/bill/edit/txtToiletMoney'), "20000")

		// Danh sách các giá trị có thể chọn
		List<String> options = ['Pay', 'NotPaid']
		String selectedStatus = options[new Random().nextInt(options.size())]
		WebUI.selectOptionByValue(findTestObject('Object Repository/bill/add/selectStatus'), selectedStatus, false)

		WebUI.click(findTestObject('Object Repository/bill/edit/btnUpdate'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()
		if (alertText.contains("Bill updated successfully!")) {
			WebUI.acceptAlert()
			println("Đã sửa thông tin hóa đơn thành công")
			verifyBillAfterEdit(selectedNumOfApart, selectedName, randomDate, selectedStatus)
		}

		WebUI.delay(3)

	}
	
	@Keyword
	def verifyBillAfterEdit(String apartmentNumber, String residentName, String date, String expectedStatus) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"
	
		Connection connection = null
		try {
			connection = DriverManager.getConnection(url, username, password)
			
			// Query kiểm tra bill mới nhất theo apartment, resident và date
			String query = """
            SELECT * FROM bill 
            WHERE number_of_apartment = ? 
            AND resident_name = ?
            AND date = ?
            ORDER BY updated_at DESC 
            LIMIT 1
        """
			
			PreparedStatement statement = connection.prepareStatement(query)
			statement.setString(1, apartmentNumber)
			statement.setString(2, residentName)
			statement.setString(3, date)
			
			ResultSet resultSet = statement.executeQuery()
			
			if (resultSet.next()) {
				println("\n=== THÔNG TIN HÓA ĐƠN SAU KHI EDIT ===")
				println("Số phòng: " + resultSet.getString("number_of_apartment"))
				println("Tên cư dân: " + resultSet.getString("resident_name"))
				println("Ngày: " + resultSet.getString("date"))
				println("Số điện: " + resultSet.getInt("number_electric"))
				println("Số nước: " + resultSet.getInt("number_water"))
				println("Tiền vệ sinh: " + resultSet.getInt("toilet_money"))
				println("Trạng thái: " + resultSet.getString("status"))
				println("Tổng tiền: " + resultSet.getLong("total"))
				
			}
		} catch (SQLException e) {
			println("Lỗi khi truy vấn database: " + e.getMessage())
			return false
		} finally {
			connection?.close()
		}
	}
	
	// Hàm lấy danh sách apartment từ database
	@Keyword
	def List<String> getApartmentsFromDatabase() {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"
		List<String> apartments = []

		Connection connection = null
		try {
			connection = DriverManager.getConnection(url, username, password)
			String query = "SELECT number_of_apartment FROM apartment WHERE status = 'available'"
			Statement statement = connection.createStatement()
			ResultSet resultSet = statement.executeQuery(query)

			while (resultSet.next()) {
				apartments.add(resultSet.getString("number_of_apartment"))
			}
		} catch (SQLException e) {
			println("Lỗi khi lấy apartment từ database: " + e.getMessage())
		} finally {
			connection?.close()
		}
		return apartments
	}

	// Hàm lấy danh sách resident từ database
	@Keyword
	def List<String> getResidentsFromDatabase() {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"
		List<String> residents = []

		Connection connection = null
		try {
			connection = DriverManager.getConnection(url, username, password)
			String query = "SELECT name FROM resident"
			Statement statement = connection.createStatement()
			ResultSet resultSet = statement.executeQuery(query)

			while (resultSet.next()) {
				residents.add(resultSet.getString("name"))
			}
		} catch (SQLException e) {
			println("Lỗi khi lấy resident từ database: " + e.getMessage())
		} finally {
			connection?.close()
		}
		return residents
	}
	
	@Keyword
	def editRequired() {
		WebUI.click(findTestObject('Object Repository/bill/add/btn_manageBill'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-bill", false)

		WebUI.click(findTestObject('Object Repository/bill/edit/btnEdit'))

		// Để Date trống
		WebUI.sendKeys(findTestObject('Object Repository/bill/add/txtDate'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để Tiền điện trống
		WebUI.sendKeys(findTestObject('Object Repository/bill/add/txtNumElec'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để Tiền nước trống
		WebUI.sendKeys(findTestObject('Object Repository/bill/add/txtNumWater'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để Tiền Toilet trống
		WebUI.sendKeys(findTestObject('Object Repository/bill/add/txtToiletMoney'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		WebUI.click(findTestObject('Object Repository/bill/edit/btnUpdate'))

		WebUI.verifyElementText(findTestObject('Object Repository/bill/add/requiredDate'), 'Date is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/bill/add/requiredNumElec'), 'Number electric is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/bill/add/requiredNumWater'), 'Number water is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/bill/add/requiredToiletMoney'), 'Toilet money is required.')

	}
}
