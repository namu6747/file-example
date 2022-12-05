package com.certpia.studentauthmanager.util;

import com.certpia.studentauthmanager.domain.AttachmentFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {

    //todo 현재 안먹힘
    @Value("${file.dir}")
    private String fileDir;

    //todo 파일 경로 지정
    public String getFullPath(String filename) {
        log.info("dir === {}",fileDir);
        if(StringUtils.hasText(fileDir)){
            return fileDir + filename;
        }
        return "C:/Users/82105/Desktop/upload/" + filename;
    }

    public AttachmentFile storeFile(MultipartFile multipartFile){
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("file has not been transferred.");
        }

        String originalName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        if(originalName.contains("..")){
            String errorMessage = String.format("File name containing ['..'] is not available. file name : [%s] ",originalName);
            throw new RuntimeException(errorMessage);
        }

        AttachmentFile attachmentFile = new AttachmentFile(multipartFile);
        String storeFileName = attachmentFile.getStoredName();
        try{
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
        } catch (Exception ex){
            log.error("FileStore.storeFile ",ex);
            throw new RuntimeException("error occur while save file : " + storeFileName);
        }
        return attachmentFile;
    }

    public static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public static String createUUIDFileName(String fileExt) {
        final String uuid = UUID.randomUUID().toString();
        return uuid + "." + fileExt;
    }

    public boolean deleteFile(String storedName){
        String fullPath = getFullPath(storedName);
        File file = new File(fullPath);
        return file.delete();
    }
}
