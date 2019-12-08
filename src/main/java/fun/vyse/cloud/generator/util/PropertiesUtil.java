package fun.vyse.cloud.generator.util;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * PropertiesUtil
 *
 * @author junchen
 * @date 2019-12-08 22:44
 */
public class PropertiesUtil {
    public static String getPropertyAsString(@NonNull Properties properties, String key){
        return properties.getProperty(key);
    }

    public static String getPropertyAsString(@NonNull Properties properties,String key,String defaultValue){
        return properties.getProperty(key,defaultValue);
    }

    public static Boolean getPropertyAsBoolean(@NonNull Properties properties,String key){
        String property = properties.getProperty(key);
        if(StringUtils.isNoneBlank(property)){
            return Boolean.parseBoolean(property);
        }
        return false;
    }

    public static Boolean getPropertyAsBoolean(@NonNull Properties properties,String key,Boolean defaultValue){
        String property = properties.getProperty(key);
        if(StringUtils.isNoneBlank(property)){
            return Boolean.parseBoolean(property);
        }
        return defaultValue;
    }
}
