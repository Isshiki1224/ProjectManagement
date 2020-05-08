package com.example.newsmanagement.util;

import lombok.Data;


/**
 * @author ysq2008
 */
@Data
public class JsonData {

    private String msg;
    private Object data;
    private int status;
    private Integer totalPage;
    private Integer pageNum;

}
