package api.web_blogging.uz.controller;

import api.web_blogging.uz.dto.AppResponse;
import api.web_blogging.uz.dto.attach.AttachDTO;
import api.web_blogging.uz.services.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
public class AttachController {

    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    @Operation(summary = "Upload attach", description = "Api for upload attach")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }

    @GetMapping("/open/{fileName}")
    @Operation(summary = "Open attach", description = "Api for open attach")
    public ResponseEntity<Resource> open(@PathVariable String fileName) {
        return attachService.open(fileName);
    }

    @GetMapping("/download/{fineName}")
    @Operation(summary = "Download", description = "Api for download attach")
    public ResponseEntity<Resource> download(@PathVariable("fineName") String fileName) {
        return attachService.download(fileName);
    }

    @GetMapping("")
    @Operation(summary = "Get attach", description = "Api for get attach")
    public ResponseEntity<PageImpl<AttachDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "size", defaultValue = "15") int size) {
        return ResponseEntity.ok(attachService.getAll(page - 1, size));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete attach", description = "Api for delete attach")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String id) {
        return ResponseEntity.ok(attachService.delete(id));
    }

}
