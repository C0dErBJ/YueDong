package com.yuedong.app.Model;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by C0dEr on 16/1/17.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public String Id;
    public String UserName;
    public String Gender;
    public String NickName;
    public Date LoginTime;
    public int Age;
    public String Hobby;
    public String RecentLocation;

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {

    }

}
