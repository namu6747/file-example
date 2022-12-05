package com.certpia.studentauthmanager.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class AttachmentFileRequestParam {
    private List<MultipartFile> attachments;
}
