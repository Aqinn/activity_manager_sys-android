package com.aqinn.actmanagersysandroid.data;

public class ApiResult<T> {

   public boolean success;
   public String errMsg;
   public T data;

   @Override
   public String toString() {
      return "ApiResult{" +
              "success=" + success +
              ", errMsg='" + errMsg + '\'' +
              ", data=" + data +
              '}';
   }

}
