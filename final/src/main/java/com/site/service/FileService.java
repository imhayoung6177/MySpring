package com.site.service;

import com.site.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;

    // application.properties에 설정한 파일 업로드 디렉토리 경로 주입.
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 파일을 서버에 물리적으로 저장하고 DB에 정보를 기록합니다.
     * @param boardId : 파일이 첨부될 게시글의 PK
     * @param multipartFile : 컨트롤러에서 받은 MultipartFile 객체
     */
    @Transactional
    public void save(long boardId, MultipartFile multipartFile) {
        // 1. 사용자가 파일을 첨부하지 않았으면, 메서드를 그냥 종료
        if (multipartFile.isEmpty()) {
            return;
        }

        try {
            // 1. 원본 파일명 추출
            String originalName = multipartFile.getOriginalFilename(); // ex: dog.png

            // 2. 서버 저장용 고유 파일명 생성 (UUID 사용)
            String uuid = UUID.randomUUID().toString(); // ex: a1b2c3d4-e5f6-7890-1234-567890abcdef
            String extension = originalName.substring(originalName.lastIndexOf(".")); // .png
            // 서버에 저장될 파일의 이름과 확장자
            String storedName = uuid + extension; // a1b2c3d4-e5f6-7890-1234-567890abcdef.png
            String uploadPath = uploadDir + storedName; // C:/upload/a1b2c3d4-e5f6-7890-1234-567890abcdef.png

            // 3. 파일 저장 경로 설정 및 폴더 생성
            File saveDir = new File(uploadPath);
            // 저장할 upload 폴더가 존재하지 않는다면
            if (!saveDir.exists()) {
                // upload 폴더 생성
                saveDir.mkdirs();
            }

            // 4. 파일을 지정된 경로에 물리적(DB)으로 저장
            File targetFile = new File(uploadPath + storedName);
            // ★ MultipartFile 객체를 실제 파일상태로 변환하여 지정된 서버 경로에 저장
            multipartFile.transferTo(targetFile);

            // 5. DB 저장을 위한 File 도메인 객체 생성하고 값을 설정
            com.site.domain.File fileEntity = new com.site.domain.File();
            fileEntity.setBoard_id(boardId);
            fileEntity.setOriginal_name(originalName);
            fileEntity.setStored_name(storedName);
            fileEntity.setFile_path(uploadPath + storedName);

            // 6. Mapper를 통해 DB에 파일 정보 저장
            fileMapper.save(fileEntity);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 특정 게시글에 첨부된 모든 파일 정보 조회
     * @param bno : 이미지가 저장된 게시글의 PK
     * @return 파일 정보 목록
     */
    public List<com.site.domain.File> findFilesByBoardId(long bno) {
        return fileMapper.findFilesByBoardId(bno);
    }

    public com.site.domain.File findById(long fileId) {
        return fileMapper.findById(fileId);
    }
}