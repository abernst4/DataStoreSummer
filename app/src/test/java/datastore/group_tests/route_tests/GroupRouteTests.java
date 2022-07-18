package datastore.group_tests.route_tests;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GroupRouteTests {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/groups")
                .then()
                .statusCode(200)
                .body(is("{[]}"));
    }

    /*
    @Test
    public void testGreetingEndpoint() {
        String uuid = UUID.randomUUID().toString();
        given()
                .pathParam("name", uuid)
                .when().get("/hello/greeting/{name}")
                .then()
                .statusCode(200)
                .body(is("hello " + uuid));
    }
     */

}
