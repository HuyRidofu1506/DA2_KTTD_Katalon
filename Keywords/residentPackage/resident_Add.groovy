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
import java.util.Random as Random
import java.sql.*

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

public class resident_Add {
	private ResultSet resultSet
	private PreparedStatement statement
	private Connection connection

	@Keyword
	def addSuccess() {
		WebUI.click(findTestObject('Object Repository/resident/manageHome/btn_manageResident'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-resident", false)

		WebUI.click(findTestObject('resident/manageHome/btnAdd'))

		// Danh sách họ và tên mẫu
		String[] firstNames = [
			"An",
			"Bình",
			"Chi",
			"Dũng",
			"Hà",
			"Khoa",
			"Linh",
			"Minh",
			"Nam",
			"Phương",
			"Quang",
			"Tâm",
			"Trang",
			"Vũ"
		]
		String[] lastNames = [
			"Nguyễn",
			"Trần",
			"Lê",
			"Phạm",
			"Hoàng",
			"Vũ",
			"Đặng",
			"Bùi",
			"Đỗ",
			"Hồ",
			"Ngô",
			"Dương",
			"Lý",
			"Cao"
		]

		// Random chọn họ và tên
		Random rand = new Random()
		String randomLastName = lastNames[rand.nextInt(lastNames.length)]
		String randomFirstName = firstNames[rand.nextInt(firstNames.length)]

		// Ghép tên đầy đủ
		String randomFullName = randomLastName + " " + randomFirstName

		// Nhập vào ô text
		WebUI.setText(findTestObject('Object Repository/resident/addResident/txtName'), randomFullName)

		// Danh sách các giá trị có thể chọn
		List<String> options = ['male', 'female']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/resident/addResident/selectGender'),
				options[randomIndex], false)

		WebUI.setText(findTestObject('resident/addResident/txtDOB'), '')

		// Click vào ô nhập ngày sinh
		WebUI.click(findTestObject('Object Repository/resident/addResident/txtDOB'))

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

		WebUI.setText(findTestObject('Object Repository/resident/addResident/txtDOB'), randomDate)

		// Tạo độ dài số random từ 9 đến 12
		int length = new Random().nextInt(4) + 9 // Số ngẫu nhiên từ 9 đến 12

		// Tạo số ngẫu nhiên với độ dài đã chọn
		String randomCCCD = ''

		for (int i = 0; i < length; i++) {
			randomCCCD += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/resident/addResident/txtCCCD'), randomCCCD)

		// Tạo số ngẫu nhiên có đúng 10 chữ số
		String randomPhone = "0"; // Đặt số đầu tiên là 0

		for (int i = 0; i < 9; i++) {
			randomPhone += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/resident/addResident/txtPhoneNumber'), randomPhone)

		WebUI.click(findTestObject('resident/addResident/btnSubmit'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Resident added successfully!")) {
			WebUI.acceptAlert()
			println("Đã nhấn OK trên Alert")
		}

		WebUI.delay(5)
		// Gọi hàm kiểm tra sau khi thêm cư dân
		verifyResidentInDatabase(randomPhone, randomCCCD)
	}

	@Keyword
	def verifyResidentInDatabase(String phoneNumber, String cccd) {
		// Thông tin kết nối database (thay đổi theo cấu hình của bạn)
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"

		connection = null
		try {
			// Kết nối database
			connection = DriverManager.getConnection(url, username, password)

			// Truy vấn kiểm tra cư dân vừa thêm bằng SĐT hoặc CCCD
			String query = "SELECT * FROM resident WHERE phone_number = ? AND cccd = ?"
			statement = connection.prepareStatement(query)
			statement.setString(1, phoneNumber)
			statement.setString(2, cccd)

			resultSet = statement.executeQuery()

			// Kiểm tra có dữ liệu hay không
			if (resultSet.next()) {
				println("✅ Dữ liệu đã được thêm vào database:")
				println("Tên: " + resultSet.getString("name"))
				println("Giới tính: " + resultSet.getString("gender"))
				println("Ngày sinh: " + resultSet.getString("dob"))
				println("CCCD: " + resultSet.getString("cccd"))
				println("SĐT: " + resultSet.getString("phone_number"))
				return true
			} else {
				println("❌ Không tìm thấy dữ liệu trong database!")
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
		WebUI.click(findTestObject('Object Repository/resident/manageHome/btn_manageResident'))

		WebUI.click(findTestObject('Object Repository/resident/manageHome/btnAdd'))

		WebUI.click(findTestObject('Object Repository/resident/addResident/btnSubmit'))

		WebUI.verifyElementText(findTestObject('Object Repository/resident/addResident/requiredName'), 'Name is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/resident/addResident/requiredGender'), 'Gender is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/resident/addResident/requiredDOB'), 'Date of Birth is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/resident/addResident/requiredCCCD'), 'CCCD is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/resident/addResident/requiredPhone'), 'Phone Number is required.')

		WebUI.click(findTestObject('Object Repository/resident/manageHome/btn_manageResident'))
	}

	@Keyword
	def addDuplicate() {
		WebUI.click(findTestObject('Object Repository/resident/manageHome/btn_manageResident'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-resident", false)

		WebUI.click(findTestObject('resident/manageHome/btnAdd'))

		// Danh sách họ và tên mẫu
		String[] firstNames = [
			"An",
			"Bình",
			"Chi",
			"Dũng",
			"Hà",
			"Khoa",
			"Linh",
			"Minh",
			"Nam",
			"Phương",
			"Quang",
			"Tâm",
			"Trang",
			"Vũ"
		]
		String[] lastNames = [
			"Nguyễn",
			"Trần",
			"Lê",
			"Phạm",
			"Hoàng",
			"Vũ",
			"Đặng",
			"Bùi",
			"Đỗ",
			"Hồ",
			"Ngô",
			"Dương",
			"Lý",
			"Cao"
		]

		// Random chọn họ và tên
		Random rand = new Random()
		String randomLastName = lastNames[rand.nextInt(lastNames.length)]
		String randomFirstName = firstNames[rand.nextInt(firstNames.length)]

		// Ghép tên đầy đủ
		String randomFullName = randomLastName + " " + randomFirstName

		// Nhập vào ô text
		WebUI.setText(findTestObject('Object Repository/resident/addResident/txtName'), randomFullName)

		// Danh sách các giá trị có thể chọn
		List<String> options = ['male', 'female']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/resident/addResident/selectGender'),
				options[randomIndex], false)

		WebUI.setText(findTestObject('resident/addResident/txtDOB'), '')

		// Click vào ô nhập ngày sinh
		WebUI.click(findTestObject('Object Repository/resident/addResident/txtDOB'))

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

		WebUI.setText(findTestObject('Object Repository/resident/addResident/txtDOB'), randomDate)

		// Nhập trùng số CCCD
		String numberCCCD = '123456789'

		WebUI.setText(findTestObject('Object Repository/resident/addResident/txtCCCD'), numberCCCD)

		// Nhập trùng sđt
		String numberPhone = '0123456789'

		WebUI.setText(findTestObject('Object Repository/resident/addResident/txtPhoneNumber'), numberPhone)

		WebUI.click(findTestObject('resident/addResident/btnSubmit'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Failed to add resident. Please try again.")) {
			WebUI.acceptAlert()
		}

		println("Đã nhấn OK trên Alert")

		WebUI.delay(5)
	}
}
