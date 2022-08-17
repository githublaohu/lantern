package com.lamp.lantern.service.core.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "AuthorityEntity", description = "权限实体类")
public class AuthorityEntity implements Serializable {
  /**
   * id
   */
  private Integer id;
  /**
   * 权限类型
   */
  private Integer authorityType;
  /**
   * 父权限id
   */
  private Integer parentAuthorityId;
  /**
   * 父权限名称
   */
  private String parentAuthorityName;
  /**
   * 权限名称
   */
  private String authorityName;
  /**
   * 权限描述
   */
  private String authorityDesc;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 修改时间
   */
  private Date updateTime;
  /**
   * 结束时间
   */
  private String terminalTime;
  /**
   * 有效时间
   */
  private Date validTime;
  /**
   * 状态
   */
  private String status;

}
