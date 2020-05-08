package com.example.newsmanagement.service;

import com.example.newsmanagement.bean.ProjectNews;
import com.example.newsmanagement.util.JsonData;

import java.util.List;

/**
 * @author ysq2008
 */
public interface NewsService {

    /**
     * 查询所有新闻
     * @return
     */
    List<ProjectNews> selectAllNews();

    /**
     * 标题搜索
     * @param searchContent
     * @param pageNum
     * @param pageSize
     * @return
     */
    JsonData selectByTitle(String searchContent, Integer pageNum, Integer pageSize,String category);

    /**
     * 新增
     * @param projectNews
     * @return
     */
    boolean add(ProjectNews projectNews);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * 修改
     * @param id
     * @return
     */
    ProjectNews selectById(String id);

}
