    package com.thinklab.platform.user.domain.model;

    import com.thinklab.platform.authen.domain.model.AccountProvider;
    import com.thinklab.platform.share.domain.exception.InternalErrorException;
    import com.thinklab.platform.share.domain.exception.ValidateException;
    import com.thinklab.platform.share.domain.model.Result;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.SneakyThrows;

    import java.time.LocalDateTime;
    import java.util.UUID;

    @AllArgsConstructor
    @Data
    public class UserRequest  {
        private UUID id;
        private  String email;
        private String username;
        private String password;
        private LocalDateTime createdAt = LocalDateTime.now();
        private AccountProvider provider;

        @SneakyThrows
        public Result<Boolean, ValidateException> validateResult(){
            try{
                if(
                        email!=null &&
                        password!= null && password.length()>=8 &&
                        provider!=null
                ){
                    return Result.success(true);
                }
                else{
                    return Result.failed(new ValidateException("Invalid Inputs, Please Check Your Information Again!"));
                }

            }
            catch (Exception e){
                throw new InternalErrorException(e.getMessage());
            }
        }

    }
