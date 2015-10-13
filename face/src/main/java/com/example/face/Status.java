package com.example.face;

/**
 * Created by mac on 15/10/13.
 */
public class  Status {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status = NOMAL;
    public static final int NOMAL = 0;//正常状态
    public static final int HAPPY = 1;//喜悦
    public static final int SAD1 = 2;//悲伤1：
    public static final int SAD2 = 3;//悲伤2：


}
