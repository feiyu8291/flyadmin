package com.fly.business.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.business.modules.system.model.entity.SysPost;
import com.fly.business.modules.system.service.SysPostService;
import com.fly.common.exception.ResponseException;
import com.fly.common.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 岗位信息 Controller
 */
@RestController
@RequestMapping("/system/post")
@RequiredArgsConstructor
public class SysPostController {

    private final SysPostService postService;

    /**
     * 分页查询岗位列表
     */
    @GetMapping("/list")
    public ResponseData<Page<SysPost>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String postName,
            @RequestParam(required = false) String postCode) {
        Page<SysPost> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(postName), SysPost::getPostName, postName)
                .like(StringUtils.hasText(postCode), SysPost::getPostCode, postCode)
                .orderByAsc(SysPost::getPostSort);
        return ResponseData.success(postService.page(page, wrapper));
    }

    /**
     * 获取岗位详情
     */
    @GetMapping("/{postId}")
    public ResponseData<SysPost> getInfo(@PathVariable Long postId) {
        SysPost post = postService.getById(postId);
        if (post == null) {
            throw new ResponseException("岗位不存在");
        }
        return ResponseData.success(post);
    }

    /**
     * 新增岗位
     */
    @PostMapping("/add")
    public ResponseData<Void> add(@RequestBody SysPost post) {
        // 检查岗位编码是否已存在
        long count = postService.count(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getPostCode, post.getPostCode()));
        if (count > 0) {
            throw new ResponseException("岗位编码已存在");
        }

        boolean result = postService.save(post);
        return result ? ResponseData.success() : ResponseData.error("新增岗位失败");
    }

    /**
     * 修改岗位
     */
    @PutMapping("/edit")
    public ResponseData<Void> edit(@RequestBody SysPost post) {
        if (post.getPostId() == null) {
            throw new ResponseException("岗位ID不能为空");
        }
        boolean result = postService.updateById(post);
        return result ? ResponseData.success() : ResponseData.error("修改岗位失败");
    }

    /**
     * 删除岗位
     */
    @DeleteMapping("/{postIds}")
    public ResponseData<Void> remove(@PathVariable String postIds) {
        List<String> idList = Arrays.asList(postIds.split(","));
        boolean result = postService.removeByIds(idList);
        return result ? ResponseData.success() : ResponseData.error("删除岗位失败");
    }
}
