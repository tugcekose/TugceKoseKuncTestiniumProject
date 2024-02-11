package POM;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BasketPageElements {
    public BasketPageElements (WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//span[@class='m-productPrice__salePrice']")
    public WebElement priceOfBasket;

    @FindBy(id= "quantitySelect0-key-0")
    public WebElement clickNumberOfProductDropdown;

    @FindBy(id= "removeCartItemBtn0-key-0")
    public WebElement removeProductFromBasketButton;

    @FindBy(xpath= "//strong[text()='Sepetinizde Ürün Bulunmamaktadır']")
    public WebElement sepetinizdeUrunBulunmamaktadirText;



}
