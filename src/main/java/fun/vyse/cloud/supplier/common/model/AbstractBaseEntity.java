package fun.vyse.cloud.supplier.common.model;

import lombok.Data;

/**
 * 基础实体基类
 * @author huangtianhui
 */
@Data
public abstract class AbstractBaseEntity implements ITimestampEntity {

    private Integer id;

    /**
     * 创建时间
     */
    private Long createdAt;

    /**
     * 更新时间
     */
    private Long updatedAt;

    /**
     * 删除时间
     */
    private Long deletedAt;

    public static final String ID_COLUMN = "id";
    public static final String CREATED_AT_COLUMN = "created_at";
    public static final String UPDATED_AT_COLUMN = "updated_at";
    public static final String DELETED_AT_COLUMN = "deleted_at";

}
