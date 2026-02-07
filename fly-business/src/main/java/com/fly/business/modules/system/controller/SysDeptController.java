package com.fly.business.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fly.business.modules.system.model.entity.SysDept;
import com.fly.business.modules.system.service.SysDeptService;
import com.fly.common.exception.ResponseException;
import com.fly.common.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 部门 Controller
 */
@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService deptService;

    /**
     * 查询部门列表（树形）
     */
    @GetMapping("/list")
    public ResponseData<List<SysDept>> list(@RequestParam(required = false) String deptName) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(deptName), SysDept::getDeptName, deptName)
                .eq(SysDept::getDelFlag, "0")
                .orderByAsc(SysDept::getParentId, SysDept::getOrderNum);
        return ResponseData.success(deptService.list(wrapper));
    }

    /**
     * 获取部门详情
     */
    @GetMapping("/{deptId}")
    public ResponseData<SysDept> getInfo(@PathVariable Long deptId) {
        SysDept dept = deptService.getById(deptId);
        if (dept == null) {
            throw new ResponseException("部门不存在");
        }
        return ResponseData.success(dept);
    }

    /**
     * 新增部门
     */
    @PostMapping("/add")
    public ResponseData<Void> add(@RequestBody SysDept dept) {
        dept.setDelFlag("0");
        boolean result = deptService.save(dept);
        return result ? ResponseData.success() : ResponseData.error("新增部门失败");
    }

    /**
     * 修改部门
     */
    @PutMapping("/edit")
    public ResponseData<Void> edit(@RequestBody SysDept dept) {
        if (dept.getDeptId() == null) {
            throw new ResponseException("部门ID不能为空");
        }
        boolean result = deptService.updateById(dept);
        return result ? ResponseData.success() : ResponseData.error("修改部门失败");
    }

    /**
     * 删除部门（逻辑删除）
     */
    @DeleteMapping("/{deptIds}")
    public ResponseData<Void> remove(@PathVariable String deptIds) {
        List<String> idList = Arrays.asList(deptIds.split(","));
        idList.forEach(id -> {
            SysDept dept = new SysDept();
            dept.setDeptId(Long.parseLong(id));
            dept.setDelFlag("1");
            deptService.updateById(dept);
        });
        return ResponseData.success();
    }
}
