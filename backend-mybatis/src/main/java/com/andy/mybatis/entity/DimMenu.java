package com.andy.mybatis.entity;

    import com.baomidou.mybatisplus.annotation.TableName;
    import java.time.LocalDateTime;
    import com.andy.mybatis.entity.BaseEntity;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author Generator
* @since 2019-11-19
*/
    @Data
        @EqualsAndHashCode(callSuper = true)
    @Accessors(chain = true)
    @TableName("dim_menu")
    public class DimMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String url;


}
