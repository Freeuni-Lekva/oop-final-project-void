package response;


public class ResponseService {

    private final ResponseRepository responseRepository;

    public ResponseService(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public Response createResponse(Response response) {
        validateResponse(response);
        responseRepository.create(response);
        return response;
    }

    private void validateResponse(Response response) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }

        if (response.getAttemptId() <= 0) {
            throw new IllegalArgumentException("Invalid attempt ID");
        }

        if (response.getQuestionId() <= 0) {
            throw new IllegalArgumentException("Invalid question ID");
        }



    }
}
