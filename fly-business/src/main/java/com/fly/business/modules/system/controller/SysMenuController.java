package com.fly.business.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fly.business.modules.system.model.entity.SysMenu;
import com.fly.business.modules.system.service.SysMenuService;
import com.fly.common.exception.ResponseException;
import com.fly.common.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 菜单权限 Controller
 */
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    /**
     * 查询菜单列表（树形）
     */
    @GetMapping("/list")
    public ResponseData<List<SysMenu>> list(@RequestParam(required = false) String menuName) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menuName), SysMenu::getMenuName, menuName)
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);
        return ResponseData.success(menuService.list(wrapper));
    }

    /**
     * 获取菜单详情
     */
    @GetMapping("/{menuId}")
    public ResponseData<SysMenu> getInfo(@PathVariable Long menuId) {
        SysMenu menu = menuService.getById(menuId);
        if (menu == null) {
            throw new ResponseException("菜单不存在");
        }
        return ResponseData.success(menu);
    }

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    public ResponseData<Void> add(@RequestBody SysMenu menu) {
        boolean result = menuService.save(menu);
        return result ? ResponseData.success() : ResponseData.error("新增菜单失败");
    }

    /**
     * 修改菜单
     */
    @PutMapping("/edit")
    public ResponseData<Void> edit(@RequestBody SysMenu menu) {
        if (menu.getMenuId() == null) {
            throw new ResponseException("菜单ID不能为空");
        }
        boolean result = menuService.updateById(menu);
        return result ? ResponseData.success() : ResponseData.error("修改菜单失败");
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuIds}")
    public ResponseData<Void> remove(@PathVariable String menuIds) {
        List<String> idList = Arrays.asList(menuIds.split(","));
        boolean result = menuService.removeByIds(idList);
        return result ? ResponseData.success() : ResponseData.error("删除菜单失败");
    }
}
