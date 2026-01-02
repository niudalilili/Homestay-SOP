package com.tanyde.result;


import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    //编码信息：1为成功，0或其他为错误
    private Integer code;
    //错误信息
    private String msg;
    //数据
    private T data;

    public static <T> Result<T> success(){
        Result<T> result=new Result<>();
        result.setCode(1);
        return result;
    }

    public static <T> Result<T>success(T object){
        Result<T> result=new Result<>();
        result.setData(object);
        result.setCode(1);
        return result;
    }

    public static <T> Result<T>error(String msg){
        Result<T> result=new Result<>();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }
}
