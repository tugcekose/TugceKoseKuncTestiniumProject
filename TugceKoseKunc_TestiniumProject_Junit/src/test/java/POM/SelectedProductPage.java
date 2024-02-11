package POM;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SelectedProductPage {

    public SelectedProductPage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    @FindBy(css = ".o-productDetail__description")
    public WebElement choosenProductDescription;

    @FindBy(id = "priceNew")
    public WebElement choosenProductPrice;

    @FindBy(xpath="//span[@class='m-variation__item']")
    public WebElement sizeOfStocks;

    @FindBy(xpath="//span[@class='m-variation__item -criticalStock']")
    public WebElement sizeOfCriticalStocks;

    @FindBy(id = "addBasket")
    public  WebElement addToBasketButton;

    @FindBy(xpath = "//a[@title='Sepetim']")
    public WebElement basketIcon;

}
