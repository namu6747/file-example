package com.certpia.studentauthmanager.dto;

import com.certpia.studentauthmanager.domain.AttachmentFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentFileDto {

    private String id;
    private String originalName;
    private String storedName;
    private String fileExt;
    private Long fileSize;

    public AttachmentFileDto(AttachmentFile file){
        this.id = file.getId();
        this.originalName = file.getOriginalName();
        this.storedName = file.getStoredName();
        this.fileExt = file.getFileExt();
        this.fileSize = file.getFileSize();
    }
}
