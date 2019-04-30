package main.java.projects.mock.pages;

import org.openqa.selenium.By;

import main.java.base.pom.ByFieldsReplacer;

/**
 * Input data for testing StringFieldsReplacerInterface. 
 * 
 * @author dan.rusu
 *
 */
public class ByFieldsReplacerTestData implements ByFieldsReplacer {
    public By cssSelector = By.cssSelector("#id_@text");
	private By xpathSelector = By.xpath("//*[@id=id_@text]");
	public By idSelector = By.id("id_@text");
	public By classNameSelector = By.className("class_@text");
	
	public By getXpathSelector() {
		return xpathSelector;
	}



	public Object a; 
	
	public By field = By.cssSelector("#counter@tex");

	
	
	public ByFieldsReplacerTestData(String toReplace, String replacement){
		initByFields(this, toReplace, replacement);
	}
}
