package top.anets.system.service.impl;

import org.springframework.web.bind.annotation.RestController;
import top.anets.system.service.FileService;
import top.anets.utils.base.Result;

@RestController
public class FileServiceImpl implements FileService {
    @Override
    public Result testGet() {
//        return Result.Success("ok");
        return null;
    }
}
