package com.guoer03.result;

import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor//lombok中全构造方法的注解。
public class Result<T> {
    private int code;
    private String msg;
    private T data;//因为data无法知道是什么类型。Result负责封装所有的发挥数据，则使用泛型。

    public Result(T data) {
        this.code=0;
        this.data = data;
        this.msg="success";
    }
    public Result(CodeMsg cm) {
        if (cm==null){
            return;
        }
        this.code=cm.getCode();
        this.msg=cm.getMsg();
    }
    /*
    * 成功时候的调用
    * */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    /*
    * 失败时的调用：失败时不会返回数据，只需要返回一个code和msg错误信息，可以将这俩构造成一个对象，因为code和错误信息往往是成对出现的*/
    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }
}
