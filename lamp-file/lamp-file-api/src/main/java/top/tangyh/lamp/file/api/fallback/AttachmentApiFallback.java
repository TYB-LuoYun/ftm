//package top.tangyh.lamp.file.api.fallback;
//
//import top.tangyh.basic.base.R;
//import top.tangyh.lamp.file.api.AttachmentApi;
//import top.tangyh.lamp.file.dto.AttachmentDTO;
//import org.springframework.stereotype.Component;
//import org.springframework.static.multipart.MultipartFile;
//
///**
// * 熔断
// *
// * @author zuihou
// * @date 2019/07/25
// */
//@Component
//public class AttachmentApiFallback implements AttachmentApi {
//    @Override
//    public R<AttachmentDTO> upload(MultipartFile file, Boolean isSingle, Long id, String bizId, String bizType) {
//        return R.timeout();
//    }
//}
