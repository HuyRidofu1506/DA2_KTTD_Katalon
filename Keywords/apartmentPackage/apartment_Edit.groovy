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

import org.openqa.selenium.Keys

public class apartment_Edit {
	@Keyword
	def editSuccess() {
		WebUI.click(findTestObject('Object Repository/apartment/add/btn_manageAparment'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-apartment", false)

		WebUI.click(findTestObject('Object Repository/apartment/edit/btnEdit'))

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

def residentInfo = selectRandomResident()
if (residentInfo[0] == null) {
    println("❌ Không có resident nào trong database")
    return
}

// Lưu cả ID và tên
String selectedResidentId = residentInfo[0]
String selectedName = residentInfo[1]

// Select trong dropdown bằng tên
TestObject dropdown = findTestObject('Object Repository/apartment/add/selectResident')
WebUI.selectOptionByLabel(dropdown, selectedName, false)

		WebUI.delay(2)
		WebUI.click(findTestObject('Object Repository/apartment/add/btnSubmit'))

		WebUI.waitForAlert(5)
		String alertText = WebUI.getAlertText()

		if (alertText.contains("Apartment updated successfully!")) {
			WebUI.acceptAlert()
			println("Đã sửa thông tin căn hộ thành công")
			verifyApartmentAfterEdit(randomNumOfApart, selectedName, options[randomIndex])
		}

		WebUI.delay(3)
	}
	
	@Keyword
	def selectRandomResident() {
	    String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
	    String username = "root"
	    String password = "123456"
	    
	    Connection connection = null
	    try {
	        connection = DriverManager.getConnection(url, username, password)
	        String query = "SELECT id, name FROM resident"
	        PreparedStatement statement = connection.prepareStatement(query, 
	                               ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                               ResultSet.CONCUR_READ_ONLY)
	        
	        ResultSet resultSet = statement.executeQuery()
	        
	        if (resultSet.last()) {
	            int count = resultSet.getRow()
	            resultSet.beforeFirst()
	            
	            if (count > 0) {
	                resultSet.absolute(new Random().nextInt(count) + 1)
	                String residentId = resultSet.getString("id")
	                String residentName = resultSet.getString("name")
	                println("👉 Đã chọn resident: $residentName (ID: $residentId)")
	                return [residentId, residentName]
	            }
	        }
	    } catch (SQLException e) {
	        println("Lỗi khi lấy resident: " + e.getMessage())
	    } finally {
	        connection?.close()
	    }
	    return [null, null]
	}
	
	@Keyword
	def verifyApartmentAfterEdit(String apartmentNumber, String residentName, String expectedStatus) {
		String url = "jdbc:mysql://localhost:3307/sb_manage_apartment"
		String username = "root"
		String password = "123456"
	
		Connection connection = null
		try {
			// Kết nối database
			connection = DriverManager.getConnection(url, username, password)
			
			// Truy vấn kiểm tra thông tin căn hộ
			String query = """
            SELECT a.*, 
                   r.name as resident_name,
                   a.resident_id as db_resident_id
            FROM apartment a
            LEFT JOIN resident r ON a.resident_id = r.id
            WHERE a.number_of_apartment = ?
        """
			
			PreparedStatement statement = connection.prepareStatement(query)
			statement.setString(1, apartmentNumber)
			
			ResultSet resultSet = statement.executeQuery()
			
			if (resultSet.next()) {
	            println("\n=== THÔNG TIN DATABASE ===")
	            println("Số phòng: " + resultSet.getString("number_of_apartment"))
	            println("Trạng thái: " + resultSet.getString("status"))
	            println("Tên cư dân: " + (resultSet.getString("resident_name") ?: "null"))          
			}
		} finally {
			if (connection != null) {
				connection.close()
			}
		}
	}
	
	@Keyword
	def editRequired() {
		WebUI.click(findTestObject('Object Repository/apartment/add/btn_manageAparment'))

		WebUI.click(findTestObject('Object Repository/apartment/edit/btnEdit'))

		// Để Number trống
		WebUI.sendKeys(findTestObject('Object Repository/apartment/edit/txtNumberOfApart'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))

		WebUI.click(findTestObject('Object Repository/apartment/edit/btnSubmit'))

		WebUI.verifyElementText(findTestObject('Object Repository/apartment/edit/requiredNuberOfApart'), 'Number of apartment is required.')
	}
	
	@Keyword
	def editDuplicate() {
		WebUI.click(findTestObject('Object Repository/apartment/add/btn_manageAparment'))

		String currentURL = WebUI.getUrl()
		println("URL trang hiện tại: " + currentURL)

		// Kiểm tra URL có đúng không
		WebUI.verifyMatch(currentURL, "http://localhost:4200/manage-apartment", false)

		WebUI.click(findTestObject('Object Repository/apartment/edit/btnEdit'))

		WebUI.setText(findTestObject('Object Repository/apartment/add/txtNumberOfApart'), "101")

		// Danh sách các giá trị có thể chọn
		List<String> options = ['available', 'inAvailable']

		// Tạo số ngẫu nhiên (0 hoặc 1)
		int randomIndex = new Random().nextInt(options.size())

		// Chọn giá trị tương ứng
		WebUI.selectOptionByValue(findTestObject('Object Repository/apartment/add/selectStatus'),
				options[randomIndex], false)

		// Danh sách các tên có sẵn
		List<String> nameList = ["Nguyễn Văn A"]

		// Tạo đối tượng Random
		Random randRes = new Random()

		// Chọn ngẫu nhiên một tên từ danh sách
		String selectedName = nameList[randRes.nextInt(nameList.size())]

		TestObject dropdown = findTestObject('Object Repository/apartment/add/selectResident')
		WebUI.selectOptionByLabel(dropdown, selectedName, false)

		WebUI.click(findTestObject('Object Repository/apartment/add/btnSubmit'))

		WebUI.waitForAlert(5)

		String alertText = WebUI.getAlertText()

		if (alertText.contains("Error updating apartments. Please try again.")) {
			WebUI.acceptAlert()
			println("Lỗi khi sửa thông tin căn hộ.")
		}

		WebUI.delay(3)
	}
}
