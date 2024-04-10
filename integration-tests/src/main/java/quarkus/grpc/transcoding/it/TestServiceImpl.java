package quarkus.grpc.transcoding.it;

import com.cloudeko.sdk.v1.TestRequest;
import com.cloudeko.sdk.v1.TestResponse;
import com.cloudeko.sdk.v1.TestService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

@GrpcService
public class TestServiceImpl implements TestService {

    @Override
    public Uni<TestResponse> create(TestRequest request) {
        return Uni.createFrom().item(TestResponse.newBuilder().setId("Hello " + request.getName()).build());
    }
}
