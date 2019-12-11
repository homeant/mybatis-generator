package fun.vyse.cloud.generator.mybatis.plugin;

import com.google.common.collect.Lists;
import fun.vyse.cloud.generator.mybatis.plugin.comment.VyseCommentGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.DomainObjectRenamingRule;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fun.vyse.cloud.generator.util.PropertiesUtil.getPropertyAsBoolean;
import static fun.vyse.cloud.generator.util.PropertiesUtil.getPropertyAsString;

/**
 * ModelPlugin
 *
 * @author junchen
 * @date 2019-12-07 11:52
 */
public class VysePlugin extends PluginAdapter {

    private boolean lombok = false;

    private boolean lombokBuilder = false;

    private boolean swagger = false;

    private boolean comment = false;

    private boolean service = false;

    private String targetProject;

    private String baseJavaPackage;

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (lombok) {
            topLevelClass.addImportedType("lombok.Data");
            topLevelClass.addAnnotation("@Data");
            if (topLevelClass.getSuperClass().isPresent()) {
                topLevelClass.addImportedType("lombok.EqualsAndHashCode");
                topLevelClass.addImportedType("lombok.ToString");
                topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper = false)");
                topLevelClass.addAnnotation("@ToString(callSuper = true)");
            }
        }
        if (lombokBuilder) {
            topLevelClass.addImportedType("lombok.Builder");
            topLevelClass.addAnnotation("@Builder");
        }
        if (swagger) {
            //导包
            topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
            topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
            //增加注解(去除注释中的转换符)
            String remarks = introspectedTable.getRemarks();
            if (remarks == null) {
                remarks = "";
            }
            remarks = remarks.replaceAll("\r", "").replaceAll("\n", "");
            if (StringUtils.isNoneBlank(remarks)) {
                topLevelClass.addAnnotation("@ApiModel(\"" + remarks + "\")");
            } else {
                topLevelClass.addAnnotation("@ApiModel(\"" + topLevelClass.getType().getShortName() + "\")");
            }
        }
        return true;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        StringBuffer buffer = new StringBuffer("deleteBy");
        String methodName = getByPrimaryKeyMethodName(buffer, introspectedTable);
        method.setName(methodName);
        return super.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        StringBuffer buffer = new StringBuffer("selectBy");
        String methodName = getByPrimaryKeyMethodName(buffer, introspectedTable);
        method.setName(methodName);
        return super.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        StringBuffer buffer = new StringBuffer("updateBy");
        String methodName = getByPrimaryKeyMethodName(buffer, introspectedTable);
        methodName = methodName+"Selective";
        method.setName(methodName);
        List<String> annotations = method.getAnnotations();
        if(CollectionUtils.isNotEmpty(annotations)){
            for (int i = 0; i < annotations.size(); i++) {
                String annotation = annotations.get(i);
                if(annotation.indexOf("@UpdateProvider")==0){
                    annotation = annotation.replaceAll("updateByPrimaryKeySelective",methodName);
                    annotations.set(i,annotation);
                }
            }
        }
        return super.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        StringBuffer buffer = new StringBuffer("updateBy");
        String methodName = getByPrimaryKeyMethodName(buffer, introspectedTable);
        method.setName(methodName);
        return super.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        StringBuffer buffer = new StringBuffer("updateBy");
        String methodName = getByPrimaryKeyMethodName(buffer, introspectedTable);
        methodName = methodName+"Blobs";
        method.setName(methodName);
        return super.clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        StringBuffer buffer = new StringBuffer("updateBy");
        String methodName = getByPrimaryKeyMethodName(buffer, introspectedTable);
        methodName = methodName+"Selective";
        method.setName(methodName);
        return super.providerUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !lombok;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !lombok;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (swagger) {
            String remarks = introspectedColumn.getRemarks();
            remarks = remarks.replaceAll("\r", "").replaceAll("\n", "");
            if (StringUtils.isNoneBlank(remarks)) {
                StringBuffer buffer = new StringBuffer("@ApiModelProperty(");
                buffer.append("value=\"" + remarks + "\"");
                buffer.append(")");
                field.addAnnotation(buffer.toString());
            }
        }
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> javaFiles = super.contextGenerateAdditionalJavaFiles(introspectedTable);
        if (service) {
            if (CollectionUtils.isEmpty(javaFiles)) {
                javaFiles = Lists.newArrayList();
            }
            DomainObjectRenamingRule rule = introspectedTable.getTableConfiguration().getDomainObjectRenamingRule();
            String domainObjectName = introspectedTable.getTableConfiguration().getTableName();
            domainObjectName = JavaBeansUtil.getCamelCaseString(domainObjectName, true);
            if (rule != null) {
                Pattern pattern = Pattern.compile(rule.getSearchString());
                String replaceString = rule.getReplaceString();
                replaceString = replaceString == null ? "" : replaceString;
                Matcher matcher = pattern.matcher(domainObjectName);
                domainObjectName = matcher.replaceAll(replaceString);
            }
            Interface serviceClass = new Interface(baseJavaPackage + ".service." + domainObjectName + "Service");
            serviceClass.setVisibility(JavaVisibility.PUBLIC);
            GeneratedJavaFile serviceFile = new GeneratedJavaFile(serviceClass, targetProject, context.getJavaFormatter());
            javaFiles.add(serviceFile);

            TopLevelClass serviceImplClass = new TopLevelClass(baseJavaPackage + ".service.impl." + domainObjectName + "ServiceImpl");
            serviceImplClass.setVisibility(JavaVisibility.PUBLIC);
            serviceImplClass.addImportedType(serviceClass.getType());
            serviceImplClass.addSuperInterface(serviceClass.getType());
            GeneratedJavaFile serviceImplFile = new GeneratedJavaFile(serviceImplClass, targetProject, context.getJavaFormatter());
            javaFiles.add(serviceImplFile);
        }
        return javaFiles;
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        comment = Boolean.parseBoolean(context.getProperty("useMapperCommentGenerator"));
        if (comment) {
            CommentGeneratorConfiguration commentCfg = new CommentGeneratorConfiguration();
            commentCfg.setConfigurationType(VyseCommentGenerator.class.getCanonicalName());
            context.setCommentGeneratorConfiguration(commentCfg);
        }
        targetProject = getPropertyAsString(super.context.getProperties(), "targetProject");
        baseJavaPackage = getPropertyAsString(super.context.getProperties(), "baseJavaPackage");
        //支持oracle获取注释#114
        context.getJdbcConnectionConfiguration().addProperty("remarksReporting", "true");
        //支持mysql获取注释
        context.getJdbcConnectionConfiguration().addProperty("useInformationSchema", "true");

        context.getCommentGeneratorConfiguration().addProperty("dateFormat", "yyyy-MM-dd");

    }

    private String getByPrimaryKeyMethodName(StringBuffer buffer,IntrospectedTable introspectedTable){
        List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();
        for (int i = 0; i < columns.size(); i++) {
            IntrospectedColumn column = columns.get(i);
            if (i > 0) {
                buffer.append("And");
            }
            String property = column.getJavaProperty();
            property = property.substring(0, 1).toUpperCase() + property.substring(1);
            buffer.append(property);
        }
        return buffer.toString();
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        lombok = getPropertyAsBoolean(properties, "lombok", false);
        lombokBuilder = getPropertyAsBoolean(properties, "lombokBuilder", false);
        swagger = getPropertyAsBoolean(properties, "swagger", false);
        service = getPropertyAsBoolean(properties, "service", false);
        super.context.getCommentGeneratorConfiguration().addProperty("lombok", getPropertyAsString(properties, "lombok"));
        super.context.getCommentGeneratorConfiguration().addProperty("swagger", getPropertyAsString(properties, "swagger"));
    }

}
