package com.xxx.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_project")
@ApiModel(value="Project对象", description="项目")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目需求")
    private String projectRequirement;

    @ApiModelProperty(value = "项目提出人")
    private String projectAdmin;

    @ApiModelProperty(value = "项目提出时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate projectCreateTime;

    // 一人负责多个项目，每个项目都有起止时间，然后选出指定工期内能完成的最多项目以及项目名称
    @ApiModelProperty(value = "项目开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate projectStartTime;

    @ApiModelProperty(value = "项目结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate projectEndTime;

    @ApiModelProperty(value = "总工期")
    private Integer period;

    @ApiModelProperty(value = "项目是否启用")
    private Integer enabled;

}
