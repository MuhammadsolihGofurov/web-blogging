package api.web_blogging.uz.controller;


import api.web_blogging.uz.dto.AppResponse;
import api.web_blogging.uz.dto.PostDto;
import api.web_blogging.uz.dto.post.CreatePostDTO;
import api.web_blogging.uz.dto.post.PostDTO;
import api.web_blogging.uz.dto.post.PostFilterDTO;
import api.web_blogging.uz.entity.PostEntity;
import api.web_blogging.uz.enums.AppLang;
import api.web_blogging.uz.services.PostService;
import api.web_blogging.uz.utils.PageUtil;
import api.web_blogging.uz.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@Tag(name = "Post APIs", description = "API list for managing Post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    @Operation(summary = "Create post", description = "Api used for creating new post")
    public ResponseEntity<PostDTO> create(@Valid @RequestBody CreatePostDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {

        return ResponseEntity.ok(postService.createPost(dto, lang));
    }

    @GetMapping("/my-posts")
    @Operation(summary = "Get My posts", description = "Api used for get my posts")
    public ResponseEntity<PageImpl<PostDTO>> getMyPosts(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "12") int size) {

        return ResponseEntity.ok(postService.getMyPosts(PageUtil.page(page), size));
    }

    @GetMapping("/details/{id}")
    @Operation(summary = "Get by Id post", description = "Api used for get by id")
    public ResponseEntity<PostDTO> getById(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update by Id post", description = "Api used for update by id")
    public ResponseEntity<PostDTO> updateById(@PathVariable("id") String id, @Valid @RequestBody CreatePostDTO dto) {
        return ResponseEntity.ok(postService.update(id, dto));
    }

    @PutMapping("/delete/{id}")
    @Operation(summary = "Delete by Id post", description = "Api used for delete by id")
    public ResponseEntity<AppResponse<String>> deleteById(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.deleteAndVisibleFalse(id));
    }

    @PostMapping("/public/filter")
    @Operation(summary = "Filter Posts", description = "Api used for filter")
    public ResponseEntity<PageImpl<PostDTO>> filterPublic(@Valid @RequestBody PostFilterDTO dto,
                                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "size", defaultValue = "12") int size) {
        return ResponseEntity.ok(postService.filter(dto, PageUtil.page(page), size));
    }


}
