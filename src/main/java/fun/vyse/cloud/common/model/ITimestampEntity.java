package fun.vyse.cloud.common.model;

/**
 * 时间公共字段
 * @author huangtianhui
 */
public interface ITimestampEntity extends IBaseEntity{
    Long getCreatedAt();

    Long getUpdatedAt();

    Long getDeletedAt();
}
