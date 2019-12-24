# mybatis-generator

一键生成`dao`层代码、`DTO`、`convert`,支持Mybatis 3.5新特性`Optional`

```xml
<context id="mysql" targetRuntime="MyBatis3" defaultModelType="flat">
        <property name="useMapperCommentGenerator" value="true"/>
        <property name="baseJavaPackage" value="fun.vyse.cloud.test"/>
        <property name="targetProject" value="./src/test/java"/>
        <property name="servicePackage" value="service"/>
        <property name="serviceImplPackage" value="service.impl"/>
        <property name="dtoImplPackage" value="dto"/>
        <property name="convertPackage" value="convert"/>

        <plugin type="fun.vyse.cloud.generator.mybatis.plugin.VysePlugin">
            <property name="lombok" value="true"/>
            <property name="lombokBuilder" value="true"/>
            <property name="enableSwagger" value="true"/>
            <property name="enableValidation" value="true"/>
            <property name="createService" value="true"/>
            <property name="createDto" value="true"/>
            <property name="createConvert" value="true"/>
            <property name="enableOptional" value="true"/>
        </plugin>
</context>        
```