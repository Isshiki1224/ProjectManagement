package com.example.newsmanagement.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author ysq2008
 */
@Data
public class ProjectNews {
    public String id;
    public String title;
    public String content;
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    public Date publicDate;
    public String filePath;
    public String category;
    public String synopsis;
}
