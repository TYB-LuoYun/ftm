package top.anets.file.strategy.impl.fastdfs;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import top.anets.file.model.chunk.FileChunksMergeDTO;
import top.anets.file.model.entity.File;
import top.anets.file.properties.FileServerProperties;
import top.anets.file.strategy.impl.AbstractFileChunkStrategy;
import top.anets.file.utils.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author zuihou
 * @date 2020/11/22 5:18 下午
 */
@Slf4j
public class FastDfsFileChunkStrategyImpl extends AbstractFileChunkStrategy {
    protected final AppendFileStorageClient storageClient;

    public FastDfsFileChunkStrategyImpl(FileServerProperties fileProperties, AppendFileStorageClient storageClient) {
        super(  fileProperties);
        this.storageClient = storageClient;
    }


    @Override
    protected void copyFile(File file) {

    }

    @Override
    protected R<File> merge(List<java.io.File> files, String path, String fileName, FileChunksMergeDTO info) throws IOException {
        return null;
    }
}
