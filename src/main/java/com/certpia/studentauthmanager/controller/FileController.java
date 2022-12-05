package com.certpia.studentauthmanager.controller;

import com.certpia.studentauthmanager.dto.AttachmentFileDto;
import com.certpia.studentauthmanager.request.AttachmentFileRequestParam;
import com.certpia.studentauthmanager.service.FileService;
import com.certpia.studentauthmanager.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileStore fileStore;

    @GetMapping("/file")
    public ModelAndView filePage(){
        return new ModelAndView("/file-system/file");
    }

    @GetMapping("/files")
    public ResponseEntity<List<AttachmentFileDto>> fileList(){
        return ResponseEntity.ok(fileService.fileList());
    }

    @PostMapping("/files")
    public ResponseEntity<List<AttachmentFileDto>> fileUpload(AttachmentFileRequestParam param){
        return ResponseEntity.ok(fileService.fileUpload(param));
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> fileDownload(@PathVariable String id) throws Exception{
        AttachmentFileDto dto = fileService.fileDownload(id);
        String originalName = dto.getOriginalName();
        String storedName = dto.getStoredName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storedName));

        String encodedUploadFileName = UriUtils.encode(originalName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<String> fileDelete(@PathVariable String id){
        String deleteAt = fileService.fileDelete(id);
        return ResponseEntity.ok(deleteAt);
    }


}
