package Test;

import POM.BasketPageElements;
import POM.SelectedProductPage;
import POM.HomePageElements;
import POM.ProductListPage;
import Utilities.BaseDriver;
import Utilities.ReadExcelData;
import org.junit.*;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@OrderWith(Alphanumeric.class)
public class E2EProductBuyingTest extends BaseDriver {

    WebDriverWait wait;

    HomePageElements homePageElements;
    ReadExcelData readExcelData;
    ProductListPage productList;
    SelectedProductPage selectedProductPage;
    BasketPageElements basketPageElements;
    String numberOfExpectedProduct = "2 adet";
    String expectedSting ="SEPETINIZDE ÜRÜN BULUNMAMAKTADIR" ;
    private static final Logger logger = LogManager.getLogger(E2EProductBuyingTest.class);



    @BeforeClass
    public static void setUp() {
        initializeDriver();
    }


    @Test
    public void T1_closePopUpTest() throws InterruptedException {
        driver.get(URL);
        logger.info("Browser is launched");
        homePageElements = new HomePageElements(driver);

        Thread.sleep(2000);
        try {
            homePageElements.acceptAllButton.click();
            logger.info("PopUp is closed with button");

        } catch (NoSuchElementException e) {
        }

    }

    @Test
    public void T2_closeGenderPopUp() throws InterruptedException {
        homePageElements = new HomePageElements(driver);
        Thread.sleep(2000);
        homePageElements.closeGenderChoosePopUp.click();
    }
    @Test
    public void T3_closeUpdatePopUp() throws InterruptedException {

        homePageElements = new HomePageElements(driver);
        Thread.sleep(4000);
        homePageElements.noThanksButtonForUpdatePopUp.click();
    }
    @Test
    public void T4_enterAndDeleteSortOnSearchBar() throws InterruptedException {
        homePageElements = new HomePageElements(driver);
        readExcelData = new ReadExcelData(driver);
        Thread.sleep(4000);
        homePageElements.searchField.click();
        homePageElements.vazgecButton.isDisplayed();
        homePageElements.searchField2.sendKeys(readExcelData.getKeywordShort());
        logger.info("data is read with readExcelData");

        Thread.sleep(4000);
        homePageElements.silButtonOfSearchBar.click();


        homePageElements.searchField2.sendKeys(readExcelData.getKeywordShirt());
        logger.info("data is read with readExcelData");
        homePageElements.searchField2.sendKeys(Keys.ENTER);
    }

    @Test
    public void T5_chooseRandomShirt() throws InterruptedException {
        productList = new ProductListPage(driver);
        int sizeOfShirtList = productList.ListOfShirts.size();

        Random random = new Random();
        int randomIndex = random.nextInt(sizeOfShirtList);

        WebElement randomProduct = productList.ListOfShirts.get(randomIndex);
        Thread.sleep(4000);
        randomProduct.click();
        Thread.sleep(4000);
    }

    @Test
    public void T6_writingValuesOnTxt() throws InterruptedException {
        selectedProductPage = new SelectedProductPage(driver);

        Thread.sleep(7000);
        String productName = selectedProductPage.choosenProductDescription.getText();
        String productPrice = selectedProductPage.choosenProductPrice.getText();


        try (FileWriter writer = new FileWriter("product_info.txt")) {
            writer.write("Ürün Adı: " + productName + "\n");
            writer.write("Ürün Fiyatı: " + productPrice + "\n");
            logger.info("Txt file is created and added values");

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @Test
    public void T7_addToBasketTest() throws InterruptedException {
        selectedProductPage = new SelectedProductPage(driver);

        Thread.sleep(1500);
        if(ExpectedConditions.visibilityOf(selectedProductPage.sizeOfStocks).apply(driver) != null) {
            selectedProductPage.sizeOfStocks.click();
            logger.info("sizeOfStocks is checked and clicked it");
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(selectedProductPage.sizeOfCriticalStocks));
            selectedProductPage.sizeOfCriticalStocks.click();
            logger.info("sizeOfCriticalStocks is checked and clicked it");
        }

        Thread.sleep(4000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();",selectedProductPage.addToBasketButton);
        Assert.assertTrue(selectedProductPage.addToBasketButton.isDisplayed());
        selectedProductPage.addToBasketButton.click();
    }

    @Test
    public void T8_comparingPricesTest() throws InterruptedException {
        selectedProductPage = new SelectedProductPage(driver);
        basketPageElements = new BasketPageElements(driver);
        Thread.sleep(4000);
        selectedProductPage.basketIcon.click();
        Thread.sleep(3000);
        String basketPrice = basketPageElements.priceOfBasket.getText();

        String filePath = "product_info.txt";

            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        logger.info("TXT file is read and value is manipulated ");
             String manipulationOfTxTfile =stringBuilder.toString().replaceAll("[^\\d.]", "");

             String productPrice= manipulationOfTxTfile + " TL";

             String priceWithoutZero = basketPrice.replaceAll(",00|\\.00", "");

             Assert.assertEquals(priceWithoutZero, productPrice);

    }

    @Test
    public void T9_comparingPricesAfterAddingMoreTest() throws InterruptedException {
        basketPageElements = new BasketPageElements(driver);

        Select dropdown = new Select(basketPageElements.clickNumberOfProductDropdown);
        dropdown.selectByIndex(1);

        WebElement selectedOption = dropdown.getFirstSelectedOption();
        String numberOfProduct = selectedOption.getText();
        Assert.assertEquals(numberOfExpectedProduct, numberOfProduct);
        Thread.sleep(4000);
    }
    @Test
    public void TX_removeProductTest() throws InterruptedException {
        basketPageElements = new BasketPageElements(driver);

        basketPageElements.removeProductFromBasketButton.click();
        Thread.sleep(1500);
        String actualString = basketPageElements.sepetinizdeUrunBulunmamaktadirText.getText();
        Assert.assertEquals(actualString,expectedSting);
    }
}
