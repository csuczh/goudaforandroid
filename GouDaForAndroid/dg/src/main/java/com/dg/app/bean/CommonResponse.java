package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 赞某动态/删除动态评论/删除动态  响应Bean
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class CommonResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //状态码

    @XStreamAlias("msg")
    private String msg;//返回状态信息

    public boolean OK() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AddResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}


