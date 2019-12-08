package fun.vyse.cloud.common.model;

/**
 * 时间公共字段
 * @author huangtianhui
 */
public interface ITimestampEntity<T> extends IBaseEntity<T>{
    Long getCreatedAt();

    Long getUpdatedAt();

    Long getDeletedAt();
}
