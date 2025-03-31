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
import java.util.Random as Random
import java.sql.*

public class employee_Add {
	@Keyword
	def addSuccess() {
		WebUI.click(findTestObject('employee/addEmployee/btn_manageEmployee'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-employee", false)

		WebUI.click(findTestObject('Object Repository/employee/addEmployee/btnAdd'))

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
		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtName'), randomFullName)

		// Danh sách các giá trị có thể chọn
		List<String> options = ['male', 'female']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/employee/addEmployee/selectGender'),
				options[randomIndex], false)

		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtDOB'), '')

		// Click vào ô nhập ngày sinh
		WebUI.click(findTestObject('Object Repository/employee/addEmployee/txtDOB'))

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

		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtDOB'), randomDate)

		// Tạo độ dài số random từ 9 đến 12
		int length = new Random().nextInt(4) + 9 // Số ngẫu nhiên từ 9 đến 12

		// Tạo số ngẫu nhiên với độ dài đã chọn
		String randomCCCD = ''

		for (int i = 0; i < length; i++) {
			randomCCCD += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtCCCD'), randomCCCD)

		// Tạo số ngẫu nhiên có đúng 10 chữ số
		String randomPhone = "0"; // Đặt số đầu tiên là 0

		for (int i = 0; i < 9; i++) {
			randomPhone += new Random().nextInt(10 // Chọn số từ 0-9
					)
		}

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtPhoneNumber'), randomPhone)

		// Danh sách các giá trị có thể chọn
		List<String> optionsPos = ['guard', 'cleaner']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndexPos = new Random().nextInt(optionsPos.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/employee/addEmployee/selectPosition'),
				optionsPos[randomIndexPos], false)

		// Giờ làm việc
		Random randWorkTime = new Random()

		// Giờ bắt đầu từ 0h đến 23h
		int startHour = randWorkTime.nextInt(24)  // Random từ 0 đến 23

		// Giờ kết thúc từ (startHour + 4) đến (startHour + 10), nhưng tối đa là 23h
		int endHour = Math.min(startHour + randWorkTime.nextInt(7) + 4, 23)

		String timeWork = startHour + "h - " + endHour + "h"

		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtWorkingTime'), timeWork)

		WebUI.click(findTestObject('Object Repository/employee/addEmployee/btnSubmit'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Employee added successfully!")) {
			WebUI.acceptAlert()
			println("Đã thêm nhân viên thành công")
			verifyEmployeeInDatabase(randomPhone, randomCCCD)
		}

		WebUI.delay(3)
	}

	@Keyword
	def verifyEmployeeInDatabase(String phoneNumber, String cccd) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"

		Connection connection = null
		try {
			// Kết nối database
			connection = DriverManager.getConnection(url, username, password)

			// Truy vấn kiểm tra nhân viên vừa thêm
			String query = """
            SELECT e.*, 
                   DATE_FORMAT(e.dob, '%d/%m/%Y') as formatted_dob
            FROM employee e
            WHERE e.phone_number = ? 
            AND e.cccd = ?
        """

			PreparedStatement statement = connection.prepareStatement(query)
			statement.setString(1, phoneNumber)
			statement.setString(2, cccd)

			ResultSet resultSet = statement.executeQuery()

			// Kiểm tra dữ liệu
			if (resultSet.next()) {
				println("✅ Dữ liệu nhân viên đã được thêm vào database:")
				println("Tên: " + resultSet.getString("name"))
				println("Giới tính: " + resultSet.getString("gender"))
				println("Ngày sinh: " + resultSet.getString("formatted_dob"))
				println("CCCD: " + resultSet.getString("cccd"))
				println("SĐT: " + resultSet.getString("phone_number"))
				println("Vị trí: " + resultSet.getString("position"))
				println("Giờ làm việc: " + resultSet.getString("working_time"))
				return true
			} else {
				println("❌ Không tìm thấy dữ liệu nhân viên trong database!")
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
	def addRequired() {
		WebUI.click(findTestObject('Object Repository/employee/addEmployee/btn_manageEmployee'))

		WebUI.click(findTestObject('Object Repository/employee/addEmployee/btnAdd'))

		WebUI.click(findTestObject('Object Repository/employee/addEmployee/btnSubmit'))

		WebUI.verifyElementText(findTestObject('Object Repository/employee/addEmployee/requiredName'), 'Name is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/addEmployee/requiredGender'), 'Gender is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/addEmployee/requiredDOB'), 'Date of Birth is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/addEmployee/requiredCCCD'), 'CCCD is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/addEmployee/requiredPhone'), 'Phone Number is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/addEmployee/requiredPosition'), 'Position is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/employee/addEmployee/requiredWorkingTime'), 'Working Time is required.')

		WebUI.click(findTestObject('Object Repository/employee/addEmployee/btn_manageEmployee'))
	}


	@Keyword
	def addDuplicate() {
		WebUI.click(findTestObject('employee/addEmployee/btn_manageEmployee'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-employee", false)

		WebUI.click(findTestObject('Object Repository/employee/addEmployee/btnAdd'))

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
		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtName'), randomFullName)

		// Danh sách các giá trị có thể chọn
		List<String> options = ['male', 'female']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/employee/addEmployee/selectGender'),
				options[randomIndex], false)

		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtDOB'), '')

		// Click vào ô nhập ngày sinh
		WebUI.click(findTestObject('Object Repository/employee/addEmployee/txtDOB'))

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

		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtDOB'), randomDate)

		// Nhập trùng CCCD
		String numberCCCD = '123456789'

		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtCCCD'), numberCCCD)

		// Nhập trùng sđt
		String numberPhone = '0123456789'

		// Nhập số vào ô text
		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtPhoneNumber'), numberPhone)

		// Danh sách các giá trị có thể chọn
		List<String> optionsPos = ['guard', 'cleaner']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndexPos = new Random().nextInt(optionsPos.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/employee/addEmployee/selectPosition'),
				optionsPos[randomIndexPos], false)

		// Giờ làm việc
		Random randWorkTime = new Random()

		// Giờ bắt đầu từ 0h đến 23h
		int startHour = randWorkTime.nextInt(24)  // Random từ 0 đến 23

		// Giờ kết thúc từ (startHour + 4) đến (startHour + 10), nhưng tối đa là 23h
		int endHour = Math.min(startHour + randWorkTime.nextInt(7) + 4, 23)

		String timeWork = startHour + "h - " + endHour + "h"

		WebUI.setText(findTestObject('Object Repository/employee/addEmployee/txtWorkingTime'), timeWork)

		WebUI.click(findTestObject('Object Repository/employee/addEmployee/btnSubmit'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Error adding employee. Please try again.")) {
			WebUI.acceptAlert()
			println("Lỗi khi thêm nhân viên.")
		}

		WebUI.delay(3)
	}
}