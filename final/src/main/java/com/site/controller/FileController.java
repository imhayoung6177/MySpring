package com.site.controller;

import com.site.domain.File;
import com.site.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    // application.properties에 설정한 파일 업로드 디렉토리 경로 주입.
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 이미지 파일을 브라우저에 보여주는 엔드포인트
     * @param filename : 서버에 저장된 고유한 파일 이름
     * @return 이미지 리소스
     * @throws MalformedURLException : 잘못된 파일 경로일 경우 예외 발생
     */
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> displayImage(@PathVariable String filename) throws MalformedURLException {
        Resource resource = new UrlResource("file:"+uploadDir+filename);
        if(resource.exists()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(resource);
    }
    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable long fileId) throws MalformedURLException {
        File file = fileService.findById(fileId);
        //Resource resource = new UrlResource("file:"+file.getFile_path());
        if(file == null){
            return ResponseEntity.notFound().build();
        }
        UrlResource resource = new UrlResource("file"+file.getFile_path());
        //다운로드시 표시될 파일 이름을 UTF-8 인코딩 한글깨짐 방지
        String encodedOriginalName = UriUtils.encode(file.getOriginal_name(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header("Content-Disposition","attachment; filename=\""+encodedOriginalName+"\"")
                .body(resource);
    }

}
