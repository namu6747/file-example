package com.certpia.studentauthmanager.config;

import com.certpia.studentauthmanager.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final FileService fileService;

    /**
     * scheduler sample
     */
    @Scheduled(cron = "0 * * * * ?")
    public void sample() {
        log.info("start sample scheduler");
    }

    /**
     * 서버에 저장된 파일만 삭제
     * 데이터베이스에서도 삭제하고 싶다면 벌크 쿼리 작성
     */
    @Scheduled(cron = "0 0 22 * * *")
    public void deletePhysicalAttachmentFile() {
        fileService.deletePhysicalAttachmentFile();
    }
}
