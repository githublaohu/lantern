package com.lamp.lantern.service.core.entity;

import java.util.Date;

public class ResourcesEntity {
  /**
   * id
   */
  private Integer id;
  /**
   * 系统id
   */
  private Integer systemId;
  /**
   * 项目id
   */
  private Integer projectId;
  /**
   * 项目名称
   */
  private String projectName;
  /**
   * 模块id
   */
  private Integer moduleId;
  /**
   * 模块名称
   */
  private String moduleName;
  /**
   * 资源类型
   */
  private String resourcesType;
  /**
   * 资源名
   */
  private String resourcesName;
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
  /**
   * 操作
   */
  private String operator;
  /**
   * 条件
   */
  private String conditions;

}
