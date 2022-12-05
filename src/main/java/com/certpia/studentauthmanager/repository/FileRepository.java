package com.certpia.studentauthmanager.repository;

import com.certpia.studentauthmanager.domain.AttachmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<AttachmentFile, String> {

    @Query("select af from AttachmentFile af where af.deletedAt is null")
    List<AttachmentFile> findAllNotDeleted();

    @Query("select af from AttachmentFile af where af.deletedAt is not null")
    List<AttachmentFile> findAllIsDeleted();
}
