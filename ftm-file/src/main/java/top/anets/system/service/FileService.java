package top.anets.system.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.anets.utils.base.Result;

@RequestMapping("fileFeign")
public interface FileService {
    @GetMapping("test")
    Result testGet();
}
