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
import java.util.Random as Random
import java.sql.*

public class apartment_Add {
	@Keyword
	def addSuccess() {
		WebUI.click(findTestObject('Object Repository/apartment/add/btn_manageAparment'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-apartment", false)

		WebUI.click(findTestObject('Object Repository/apartment/add/btnAdd'))

		// Random số phòng
		Random rand = new Random()
		int firstNumber = rand.nextInt(9) + 1
		int secondNumber = rand.nextInt(4) + 1
		int thirdNumber = rand.nextInt(9) + 1

		// Ghép 3 số lại thành một chuỗi
		String randomNumOfApart = "${firstNumber}${secondNumber}${thirdNumber}"

		WebUI.setText(findTestObject('Object Repository/apartment/add/txtNumberOfApart'), randomNumOfApart)

		// Danh sách các giá trị có thể chọn
		List<String> options = ['available', 'inAvailable']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/apartment/add/selectStatus'),
				options[randomIndex], false)

		// Danh sách các tên có sẵn
		List<String> nameList = [
			"Nguyễn Văn A",
			"Cao Thị T",
			"Cao Văn C"
		]

		// Tạo đối tượng Random
		Random randRes = new Random()

		// Chọn ngẫu nhiên một tên từ danh sách
		String selectedName = nameList[randRes.nextInt(nameList.size())]

		TestObject dropdown = findTestObject('Object Repository/apartment/add/selectResident')
		WebUI.selectOptionByLabel(dropdown, selectedName, false)

		WebUI.click(findTestObject('Object Repository/apartment/add/btnSubmit'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Apartment added successfully!")) {
			WebUI.acceptAlert()
			println("Đã thêm căn hộ thành công")
			verifyApartmentInDatabase(randomNumOfApart, selectedName)
		}

		WebUI.delay(3)
	}

	@Keyword
	def verifyApartmentInDatabase(String apartmentNumber, String residentName) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"

		Connection connection = null
		try {
			// Kết nối database
			connection = DriverManager.getConnection(url, username, password)

			// Truy vấn kiểm tra căn hộ vừa thêm
			String query = """
            SELECT a.*, r.name as resident_name 
            FROM apartment a
            JOIN resident r ON a.resident_id = r.id
            WHERE a.number_of_apartment = ? 
            AND r.name = ?
        """

			PreparedStatement statement = connection.prepareStatement(query)
			statement.setString(1, apartmentNumber)
			statement.setString(2, residentName)

			ResultSet resultSet = statement.executeQuery()

			// Kiểm tra dữ liệu
			if (resultSet.next()) {
				println("✅ Dữ liệu căn hộ đã được thêm vào database:")
				println("Số phòng: " + resultSet.getString("number_of_apartment"))
				println("Trạng thái: " + resultSet.getString("status"))
				println("Tên cư dân: " + resultSet.getString("resident_name"))
				return true
			} else {
				println("❌ Không tìm thấy dữ liệu căn hộ trong database!")
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
		WebUI.click(findTestObject('Object Repository/apartment/add/btn_manageAparment'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-apartment", false)

		WebUI.click(findTestObject('Object Repository/apartment/add/btnAdd'))

		WebUI.click(findTestObject('Object Repository/apartment/add/btnSubmit'))

		WebUI.verifyElementText(findTestObject('Object Repository/apartment/add/requiredNumOfApart'), 'Number of apartment is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/apartment/add/requiredStatus'), 'Status is required.')

		WebUI.verifyElementText(findTestObject('Object Repository/apartment/add/requiredResident'), 'Resident name is required.')
	}

	@Keyword
	def addDuplicate() {
		WebUI.click(findTestObject('Object Repository/apartment/add/btn_manageAparment'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-apartment", false)

		WebUI.click(findTestObject('Object Repository/apartment/add/btnAdd'))

		WebUI.setText(findTestObject('Object Repository/apartment/add/txtNumberOfApart'), "436")

		// Danh sách các giá trị có thể chọn
		List<String> options = ['available', 'inAvailable']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/apartment/add/selectStatus'),
				options[randomIndex], false)

		// Danh sách các tên có sẵn
		List<String> nameList = ["Cao Văn C"]

		// Tạo đối tượng Random
		Random randRes = new Random()

		// Chọn ngẫu nhiên một tên từ danh sách
		String selectedName = nameList[randRes.nextInt(nameList.size())]

		TestObject dropdown = findTestObject('Object Repository/apartment/add/selectResident')
		WebUI.selectOptionByLabel(dropdown, selectedName, false)

		WebUI.click(findTestObject('Object Repository/apartment/add/btnSubmit'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Error adding apartment. Please try again.")) {
			WebUI.acceptAlert()
			println("Lỗi khi thêm căn hộ.")
		}

		WebUI.delay(3)
	}
}
