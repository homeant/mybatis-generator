package fun.vyse.cloud.supplier.common.model;

import java.io.Serializable;

/**
 * 基础业务对象接口
 * @author huangtianhui
 */
public interface IBaseEntity extends Serializable,Cloneable {
    /**
     * 获取业务主键
     * @return Integer.class
     */
    Integer getId();
}
