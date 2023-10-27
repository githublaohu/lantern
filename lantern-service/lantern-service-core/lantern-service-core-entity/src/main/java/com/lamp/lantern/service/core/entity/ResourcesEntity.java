package com.lamp.lantern.service.core.entity;

import com.lamp.lantern.service.core.entity.database.Resources;
import io.swagger.annotations.Api;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Api(value = "ResourcesEntity", description = "资源实体类")
public class ResourcesEntity extends Resources {



}
