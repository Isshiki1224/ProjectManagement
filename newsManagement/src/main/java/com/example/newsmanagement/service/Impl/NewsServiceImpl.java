package com.example.newsmanagement.service.Impl;

import com.example.newsmanagement.bean.ProjectNews;
import com.example.newsmanagement.service.NewsService;
import com.example.newsmanagement.util.JsonData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ysq2008
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Resource
    JestClient jestClient;


    @Override
    public JsonData selectByTitle(String searchContent, Integer pageNum, Integer pageSize,String category) {
        System.out.println("searchContent=" + searchContent);
        JsonData data = new JsonData();
        int fromInt = (pageNum - 1) * pageSize;
        List<ProjectNews> legals = new ArrayList<>();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if("".equals(category)){
            if (!("".equalsIgnoreCase(searchContent))) {
                MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title", searchContent);
                boolQueryBuilder.must(matchQueryBuilder);
            }
        }else{
            System.out.println("category=" + category);
            if (!("".equalsIgnoreCase(searchContent))) {
                MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title", searchContent);
                MatchQueryBuilder matchQueryBuilder1 = new MatchQueryBuilder("category", category);
                boolQueryBuilder.must(matchQueryBuilder).must(matchQueryBuilder1);
            }else{
                MatchQueryBuilder matchQueryBuilder1 = new MatchQueryBuilder("category", category);
                boolQueryBuilder.must(matchQueryBuilder1);
            }
        }

        builder.query(boolQueryBuilder);
        builder.size(1000);
        String dslStr1 = builder.toString();
        System.out.println(dslStr1);
        Search search1 = new Search.Builder(dslStr1).addIndex("news").addType("_doc").build();
        SearchResult searchResult1 = null;
        try {
            searchResult1 = jestClient.execute(search1);
        } catch (IOException e) {
            e.printStackTrace();
            data.setStatus(-1);
            data.setMsg("标题查询失败");
            return data;
        }
        List<SearchResult.Hit<ProjectNews, Void>> hits1 = searchResult1.getHits(ProjectNews.class);

        builder.from(fromInt);
        builder.size(pageSize);
        builder.sort("publicDate", SortOrder.DESC);
        builder.query(boolQueryBuilder);

        String dslStr = builder.toString();
        System.out.println(dslStr);
        Search search = new Search.Builder(dslStr).addIndex("news").addType("_doc").build();
        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
            data.setStatus(-2);
            data.setMsg("标题查询失败");
            return data;
        }
        List<SearchResult.Hit<ProjectNews, Void>> hits = searchResult.getHits(ProjectNews.class);

        for (SearchResult.Hit<ProjectNews, Void> hit : hits) {
            ProjectNews source = hit.source;
            source.setId(hit.id);
            legals.add(source);
        }
        System.out.println(hits1.size());
        data.setData(legals);
        data.setTotalPage(hits1.size());
        return data;
    }

    @Override
    public boolean delete(String id) {
        System.out.println(id);
        Delete delete = new Delete.Builder(id).index("news").type("_doc").build();
        try {
            jestClient.execute(delete);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public ProjectNews selectById(String id) {
        Get get = new Get.Builder("news", id).type("_doc").build();

        JestResult result = null;
        try {
            result = jestClient.execute(get);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        JsonObject jsonObject = result.getJsonObject().get("_source").getAsJsonObject();

        Gson gson = new Gson();
        ProjectNews projectNews = new ProjectNews();
        projectNews = gson.fromJson(jsonObject, projectNews.getClass());
        projectNews.setId(id);
        System.out.println(projectNews);

        return projectNews;
    }

    @Override
    public boolean add(ProjectNews projectNews) {
        Index index;
        System.out.println(projectNews.getId());
        if (projectNews.getId() != null) {
            System.out.println("bbb");
//            String[] ids = projectNews.getId().split(",");
            String id = projectNews.getId();
            index = new Index.Builder(projectNews).index("news").type("_doc").id(id).build();
            try {
                jestClient.execute(index);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            index = new Index.Builder(projectNews).index("news").type("_doc").build();
            System.out.println("aaa");
            try {
                jestClient.execute(index);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public List<ProjectNews> selectAllNews() {
        List<ProjectNews> projectNews = new ArrayList<>();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(1000);
        String dsl = builder.toString();
        Search search = new Search.Builder(dsl).addIndex("news").addType("_doc").build();
        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        List<SearchResult.Hit<ProjectNews, Void>> hits = searchResult.getHits(ProjectNews.class);
        for (SearchResult.Hit<ProjectNews, Void> hit : hits) {
            ProjectNews source = hit.source;
            projectNews.add(source);
        }
        return projectNews;
    }
}
