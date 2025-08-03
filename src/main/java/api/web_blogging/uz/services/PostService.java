package api.web_blogging.uz.services;

import api.web_blogging.uz.dto.AppResponse;
import api.web_blogging.uz.dto.FilterResultDTO;
import api.web_blogging.uz.dto.post.CreatePostDTO;
import api.web_blogging.uz.dto.post.PostDTO;
import api.web_blogging.uz.dto.post.PostFilterDTO;
import api.web_blogging.uz.entity.PostEntity;
import api.web_blogging.uz.enums.AppLang;
import api.web_blogging.uz.enums.ProfileRole;
import api.web_blogging.uz.exps.AppBadException;
import api.web_blogging.uz.repository.CustomRepository;
import api.web_blogging.uz.repository.PostRepository;
import api.web_blogging.uz.utils.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ResourceBundleService getMessageService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private CustomRepository customRepository;

    public PostDTO createPost(CreatePostDTO dto, AppLang lang) {
        PostEntity post = new PostEntity();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setPhotoId(dto.getAttach().getId());
        post.setCreatedAt(LocalDateTime.now());
        post.setVisible(true);
        //  owner
        post.setProfileId(SpringSecurityUtil.getCurrentProfileId());
        postRepository.save(post);

        return toDTO(post);

//        return new AppResponse<>(getMessageService.getMessage("post.create.success", lang));
    }

    public PageImpl<PostDTO> getMyPosts(int page, int size) {
        Integer userId = SpringSecurityUtil.getCurrentProfileId();
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<PostEntity> results = postRepository.getAllByProfileIdAndVisibleTrueOrderByCreatedAtDesc(userId, pageRequest);

        List<PostDTO> dtoLists = results.getContent().stream()
                .map(post -> toDTO(post))
                .toList();

        return new PageImpl<>(dtoLists, pageRequest, results.getTotalElements());

    }

    public PostDTO getById(String id) {
        PostEntity entity = getPost(id);

        return toDTO(entity);
    }

    public PostDTO update(String id, CreatePostDTO dto) {
        PostEntity entity = getPost(id);

        if (!SpringSecurityUtil.hasRole(ProfileRole.ROLE_ADMIN) && entity.getProfileId().equals(SpringSecurityUtil.getCurrentProfileId())) {
            throw new AppBadException("You dont have permission to update this post");
        }

        String deletedPhotoId = null;

        // delete old image
        if (!dto.getAttach().getId().equals(entity.getPhotoId())) {
            deletedPhotoId = entity.getPhotoId();
        }

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setPhotoId(dto.getAttach().getId());
        postRepository.save(entity);

        if (deletedPhotoId != null) {
            attachService.delete(deletedPhotoId);
        }

        return toDTO(entity);
    }

    public AppResponse<String> delete(String id) {
        PostEntity entity = getPost(id);
        postRepository.delete(entity);
        return new AppResponse<>("Deleted post");
    }

    public AppResponse<String> deleteAndVisibleFalse(String id) {
        PostEntity entity = getPost(id);

        if (!SpringSecurityUtil.hasRole(ProfileRole.ROLE_ADMIN) && entity.getProfileId().equals(SpringSecurityUtil.getCurrentProfileId())) {
            throw new AppBadException("You dont have permission to update this post");
        }

        postRepository.deleteByIdAndVisibleFalse(id);

        return new AppResponse<>("Deleted post");
    }

    public PageImpl<PostDTO> filter(PostFilterDTO filterDto, int page, int size) {
        FilterResultDTO<PostEntity> resultDTO = customRepository.filter(filterDto, page, size);
        List<PostDTO> dtoLists = resultDTO.getList().stream().map(this::toDTO).toList();

        return new PageImpl<>(dtoLists, PageRequest.of(page, size), resultDTO.getTotalCount());
    }

    //  additional methods

    public PostEntity getPost(String id) {
        return postRepository.findById(id).orElseThrow(() -> {
            throw new AppBadException("Post Not Found" + id);
        });
    }

    public PostDTO toDTO(PostEntity post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setAttach(attachService.attachDTO(post.getPhotoId()));

        return dto;
    }


}
