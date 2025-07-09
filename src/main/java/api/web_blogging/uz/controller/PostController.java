package api.web_blogging.uz.controller;


import api.web_blogging.uz.dto.PostDto;
import api.web_blogging.uz.utils.SpringSecurityUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping("/create")
    public String create(@RequestBody PostDto postDto) {
        String text = String.valueOf((Integer) SpringSecurityUtil.getCurrentProfileId());
        return text;
    }
}
