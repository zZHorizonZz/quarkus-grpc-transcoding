package quarkus.grpc.transcoding.it;

import io.quarkus.grpc.GrpcService;
import io.quarkus.test.TestRequest;
import io.quarkus.test.TestResponse;
import io.quarkus.test.TestService;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

@GrpcService
public class TestServiceImpl implements TestService {

    private final static Logger log = Logger.getLogger(TestServiceImpl.class);

    @Override
    public Uni<TestResponse> create(TestRequest request) {
        log.info("Received request: " + request.getName());
        return Uni.createFrom().item(TestResponse.newBuilder().setId("Hello " + request.getName()).build());
    }
}
