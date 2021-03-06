package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/10/25.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class Province extends Entity implements ListEntity<City> {
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;

    @XStreamAlias("city")
    private List<City> list = new ArrayList<City>();

    @Override
    public List<City> getList() {
        return list;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setList(List<City> list) {
        this.list = list;
    }
}
