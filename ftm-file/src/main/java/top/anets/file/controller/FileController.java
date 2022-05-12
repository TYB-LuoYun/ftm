package top.anets.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.ifeign.file.FileFeign;
import top.anets.system.service.FileService;
import top.anets.utils.base.Result;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileFeign fileFeign;

    @Autowired
    private FileService  fileService;
    @GetMapping("test")
    public Result testFeign(){
         return fileFeign.getFileTest();
    }

    @GetMapping("test1")
    public Result testFeign1(){
        return fileService.testGet();
    }



}









