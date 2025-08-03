package api.web_blogging.uz.services;

import api.web_blogging.uz.dto.attach.AttachDTO;
import api.web_blogging.uz.entity.AttachEntity;
import api.web_blogging.uz.exps.AppBadException;
import api.web_blogging.uz.repository.AttachRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachService {

    @Value("${attach.upload.folder}")
    private String folderName;

    @Value("${attach.url}")
    private String attachUrl;

    @Autowired
    private AttachRepository attachRepository;

    public AttachDTO upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppBadException("File not found");
        }

        try {
            String pathFolder = getYmDString(); // 2024/09/27
            String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
            String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename())); // .jpg, .png, .mp4

            // create folder if not exists
            File folder = new File(folderName + "/" + pathFolder);
            if (!folder.exists()) {
                boolean t = folder.mkdirs();
            }

            // save to system
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            Files.write(path, bytes);

            // save to db
            AttachEntity entity = new AttachEntity();
            entity.setId(key + "." + extension);
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            entity.setOrigenName(file.getOriginalFilename());
            entity.setExtension(extension);
            entity.setVisible(true);
            attachRepository.save(entity);

            return toDTO(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<Resource> open(String id) {
        AttachEntity entity = getEntity(id);
        Path filePath = Paths.get(getPath(entity)).normalize();
        UrlResource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + id);
            }
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Fallback content type
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body((Resource) resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Resource> download(String id) {
        try {
            AttachEntity entity = getEntity(id);
            Path filePath = Paths.get(getPath(entity)).normalize();
            UrlResource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOrigenName() + "\"").body((Resource) resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not read the file!");
        }
    }

    public boolean delete(String id) {
        AttachEntity entity = getEntity(id);
        attachRepository.delete(entity);
        File file = new File(getPath(entity));
        boolean b = false;
        if (file.exists()) {
            b = file.delete();
        }
        return b;
    }

    public PageImpl<AttachDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AttachEntity> entityPages = (Page<AttachEntity>) attachRepository.findAll(pageable);
        return new PageImpl<>(entityPages.stream().map(this::toDTO).toList(), pageable, entityPages.getTotalElements());
    }

    /**
     * Other Util Methods
     */
    private String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    private String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public AttachEntity getEntity(String id) {
        Optional<AttachEntity> optional = attachRepository.findById(id);
        if (optional.isEmpty()) {
//            log.warn("Attach error : file not found");
            throw new AppBadException("File not found");
        }
        return optional.get();
    }

    private String getPath(AttachEntity entity) {
        return folderName + "/" + entity.getPath() + "/" + entity.getId();
    }

    public String openURL(String fileName) {
        return attachUrl + "/open/" + fileName;
    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getId());
        attachDTO.setOriginName(entity.getOrigenName());
        attachDTO.setSize(entity.getSize());
        attachDTO.setExtension(entity.getExtension());
        attachDTO.setCreatedData(entity.getCreatedDate());
        attachDTO.setUrl(openURL(entity.getId()));
        return attachDTO;
    }

    public AttachDTO attachDTO(String photoId) {
        if (photoId == null) return null;

        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(photoId);
        attachDTO.setUrl(openURL(photoId));
        return attachDTO;
    }

}
