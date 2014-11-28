package com.jsonde.api.function.echo;

import com.jsonde.api.function.FunctionResponse;

public class EchoFunctionResponse extends FunctionResponse<EchoFunctionRequest> {

    private String message;

    public EchoFunctionResponse(EchoFunctionRequest request) {
        super(request);
        message = request.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
