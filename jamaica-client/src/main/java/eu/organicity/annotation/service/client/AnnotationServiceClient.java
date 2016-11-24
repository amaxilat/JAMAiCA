package eu.organicity.annotation.service.client;

/**
 * Helper Client implementation for the OrganiCity Annotation Service.
 * This client implements the API described here:
 * https://annotations.organicity.eu/swagger-ui.html
 *
 * @author amaxilat@cti.gr
 */
public class AnnotationServiceClient extends AnnotationServiceAndroidClient {

    public AnnotationServiceClient(final String token) {
        super(token);
    }

}
