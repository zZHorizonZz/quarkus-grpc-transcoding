package quarkus.grpc.transcoding.api;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

public abstract class MutinyTranscodingService {

    private static Logger log = Logger.getLogger(MutinyTranscodingService.class);

    protected <T extends Message> T transcodeRequest(String request, T.Builder builder) throws InvalidProtocolBufferException {
        JsonFormat.parser().ignoringUnknownFields().merge(request, builder);
        return (T) builder.build();
    }

    protected Response transcodeResponse(MessageOrBuilder response) {
        try {
            return Response.ok().entity(JsonFormat.printer().omittingInsignificantWhitespace().print(response)).build();
        } catch (InvalidProtocolBufferException e) {
            log.errorv(e, "Error transcoding response: {0}", e.getMessage());
            return Response.serverError().build();
        }
    }
}
