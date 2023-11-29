package io.github.FlyingASea.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Errors {
    UNKNOWN_ERROR(1, 500, "Server unknown error"),
    // ------------------ 1xx: Login, Authorized ------------------
    UNAUTHORIZED(100, 401, "Unauthorized"),
    FORBIDDEN(101, 403, "Forbidden"),
    WRONG_PASSWORD(102, 403, "Wrong user or password"),
    PERMISSION_DENIED(103, 403, "Permission denied"),
    INVALID_REFRESH_TOKEN(104, 403, "Invalid refresh token"),
    INVALID_ID(105, 400, "Invalid id"),
    INVALID_PASSWORD(106, 400, "Invalid password"),
    USER_ALREADY_EXIST(107, 409, "User already exist"),
    // ------------------ 2xx: Rate Limit -------------------------
    TOO_MANY_REQUESTS(201, 429, "Too many requests"),
    // ------------------ 3xx: Bad Request ------------------------
    BAD_REQUEST(300, 400, "Bad request"),
    UNSUPPORTED_MEDIA_TYPE(301, 415, "Unsupported media type"),
    UNSUPPORTED_METHOD(302, 405, "Unsupported method"),
    UNACCEPTABLE_MEDIA_TYPE(303, 406, "Unacceptable media type"),
    INVALID_DATA_FORMAT(304, 400, "Invalid data format"),
    INVALID_NAME(305, 400, "Invalid name"),
    UNKNOWN_FILE_TYPE(306, 400, "Unknown file type"),
    NOT_A_MULTIPART_REQUEST(307, 400, "Not a multipart request"),
    // ------------------ 4xx: Other --------------------------
    FILE_UPLOAD_FAILED(400, 400, "File upload failed"),
    AI_SYSTEM_NOT_ENABLED(401, 501, "AI system is not enabled"),
    NO_SUCH_CONTENT(402, 404, "No such content"),
    // ------------------ 5xx: Generate Error ------------------
    PIC_HAS_TWO_MORE_FACES(500, 400, "Picture has two or more faces"),
    PIC_HAS_NO_FACE(501, 400, "Picture has no face"),
    FACE_NOT_HORIZONTAL(502, 400, "Face is not horizontal"),
    FACE_NOT_FRONT(503, 400, "Face is not front"),
    AI_GENERATE_FAILED(504, 500, "AI generate failed"),
    PERSON_TOO_SMALL(505, 400, "Person is too small")
    ;

    private final int errCode;
    private final int httpCode;
    private final String errMsg;
}
