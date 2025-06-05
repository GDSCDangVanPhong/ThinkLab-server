package com.thinklab.platform.share.domain.model;
import lombok.Data;

@Data
public class Result<S, F> {

    private final S successData;
    private final F failedData;
    private final boolean isSuccess ;

    public static <S,F> Result<S,F> success(S successData){
        return new Result<>(
                successData,
                null,
                true
        );
    }


    public static <S,F> Result<S,F> failed(F failedData){
        return new Result<>(
                null,
                failedData,
                false
        );
    }
    public boolean isSuccess(){
        return isSuccess;
    }

}
