package quarkus.grpc.transcoding.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.AnnotationsTransformerBuildItem;
import io.quarkus.arc.processor.AnnotationsTransformer;
import io.quarkus.arc.processor.BuiltinScope;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.grpc.deployment.GrpcDotNames;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.FieldInfo;

import java.util.*;

class QuarkusGrpcTranscodingProcessor {

    private static final String FEATURE = "quarkus-grpc-transcoding";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void processGeneratedBeans(CombinedIndexBuildItem index,
                               BuildProducer<AnnotationsTransformerBuildItem> transformers,
                               BuildProducer<AdditionalBeanBuildItem> delegatingBeans) {
        Set<String> generatedBeans = new HashSet<>();

        for (ClassInfo generatedBean : index.getIndex().getAllKnownSubclasses(TranscodingDotNames.MUTINY_TRANSCODING_SERVICE)) {
            FieldInfo delegateField = generatedBean.field("delegate");
            if (delegateField == null) {
                throw new IllegalStateException("A generated bean does not declare the delegate field: " + generatedBean);
            }
            DotName serviceInterface = delegateField.type().name();
            Collection<ClassInfo> serviceCandidates = index.getIndex().getAllKnownImplementors(serviceInterface);
            if (serviceCandidates.isEmpty()) {
                // No user-defined bean that implements the generated interface
                continue;
            }
            ClassInfo userDefinedBean = null;
            for (ClassInfo candidate : serviceCandidates) {
                // The bean must be annotated with @GrpcService
                if (candidate.declaredAnnotation(GrpcDotNames.GRPC_SERVICE) != null) {
                    userDefinedBean = candidate;
                    break;
                }
            }
            if (userDefinedBean == null) {
                continue;
            }

            DotName generatedBeanName = generatedBean.name();
            generatedBeans.add(generatedBeanName.toString());
        }

        if (!generatedBeans.isEmpty()) {
            transformers.produce(new AnnotationsTransformerBuildItem(new AnnotationsTransformer() {
                @Override
                public boolean appliesTo(AnnotationTarget.Kind kind) {
                    return kind == AnnotationTarget.Kind.CLASS;
                }

                @Override
                public void transform(TransformationContext context) {
                    if (generatedBeans.contains(context.getTarget().asClass().name().toString())) {
                        context.transform()
                                .add(BuiltinScope.APPLICATION.getName())
                                .done();
                    }
                }
            }));
        }
    }
}
