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

import org.openqa.selenium.Keys

public class resident_Edit {
	@Keyword
	def editSuccess() {
		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-resident", false)

		WebUI.click(findTestObject('resident/manageHome/btnEdit'))

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
		WebUI.setText(findTestObject('Object Repository/resident/editResident/txtName'), randomFullName)

		// Danh sách các giá trị có thể chọn
		List<String> options = ['male', 'female']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/resident/editResident/selectGender'),
				options[randomIndex], false)

		WebUI.setText(findTestObject('resident/editResident/txtDOB'), '')

		// Click vào ô nhập ngày sinh
		WebUI.click(findTestObject('Object Repository/resident/editResident/txtDOB'))

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

		WebUI.setText(findTestObject('Object Repository/resident/editResident/txtDOB'), randomDate)

		// Tạo độ dài số random từ 9 đến 12
		int length = new Random().nextInt(4) + 9 // Số ngẫu nhiên từ 9 đến 12

		// Tạo số ngẫu nhiên với độ dài đã chọn
		String randomCCCD = ''

		for (int i = 0; i < length; i++) {
			randomCCCD += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/resident/editResident/txtCCCD'), randomCCCD)

		// Tạo số ngẫu nhiên có đúng 10 chữ số
		String randomPhone = "0"; // Đặt số đầu tiên là 0

		for (int i = 0; i < 9; i++) {
			randomPhone += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/resident/editResident/txtPhoneNumber'), randomPhone)

		WebUI.click(findTestObject('resident/editResident/btnUpdate'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Resident updated successfully!")) {
			WebUI.acceptAlert()
			verifyResidentAfterEdit(randomPhone, randomCCCD, randomFullName, options[randomIndex], randomDate)
		}

		println("Đã nhấn OK trên Alert")

		WebUI.delay(5)
	}

	@Keyword
	def verifyResidentAfterEdit(String phoneNumber, String cccd, String expectedName, String expectedGender, String expectedDob) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"

		Connection connection = null
		try {
			// Kết nối database
			connection = DriverManager.getConnection(url, username, password)

			// Truy vấn kiểm tra thông tin cư dân sau khi edit
			String query = """
            SELECT name, gender, DATE_FORMAT(dob, '%d/%m/%Y') as formatted_dob, 
                   cccd, phone_number 
            FROM resident 
            WHERE phone_number = ? OR cccd = ?
        """

			PreparedStatement statement = connection.prepareStatement(query)
			statement.setString(1, phoneNumber)
			statement.setString(2, cccd)

			ResultSet resultSet = statement.executeQuery()

			if (resultSet.next()) {
				println("=== THÔNG TIN TRONG DATABASE SAU KHI EDIT ===")
				println("Tên: " + resultSet.getString("name"))
				println("Giới tính: " + resultSet.getString("gender"))
				println("Ngày sinh: " + resultSet.getString("formatted_dob"))
				println("CCCD: " + resultSet.getString("cccd"))
				println("SĐT: " + resultSet.getString("phone_number"))

				// Verify từng trường dữ liệu
				boolean isVerified = true

				if (!resultSet.getString("name").equals(expectedName)) {
					println("❌ Tên không khớp: DB=${resultSet.getString("name")} | Expected=${expectedName}")
					isVerified = false
				}

				if (!resultSet.getString("gender").equals(expectedGender)) {
					println("❌ Giới tính không khớp: DB=${resultSet.getString("gender")} | Expected=${expectedGender}")
					isVerified = false
				}

				if (!resultSet.getString("formatted_dob").equals(expectedDob)) {
					println("❌ Ngày sinh không khớp: DB=${resultSet.getString("formatted_dob")} | Expected=${expectedDob}")
					isVerified = false
				}

				if (isVerified) {
					println("✅ Tất cả thông tin đã được cập nhật chính xác")
					return true
				} else {
					return false
				}
			} else {
				println("❌ Không tìm thấy cư dân trong database!")
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

	@Keyword
	def editRequired() {
		WebUI.click(findTestObject('resident/manageHome/btn_manageResident'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-resident", false)

		WebUI.click(findTestObject('resident/manageHome/btnEdit'))

		// Nhập vào ô text
		WebUI.sendKeys(findTestObject('resident/editResident/txtName'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để DOB trống
		WebUI.sendKeys(findTestObject('resident/editResident/txtDOB'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để CCCD trống
		WebUI.sendKeys(findTestObject('resident/editResident/txtCCCD'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để sđt trống
		WebUI.sendKeys(findTestObject('resident/editResident/txtPhoneNumber'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		WebUI.click(findTestObject('resident/editResident/btnUpdate'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Please fill in all required fields correctly.")) {
			WebUI.acceptAlert()
		}

		println("Đã nhấn OK trên Alert")

		WebUI.verifyElementText(findTestObject('Object Repository/resident/editResident/requiredName'), 'Name is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/resident/editResident/requiredDOB'), 'Date of Birth is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/resident/editResident/requiredCCCD'), 'CCCD is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/resident/editResident/requiredPhone'), 'Phone Number is required.')

		WebUI.delay(5)
	}

	@Keyword
	def editDuplicate() {
		WebUI.click(findTestObject('resident/manageHome/btn_manageResident'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-resident", false)

		WebUI.click(findTestObject('resident/manageHome/btnEdit'))

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
		WebUI.setText(findTestObject('Object Repository/resident/editResident/txtName'), randomFullName)

		// Danh sách các giá trị có thể chọn
		List<String> options = ['male', 'female']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/resident/editResident/selectGender'),
				options[randomIndex], false)

		WebUI.setText(findTestObject('resident/editResident/txtDOB'), '')

		// Click vào ô nhập ngày sinh
		WebUI.click(findTestObject('Object Repository/resident/editResident/txtDOB'))

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

		WebUI.setText(findTestObject('Object Repository/resident/editResident/txtDOB'), randomDate)

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/resident/editResident/txtCCCD'), '123456789')

		// Tạo số ngẫu nhiên có đúng 10 chữ số
		String randomPhone = "0"; // Đặt số đầu tiên là 0

		for (int i = 0; i < 9; i++) {
			randomPhone += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/resident/editResident/txtPhoneNumber'), randomPhone)

		WebUI.click(findTestObject('resident/editResident/btnUpdate'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Error updating resident. Please try again.")) {
			WebUI.acceptAlert()
		}

		println("Đã nhấn OK trên Alert")

		WebUI.delay(5)
	}
}
