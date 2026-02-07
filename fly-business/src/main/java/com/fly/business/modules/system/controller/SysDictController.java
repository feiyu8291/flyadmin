package com.fly.business.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.business.modules.system.model.entity.SysDictData;
import com.fly.business.modules.system.model.entity.SysDictType;
import com.fly.business.modules.system.service.SysDictDataService;
import com.fly.business.modules.system.service.SysDictTypeService;
import com.fly.common.exception.ResponseException;
import com.fly.common.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 字典管理 Controller
 */
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictTypeService dictTypeService;
    private final SysDictDataService dictDataService;

    // ==================== 字典类型 ====================

    /**
     * 分页查询字典类型列表
     */
    @GetMapping("/type/list")
    public ResponseData<Page<SysDictType>> typeList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String dictName,
            @RequestParam(required = false) String dictType) {
        Page<SysDictType> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(dictName), SysDictType::getDictName, dictName)
                .like(StringUtils.hasText(dictType), SysDictType::getDictType, dictType)
                .orderByDesc(SysDictType::getCreateTime);
        return ResponseData.success(dictTypeService.page(page, wrapper));
    }

    /**
     * 获取字典类型详情
     */
    @GetMapping("/type/{dictId}")
    public ResponseData<SysDictType> getTypeInfo(@PathVariable Long dictId) {
        SysDictType dictType = dictTypeService.getById(dictId);
        if (dictType == null) {
            throw new ResponseException("字典类型不存在");
        }
        return ResponseData.success(dictType);
    }

    /**
     * 新增字典类型
     */
    @PostMapping("/type/add")
    public ResponseData<Void> addType(@RequestBody SysDictType dictType) {
        // 检查字典类型是否已存在
        long count = dictTypeService.count(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDictType, dictType.getDictType()));
        if (count > 0) {
            throw new ResponseException("字典类型已存在");
        }

        boolean result = dictTypeService.save(dictType);
        return result ? ResponseData.success() : ResponseData.error("新增字典类型失败");
    }

    /**
     * 修改字典类型
     */
    @PutMapping("/type/edit")
    public ResponseData<Void> editType(@RequestBody SysDictType dictType) {
        if (dictType.getDictId() == null) {
            throw new ResponseException("字典ID不能为空");
        }
        boolean result = dictTypeService.updateById(dictType);
        return result ? ResponseData.success() : ResponseData.error("修改字典类型失败");
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/type/{dictIds}")
    public ResponseData<Void> removeType(@PathVariable String dictIds) {
        List<String> idList = Arrays.asList(dictIds.split(","));
        boolean result = dictTypeService.removeByIds(idList);
        return result ? ResponseData.success() : ResponseData.error("删除字典类型失败");
    }

    // ==================== 字典数据 ====================

    /**
     * 根据字典类型查询字典数据
     */
    @GetMapping("/data/type/{dictType}")
    public ResponseData<List<SysDictData>> getDataByType(@PathVariable String dictType) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getUseStatus, "0")
                .orderByAsc(SysDictData::getDictSort);
        return ResponseData.success(dictDataService.list(wrapper));
    }

    /**
     * 分页查询字典数据列表
     */
    @GetMapping("/data/list")
    public ResponseData<Page<SysDictData>> dataList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String dictType,
            @RequestParam(required = false) String dictLabel) {
        Page<SysDictData> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(dictType), SysDictData::getDictType, dictType)
                .like(StringUtils.hasText(dictLabel), SysDictData::getDictLabel, dictLabel)
                .orderByAsc(SysDictData::getDictSort);
        return ResponseData.success(dictDataService.page(page, wrapper));
    }

    /**
     * 获取字典数据详情
     */
    @GetMapping("/data/{dictCode}")
    public ResponseData<SysDictData> getDataInfo(@PathVariable Long dictCode) {
        SysDictData dictData = dictDataService.getById(dictCode);
        if (dictData == null) {
            throw new ResponseException("字典数据不存在");
        }
        return ResponseData.success(dictData);
    }

    /**
     * 新增字典数据
     */
    @PostMapping("/data/add")
    public ResponseData<Void> addData(@RequestBody SysDictData dictData) {
        boolean result = dictDataService.save(dictData);
        return result ? ResponseData.success() : ResponseData.error("新增字典数据失败");
    }

    /**
     * 修改字典数据
     */
    @PutMapping("/data/edit")
    public ResponseData<Void> editData(@RequestBody SysDictData dictData) {
        if (dictData.getDictCode() == null) {
            throw new ResponseException("字典编码不能为空");
        }
        boolean result = dictDataService.updateById(dictData);
        return result ? ResponseData.success() : ResponseData.error("修改字典数据失败");
    }

    /**
     * 删除字典数据
     */
    @DeleteMapping("/data/{dictCodes}")
    public ResponseData<Void> removeData(@PathVariable String dictCodes) {
        List<String> idList = Arrays.asList(dictCodes.split(","));
        boolean result = dictDataService.removeByIds(idList);
        return result ? ResponseData.success() : ResponseData.error("删除字典数据失败");
    }
}
