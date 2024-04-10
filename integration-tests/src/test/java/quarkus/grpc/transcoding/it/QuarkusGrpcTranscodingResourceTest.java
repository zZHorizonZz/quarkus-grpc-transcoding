package quarkus.grpc.transcoding.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.quarkus.test.TestRequest;
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

    @Test
    public void testGrpcEndpoint() {
        given()
                .body(getJsonRequest())
                .when().post("/create")
                .then()
                .statusCode(200)
                .body("id", is("Hello test"));
    }

    private String getJsonRequest() {
        try {
            return JsonFormat.printer().omittingInsignificantWhitespace().print(TestRequest.newBuilder().setName("test").build());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
