package com.agroneo.app.api;

import com.agroneo.app.utils.Json;

public interface ApiResponse {
    void apiResult(Json response);

    void apiError();

}
