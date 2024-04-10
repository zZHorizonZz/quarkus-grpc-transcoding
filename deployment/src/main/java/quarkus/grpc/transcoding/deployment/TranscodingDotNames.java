package quarkus.grpc.transcoding.deployment;

import org.jboss.jandex.DotName;
import quarkus.grpc.transcoding.api.MutinyTranscodingService;

public class TranscodingDotNames {

    public static final DotName MUTINY_TRANSCODING_SERVICE = DotName.createSimple(MutinyTranscodingService.class.getName());
}
