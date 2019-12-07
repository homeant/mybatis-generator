package fun.vyse.cloud.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Huang Tian Hui
 */
public class Generator {
    public static void main( String[] args ) throws Exception {
        List<String> warnings = new ArrayList<String>();
        ClassLoader classLoader = Generator.class.getClassLoader();
        URL url = classLoader.getResource("generatorConfig.xml");
        File configFile = new File(url.getFile());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
