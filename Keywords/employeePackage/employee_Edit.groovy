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
import java.util.Random as Random
import org.openqa.selenium.Keys

public class employee_Edit {
	@Keyword
	def editSuccess() {
		WebUI.click(findTestObject('employee/addEmployee/btn_manageEmployee'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-employee", false)

		WebUI.click(findTestObject('Object Repository/employee/edit/btnEdit'))

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
		WebUI.setText(findTestObject('Object Repository/employee/edit/txtName'), randomFullName)

		// Danh sách các giá trị có thể chọn
		List<String> options = ['male', 'female']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/employee/edit/selectGender'),
				options[randomIndex], false)

		WebUI.setText(findTestObject('Object Repository/employee/edit/txtDOB'), '')

		// Click vào ô nhập ngày sinh
		WebUI.click(findTestObject('Object Repository/employee/edit/txtDOB'))

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

		WebUI.setText(findTestObject('Object Repository/employee/edit/txtDOB'), randomDate)

		// Tạo độ dài số random từ 9 đến 12
		int length = new Random().nextInt(4) + 9 // Số ngẫu nhiên từ 9 đến 12

		// Tạo số ngẫu nhiên với độ dài đã chọn
		String randomCCCD = ''

		for (int i = 0; i < length; i++) {
			randomCCCD += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/employee/edit/txtCCCD'), randomCCCD)

		// Tạo số ngẫu nhiên có đúng 10 chữ số
		String randomPhone = "0"; // Đặt số đầu tiên là 0

		for (int i = 0; i < 9; i++) {
			randomPhone += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/employee/edit/txtPhoneNumber'), randomPhone)

		// Danh sách các giá trị có thể chọn
		List<String> optionsPos = ['guard', 'cleaner']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndexPos = new Random().nextInt(optionsPos.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/employee/edit/selectPosition'),
				optionsPos[randomIndexPos], false)

		// Giờ làm việc
		Random randWorkTime = new Random()

		// Giờ bắt đầu từ 0h đến 23h
		int startHour = randWorkTime.nextInt(24)  // Random từ 0 đến 23

		// Giờ kết thúc từ (startHour + 4) đến (startHour + 10), nhưng tối đa là 23h
		int endHour = Math.min(startHour + randWorkTime.nextInt(7) + 4, 23)

		String timeWork = startHour + "h - " + endHour + "h"

		WebUI.setText(findTestObject('Object Repository/employee/edit/txtWorkingTime'), timeWork)

		WebUI.click(findTestObject('Object Repository/employee/edit/btnUpdate'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Employee updated successfully!")) {
			WebUI.acceptAlert()
			println("Đã cập nhật thông tin nhân viên thành công")
			verifyEmployeeAfterEdit(randomPhone, randomCCCD, randomFullName, options[randomIndex],
					randomDate, optionsPos[randomIndexPos], timeWork)
		}

		WebUI.delay(3)
	}

	@Keyword
	def verifyEmployeeAfterEdit(String phoneNumber, String cccd, String expectedName, String expectedGender,
			String expectedDob, String expectedPosition, String expectedWorkingTime) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"

		Connection connection = null
		try {
			// Kết nối database
			connection = DriverManager.getConnection(url, username, password)

			// Truy vấn kiểm tra thông tin nhân viên sau khi edit
			String query = """
            SELECT name, gender, DATE_FORMAT(dob, '%d/%m/%Y') as formatted_dob, 
                   position, working_time, phone_number, cccd
            FROM employee 
            WHERE phone_number = ? AND cccd = ?
        """

			PreparedStatement statement = connection.prepareStatement(query)
			statement.setString(1, phoneNumber)
			statement.setString(2, cccd)

			ResultSet resultSet = statement.executeQuery()

			if (resultSet.next()) {
				println("=== THÔNG TIN NHÂN VIÊN TRONG DATABASE SAU KHI EDIT ===")
				println("Tên: " + resultSet.getString("name"))
				println("Giới tính: " + resultSet.getString("gender"))
				println("Ngày sinh: " + resultSet.getString("formatted_dob"))
				println("Vị trí: " + resultSet.getString("position"))
				println("Giờ làm việc: " + resultSet.getString("working_time"))
				println("SĐT: " + resultSet.getString("phone_number"))
				println("CCCD: " + resultSet.getString("cccd"))
			} else {
				println("❌ Không tìm thấy nhân viên trong database!")
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
		WebUI.click(findTestObject('Object Repository/employee/addEmployee/btn_manageEmployee'))

		WebUI.click(findTestObject('Object Repository/employee/edit/btnEdit'))

		// Để Name trống
		WebUI.sendKeys(findTestObject('Object Repository/employee/edit/txtName'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để DOB trống
		WebUI.sendKeys(findTestObject('Object Repository/employee/edit/txtDOB'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để CCCD trống
		WebUI.sendKeys(findTestObject('Object Repository/employee/edit/txtCCCD'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để sđt trống
		WebUI.sendKeys(findTestObject('Object Repository/employee/edit/txtPhoneNumber'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		// Để WorkingTime trống
		WebUI.sendKeys(findTestObject('Object Repository/employee/edit/txtWorkingTime'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		WebUI.click(findTestObject('Object Repository/employee/edit/btnUpdate'))

		WebUI.verifyElementText(findTestObject('Object Repository/employee/edit/requiredName'), 'Name is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/edit/requiredDOB'), 'Date of Birth is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/edit/requiredCCCD'), 'CCCD is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/edit/requiredPhone'), 'Phone Number is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/edit/requiredWorkingTime'), 'Working Time is required.')
	}

	@Keyword
	def editDuplicate() {
		WebUI.click(findTestObject('employee/addEmployee/btn_manageEmployee'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-employee", false)

		WebUI.click(findTestObject('Object Repository/employee/edit/btnEdit'))

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
		WebUI.setText(findTestObject('Object Repository/employee/edit/txtName'), randomFullName)

		// Danh sách các giá trị có thể chọn
		List<String> options = ['male', 'female']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/employee/edit/selectGender'),
				options[randomIndex], false)

		WebUI.setText(findTestObject('Object Repository/employee/edit/txtDOB'), '')

		// Click vào ô nhập ngày sinh
		WebUI.click(findTestObject('Object Repository/employee/edit/txtDOB'))

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

		WebUI.setText(findTestObject('Object Repository/employee/edit/txtDOB'), randomDate)

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/employee/edit/txtCCCD'), '123456789')

		// Tạo số ngẫu nhiên có đúng 10 chữ số
		String randomPhone = "0"; // Đặt số đầu tiên là 0

		for (int i = 0; i < 9; i++) {
			randomPhone += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/employee/edit/txtPhoneNumber'), randomPhone)

		// Danh sách các giá trị có thể chọn
		List<String> optionsPos = ['guard', 'cleaner']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndexPos = new Random().nextInt(optionsPos.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/employee/edit/selectPosition'),
				optionsPos[randomIndexPos], false)

		// Giờ làm việc
		Random randWorkTime = new Random()

		// Giờ bắt đầu từ 0h đến 23h
		int startHour = randWorkTime.nextInt(24)  // Random từ 0 đến 23

		// Giờ kết thúc từ (startHour + 4) đến (startHour + 10), nhưng tối đa là 23h
		int endHour = Math.min(startHour + randWorkTime.nextInt(7) + 4, 23)

		String timeWork = startHour + "h - " + endHour + "h"

		WebUI.setText(findTestObject('Object Repository/employee/edit/txtWorkingTime'), timeWork)

		WebUI.click(findTestObject('Object Repository/employee/edit/btnUpdate'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Error updating employee. Please try again.")) {
			WebUI.acceptAlert()
			println("Lỗi khi cập thật thông tin nhân viên.")
		}

		WebUI.delay(3)
		WebUI.closeBrowser()
	}
}
