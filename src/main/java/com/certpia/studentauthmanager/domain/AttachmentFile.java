package com.certpia.studentauthmanager.domain;

import com.certpia.studentauthmanager.util.FileStore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 첨부 파일
 *
 * 현재 수정화면에서 삭제 누르면 저장 안 하더라도 파일 삭제
 * 
 * 파일 시스템 점진적 발전(아무것도 안함)
 *
 * 0. BootStrap or Dropzone Api 연동
 * 1. 게시판과 결합
 * 2. input type="files" on change 때 파일 임시 폴더에 미리 업로드
 *     저장시 보관용 폴더에 복사
 * 3. 게시글 수정시 중복되는 경우 방어
 * 4. 완전한 파일 삭제 여부, 디비 삭제 여부 결정
 */
@Slf4j
@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AttachmentFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "stored_name")
    private String storedName;

    @Column(name = "saved_at")
    private LocalDateTime savedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_size")
    private Long fileSize;

    public AttachmentFile(MultipartFile file) {
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExt = FileStore.extractExt(originalName);
        String storedName = FileStore.createUUIDFileName(fileExt);

        this.originalName = originalName;
        this.storedName = storedName;
        this.savedAt = LocalDateTime.now();
        this.deletedAt = null;
        this.fileExt = fileExt;
        this.fileSize = file.getSize();
    };

    public String delete(){
        this.deletedAt = LocalDateTime.now();
        return this.deletedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
