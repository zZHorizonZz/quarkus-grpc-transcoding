package quarkus.grpc.transcoding.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuarkusGrpcTranscodingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/quarkus-grpc-transcoding")
                .then()
                .statusCode(200)
                .body(is("Hello quarkus-grpc-transcoding"));
    }
}
