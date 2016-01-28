package com.qhvv.englishpuzzle.configuration;

/**
 * Created by Vo Quang Hoa on 20/09/2015.
 */
public interface IDataRequestHandler {
    void onSuccess();
    void onError(String errorMessgae);
}
