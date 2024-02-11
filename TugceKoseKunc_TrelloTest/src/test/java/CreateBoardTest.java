import Pojo.BoardPojo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateBoardTest {
    private RequestSpecification reqSpe;
    private BoardPojo board;
    protected static String boardID;
    protected static String listID;
    protected static String CardID;
    protected static String Card2ID;


    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://api.trello.com/";

        reqSpe = given()
                .log().uri()
                .queryParam("key", "EnterYourKey")
                .queryParam("token", "EnterYourToken")
                .contentType(ContentType.JSON);


        board = new BoardPojo();
        board.setName("tugceBoard");
        board.setListName("firstList");
        board.setCardName("updatedCardName");

    }

    @Test
    public void createBoard() {

        board.setBoardId(given().spec(reqSpe)
                .queryParam("name", board.getName())
                .when()
                .post("1/boards")
                .then()
                .log().body()
                .statusCode(200)
                .extract().jsonPath().getString("id"));

        boardID = board.getBoardId();


    }

    @Test(dependsOnMethods = "createBoard")
    public void createList() {
        board.setListID(given().spec(reqSpe)
                .queryParam("name", board.getListName())
                .pathParam("id", boardID)
                .when().post("1/boards/{id}/lists")
                .then()
                .log().body()
                .statusCode(200)
                .extract().jsonPath().getString("id"));
        listID = board.getListID();
        System.out.println(listID);

    }

    @Test(dependsOnMethods = "createList")
    public void createCard1() {
        board.setCardID(given().spec(reqSpe)
                .header("Accept", "application/json")
                .queryParam("idList", listID)
                .when()
                .post("1/cards")
                .then()
                .log().body()
                .statusCode(200)
                .extract().jsonPath().getString("id"));
        CardID = board.getCardID();
    }


    @Test(dependsOnMethods = "createCard1")
    public void createCard2() {
        board.setCard2ID(given().spec(reqSpe)
                .header("Accept", "application/json")
                .queryParam("idList", listID)
                .when()
                .post("1/cards")
                .then()
                .log().body()
                .statusCode(200)
                .extract().jsonPath().getString("id"));
        Card2ID = board.getCard2ID();
        System.out.println(Card2ID);
    }

    @Test(dependsOnMethods = "createCard2")
    public void getCard1() {

        given().spec(reqSpe)
                .header("Accept", "application/json")
                .pathParam("id", CardID)
                .when()
                .get("1/cards/{id}")
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(CardID));
    }

    @Test(dependsOnMethods = "getCard1")
    public void getCard2() {
        given().spec(reqSpe)
                .header("Accept", "application/json")
                .pathParam("id", Card2ID)
                .when()
                .get("1/cards/{id}")
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(Card2ID));
    }

    public static String getRandomCard(String CardID, String Card2ID) {

        Random rn = new Random();
        int randomNumber = rn.nextInt(2);

        if (randomNumber == 0) {
            return CardID;
        } else {
            return Card2ID;
        }
    }

    @Test(dependsOnMethods = "getCard2")
    public void updateRandomCard() {
        given().spec(reqSpe)
                .header("Accept", "application/json'")
                .pathParam("id", getRandomCard(CardID, Card2ID))
                .body(board)
                .when()
                .put("1/cards/{id}")
                .then()
                .log().body()
                .statusCode(200);
    }


    @Test(dependsOnMethods = "updateRandomCard")
    public void deleteCard() {
        given().spec(reqSpe)
                .pathParam("id", CardID)
                .when()
                .delete("1/cards/{id}")
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "updateRandomCard")
    public void deleteCard2() {
        given().spec(reqSpe)
                .pathParam("id", Card2ID)
                .when()
                .delete("1/cards/{id}")
                .then()
                .statusCode(200);
    }
}


