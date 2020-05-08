package com.example.newsmanagement.controller;

import com.example.newsmanagement.bean.ProjectNews;
import com.example.newsmanagement.service.NewsService;
import com.example.newsmanagement.util.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @author ldk
 */
@Controller
public class NewsController {
    private JsonData data;

    @Resource
    private NewsService newsService;

    @RequestMapping("/selectAll")
    @ResponseBody
    @CrossOrigin
    public JsonData getNewsList(){
        data = new JsonData();
        List<ProjectNews> projectNews = newsService.selectAllNews();
        if (projectNews == null) {
            data.setMsg("操作失败");
            data.setStatus(-1);
        }
        return data;
    }

    @RequestMapping("/news")
    @ResponseBody
    @CrossOrigin
    public JsonData getNewsByTitle(String searchContent, Integer pageNum, Integer pageSize,String category){
        data = new JsonData();
        data = newsService.selectByTitle(searchContent, pageNum, pageSize,category);
        if (data.getStatus() != 0) {
            return data;
        }
        data.setMsg("操作成功");
        data.setPageNum(pageNum);
        data.setStatus(0);
        return data;
    }

    @RequestMapping("/upload")
    @ResponseBody
    @CrossOrigin
    public JsonData upload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        data = new JsonData();

        if (file.isEmpty()) {
            data.setStatus(-1);
            data.setMsg("文件上传不能为空");
            return data;
        }
        System.out.println("file=" + file.getOriginalFilename());
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        InputStream is = null;
        //把图片以毫秒值命名
        String filename = System.currentTimeMillis() + suffix;
        String newPath = "";
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File home = fsv.getHomeDirectory();
        String savePath = home.getPath() + "/picture/" + filename;
        System.out.println(savePath);
        String visitPath = filename;
//        //获取项目classes/static的地址
//        String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
//        //获取文件名
//        String fileName = file.getOriginalFilename();
//
//        // 图片存储目录及图片名称
//        String urlPath = "images" + File.separator + filename;
//        //图片保存路径
//        String savePath = staticPath + File.separator + urlPath;
//        System.out.println("图片保存地址："+savePath);
//        // 访问路径=静态资源路径+文件目录路径
//        String visitPath ="static/" + urlPath;
//        System.out.println("图片访问uri："+visitPath);

        try {
            is = file.getInputStream();

            newPath = savePath;
            File file1 = new File(newPath);

            if (!file1.getParentFile().exists()) {
                boolean b = file1.getParentFile().mkdir();
            }
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //String relaPath = "/pic/" + filename;
        data.setData(visitPath);
        data.setStatus(0);
        data.setMsg("上传成功");
        return data;
    }

    @RequestMapping("/add")
    @ResponseBody
    @CrossOrigin()
    public JsonData add(@RequestBody ProjectNews projectNews) {
        data = new JsonData();
        System.out.println(projectNews);
        boolean flag;
        flag = newsService.add(projectNews);

        if (flag) {
            data.setStatus(0);
            data.setMsg("新增成功");
        } else {
            data.setStatus(-2);
            data.setMsg("新增失败");
        }
        System.out.println(data);
        return data;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @CrossOrigin
    public JsonData delete(String id) {

        data = new JsonData();

        boolean flag = newsService.delete(id);

        if (flag) {
            data.setStatus(0);
            data.setMsg("删除成功");
        } else {
            data.setStatus(-1);
            data.setMsg("删除失败");
        }
        return data;
    }

    @RequestMapping("/getById")
    @ResponseBody
    @CrossOrigin
    public JsonData getById(String id) {
        System.out.println("id=" + id);
        data = new JsonData();
        ProjectNews projectNews = newsService.selectById(id);
        if (projectNews == null) {
            data.setStatus(-1);
            data.setMsg("未找到对应的法律法规");
            return data;
        }
        data.setMsg("操作成功");
        data.setStatus(0);
        data.setData(projectNews);
        return data;
    }



}
