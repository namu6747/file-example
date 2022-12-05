package com.certpia.studentauthmanager.service;

import com.certpia.studentauthmanager.domain.AttachmentFile;
import com.certpia.studentauthmanager.dto.AttachmentFileDto;
import com.certpia.studentauthmanager.repository.FileRepository;
import com.certpia.studentauthmanager.request.AttachmentFileRequestParam;
import com.certpia.studentauthmanager.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStore fileStore;
    private final FileRepository fileRepository;

    /**
     * 파일 업로드
     */
    public List<AttachmentFileDto> fileUpload(AttachmentFileRequestParam requestParam){
        List<MultipartFile> attachments = requestParam.getAttachments();
        List<AttachmentFileDto> attachmentFiles = storeFiles(attachments);
        return attachmentFiles;
    }

    /**
     * 삭제되지 않은 파일만 가져오기
     */
    public List<AttachmentFileDto> fileList(){
        return fileRepository.findAllNotDeleted()
                .stream()
                .map(AttachmentFileDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 파일 다운로드
     */
    public AttachmentFileDto fileDownload(String uuid) {
        AttachmentFile attachmentFile = fileRepository.findById(uuid).orElseThrow(() ->new RuntimeException("다운로드할 파일을 찾지 못하였습니다."));
        return new AttachmentFileDto(attachmentFile);
    }

    /**
     * 파일 삭제
     */
    @Transactional
    public String fileDelete(String uuid){
        AttachmentFile attachmentFile = fileRepository.findById(uuid).orElseThrow(() ->new RuntimeException("삭제할 파일을 찾지 못하였습니다."));
        return attachmentFile.delete();
    }

    /**
     * 파일 여러 건 업로드
     */
    private List<AttachmentFileDto> storeFiles(List<MultipartFile> multipartFiles){
        List<AttachmentFileDto> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                AttachmentFile attachmentFile = fileStore.storeFile(multipartFile);
                AttachmentFile savedFile = fileRepository.save(attachmentFile);
                storeFileResult.add(new AttachmentFileDto(savedFile));
            }
        }
        return storeFileResult;
    }

    /**
     * DB 에 삭제 시간이 추가되어있는 파일의 실제 데이터를 제거
     * 스케쥴러 클래스 외에 사용 금지
     * 
     * 메소드명 개선 필요
     */
    @Transactional
    public void deletePhysicalAttachmentFile(){
        List<AttachmentFile> attachmentFiles = fileRepository.findAllIsDeleted();
        attachmentFiles.stream().forEach(file -> fileStore.deleteFile(file.getStoredName()));
    }
}
