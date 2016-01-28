package com.qhvv.englishpuzzle.configuration;

/**
 * Created by Vo Quang Hoa on 19/09/2015.
 */
public interface IRequestHande<T> {
    void onError(String reason);
    void onSuccess(T data);
}
