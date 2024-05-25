package com.github.deansquirrel.tools.http;

import okhttp3.Call;

import java.io.IOException;

public interface ICallback {
    void onFailure(Call call, IOException e);
    void onResponse(Call call, String response);
}
