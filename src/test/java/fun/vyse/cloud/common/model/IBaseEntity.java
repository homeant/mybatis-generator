package fun.vyse.cloud.common.model;

import java.io.Serializable;

/**
 * 基础业务对象接口
 * @author huangtianhui
 */
public interface IBaseEntity<T> extends Serializable,Cloneable {
    /**
     * 获取业务主键
     * @return Integer.class
     */
    T getId();
}
