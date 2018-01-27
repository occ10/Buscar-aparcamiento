package server;

/**
 * Created by walid on 27/01/2018.
 */

public class RestResponse {

    private final int code;

    private final String content;


    public RestResponse(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getHttpResponseCode(){
        return code;
    }

    public String getHttpContent() {
        return content;
    }
}
