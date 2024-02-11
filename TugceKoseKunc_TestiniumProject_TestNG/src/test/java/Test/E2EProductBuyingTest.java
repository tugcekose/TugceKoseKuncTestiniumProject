package Test;

import POM.BasketPageElements;
import POM.SelectedProductPage;
import POM.HomePageElements;
import POM.ProductListPage;
import Utilities.BaseDriver;
import Utilities.ReadExcelData;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Random;

public class E2EProductBuyingTest extends BaseDriver {
    WebDriverWait wait;

    HomePageElements homePageElements;
    ReadExcelData readExcelData;
    ProductListPage productList;
    SelectedProductPage selectedProductPage;
    BasketPageElements basketPageElements;
    String numberOfExpectedProduct = "2 adet";
    String expectedSting ="SEPETİNİZDE ÜRÜN BULUNAMAMAKTADIR" ;




    @Test(priority = 1)
    public void ClosePopUpTest() throws InterruptedException {
        homePageElements = new HomePageElements(driver);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Thread.sleep(5000);
        try {
            homePageElements.acceptAllButton.click();
        } catch (NoSuchElementException e) {
        }
    }

    @Test(priority = 2)
    public void CloseGenderPopUp() throws InterruptedException {
        homePageElements = new HomePageElements(driver);
        homePageElements.closeGenderChoosePopUp.click();
    }


    @Test(priority = 3)
    public void CloseUpdatePopUp() throws InterruptedException {

        homePageElements = new HomePageElements(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Thread.sleep(7000);
        homePageElements.noThanksButtonForUpdatePopUp.click();
    }

    @Test(priority = 4)
    public void EnterAndDeleteSortOnSearchBar() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        homePageElements = new HomePageElements(driver);
        readExcelData = new ReadExcelData(driver);


        Thread.sleep(4000);
        homePageElements.searchField.click();
        homePageElements.vazgecButton.isDisplayed();
        homePageElements.searchField2.sendKeys(readExcelData.getKeywordShort());

        Thread.sleep(5000);
        homePageElements.silButtonOfSearchBar.click();

        Thread.sleep(5000);
        homePageElements.searchField2.sendKeys(readExcelData.getKeywordShirt());
        homePageElements.searchField2.sendKeys(Keys.ENTER);

    }

    @Test(priority = 5)
    public void ChooseRandomShirt() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        productList = new ProductListPage(driver);
        int sizeOfShirtList = productList.ListOfShirts.size();

        Random random = new Random();
        int randomIndex = random.nextInt(sizeOfShirtList);

        WebElement randomProduct = productList.ListOfShirts.get(randomIndex);
        Thread.sleep(4000);
        randomProduct.click();

    }

    @Test(priority = 6)
    public void WritingValuesOnTxt(ITestContext context) throws InterruptedException {

        selectedProductPage = new SelectedProductPage(driver);

        Thread.sleep(4000);
        String productName = selectedProductPage.choosenProductDescription.getText();
        String productPrice = selectedProductPage.choosenProductPrice.getText();
        context.setAttribute("product price", productPrice);

        try (FileWriter writer = new FileWriter("product_info.txt")) {
            writer.write("Ürün Adı: " + productName + "\n");
            writer.write("Ürün Fiyatı: " + productPrice + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 7)
    public void AddToBasketTest() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        selectedProductPage = new SelectedProductPage(driver);

        Thread.sleep(3000);
        if (selectedProductPage.sizeOfStocks.isDisplayed()) {

            selectedProductPage.sizeOfStocks.click();
        } else {
            selectedProductPage.sizeOfCriticalStocks.click();
        }

        Thread.sleep(4000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();",selectedProductPage.addToBasketButton);
        Assert.assertTrue(selectedProductPage.addToBasketButton.isDisplayed());
        selectedProductPage.addToBasketButton.click();

    }

    @Test(priority = 8)
    public void CompearingPricesTest(ITestContext context) throws InterruptedException {

        selectedProductPage = new SelectedProductPage(driver);
        basketPageElements = new BasketPageElements(driver);

        Thread.sleep(3000);
        selectedProductPage.basketIcon.click();

        String productPrice = (String) context.getAttribute("product price");
        String basketPrice = basketPageElements.priceOfBasket.getText();

        String priceWithoutZero = basketPrice.replaceAll(",00|\\.00", "");

        System.out.println(productPrice);
        Assert.assertEquals(priceWithoutZero, productPrice);

    }


    @Test(priority = 9)
    public void CompearingPricesTest() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        basketPageElements = new BasketPageElements(driver);

        Select dropdown = new Select(basketPageElements.clickNumberOfProductDropdown);
        dropdown.selectByIndex(1);

        String numberOfProduct = basketPageElements.productNumberCheckAfterAddedOneMore.getText();
        Assert.assertEquals(numberOfExpectedProduct, numberOfProduct);


    }

    @Test(priority = 10)
    public void RemoveProductTest() throws InterruptedException {

        basketPageElements = new BasketPageElements(driver);

        basketPageElements.removeProductFromBasketButton.click();
        String actualString = basketPageElements.sepetinizdeUrunBulunmamaktadırText.getText();
        Assert.assertEquals(actualString,expectedSting);
    }
}







