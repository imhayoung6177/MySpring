package com.site.mapper;

import com.site.domain.File;

import java.util.List;

public interface FileMapper {
    void save(File fileEntity);

    List<File> findFilesByBoardId(long bno);

    File findById(long fileId);
}
