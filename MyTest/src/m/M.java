package m;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class M {
	static int index = 1; 
	static int noOfStops = 2;
	static String busNo;
	static Map<String, ArrayList<String>> routes = new HashMap<String, ArrayList<String>>();
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
		System.setProperty("webdriver.firefox.bin","C:\\Users\\manishja\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
		WebDriver driver = new FirefoxDriver();
		String baseUrl = "http://www.go4mumbai.com";
		driver.manage().window().maximize();
		driver.get(baseUrl + "/List_of_Bus_Stops.php");
		Thread.sleep(3000);
		while(index < noOfStops){
			clickNextBus(driver);
			getStops(driver);
		}
		
		writeToFile();
		System.out.println("Done!!");
	}

	private static void writeToFile() throws FileNotFoundException, IOException {
		File file = new File("D:\\stops.txt");
		ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
		stream.writeObject(routes);
		stream.close();
	}

	private static void clickNextBus(WebDriver driver) throws InterruptedException {
		WebElement dropdown = driver.findElement(By.name("busno"));
		List<WebElement> options = dropdown.findElements(By.tagName("option"));
		noOfStops = options.size();
		System.out.println(options.get(index).getText());
		busNo = options.get(index).getText();
		options.get(index).click();
		Thread.sleep(3000);
	}

	private static void getStops(WebDriver driver) {
		WebElement el = driver.findElement(By.xpath("/html/body/div/table[3]/tbody/tr/td/table/tbody/tr[2]/td/table"));
		List<WebElement> els = el.findElements(By.tagName("tr"));
		els.remove(0);
		ArrayList<String> stops = new ArrayList<String>();
		for(WebElement e : els){
			System.out.println((e.findElements(By.tagName("td"))).get(1).getText());
			stops.add((e.findElements(By.tagName("td"))).get(1).getText());
		}
		routes.put(busNo, stops);
		index++;
	}
}
