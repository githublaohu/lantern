package com.lamp.lantern.service.core.consumer.controller;

import com.lamp.lantern.service.core.consumer.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.service.core.service.ResourcesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/resources")
@RestController("resourcesController")
@Api(tags = {"资源管理接口"})
public class ResourcesController {

    @Reference(url = "127.0.0.1:20880")
    private ResourcesService resourcesService;

    @ApiOperation(value = "插入新资源")
    @RequestMapping(value = "insertResources")
    public ResultObjectEnums insertResources(@RequestBody ResourcesEntity resourcesEntity) {
        Integer integer = resourcesService.insertResource(resourcesEntity);
        return ResultObjectEnums.SUCCESS;
    }
    @ApiOperation(value = "批量插入新资源")
    @RequestMapping(value = "insertResourcesBatch")
    public ResultObjectEnums insertResourcesBatch(@RequestBody ResourcesEntity resourcesEntity) {
        Integer integer = resourcesService.insertResource(resourcesEntity);
        return ResultObjectEnums.SUCCESS;
    }
    @ApiOperation(value = "Form表单更新资源")
    @RequestMapping(value = "updateResourcesForm")
    public ResultObjectEnums updateResources(@RequestBody ResourcesEntity resourcesEntity) {
        Integer integer = resourcesService.updateByForm(resourcesEntity);
        return ResultObjectEnums.SUCCESS;
    }
    @ApiOperation(value = "删除资源")
    @RequestMapping(value = "deleteResources")
    public ResultObjectEnums deleteResources(@RequestBody ResourcesEntity resourcesEntity) {
        Integer integer = resourcesService.deleteResource(resourcesEntity);
        return ResultObjectEnums.SUCCESS;
    }
    @ApiOperation(value = "批量删除资源")
    @RequestMapping(value = "deleteResourcesBatch")
    public ResultObjectEnums deleteResourcesBatch(@RequestBody List<ResourcesEntity> resourcesEntityList) {
        Integer integer = resourcesService.deleteResources(resourcesEntityList);
        return ResultObjectEnums.SUCCESS;
    }
    @ApiOperation(value = "按照项目id查询资源")
    @RequestMapping(value = "selectResourcesByProjectId")
    public Resources selectResourcesByProjectId(@RequestBody ResourcesEntity resourcesEntity) {
        return resourcesService.selectByProjectId(resourcesEntity);
    }
    @ApiOperation(value = "按照模块id查询资源")
    @RequestMapping(value = "selectResourcesByModuleId")
    public Resources selectResourcesByModuleId(@RequestBody ResourcesEntity resourcesEntity) {
        return resourcesService.selectByModuleId(resourcesEntity);
    }
    @ApiOperation(value = "按照资源id查询资源")
    @RequestMapping(value = "selectResourcesById")
    public Resources selectResourcesById(@RequestBody ResourcesEntity resourcesEntity) {
        return resourcesService.selectById(resourcesEntity);
    }
    @ApiOperation(value = "按照资源表单查询资源")
    @RequestMapping(value = "selectResourcesByForm")
    public List<Resources> selectResourcesByForm(@RequestBody ResourcesEntity resourcesEntity) {
        return resourcesService.selectByForm(resourcesEntity);
    }
    @ApiOperation(value = "按照用户ID查询资源")
    @RequestMapping(value = "selectResourcesByUserId")
    public List<Resources> selectResourcesByUserId(@RequestBody UserInfoEntity userInfoEntity) {
        return resourcesService.selectResourcesByUserId(userInfoEntity);
    }
}
