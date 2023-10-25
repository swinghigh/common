package com.zhongzy.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ShopCodeGenerator {

    /**
     * 是否强制带上注解
     */
    private static boolean enableTableFieldAnnotation = true;
    /**
     * 生成的注解带上IdType类型
     */
    private static IdType tableIdType = null;
    /**
     * 是否去掉生成实体的属性名前缀
     */
    private static String[] fieldPrefix = null;
    /**
     * 生成的Service 接口类名是否以I开头
     * <p>默认是以I开头</p>
     * <p>user表 -> IUserService, UserServiceImpl</p>
     */
    private static boolean serviceClassNameStartWithI = true;
    //TODO 根据实际情况修改
    //微服务名称
    private static String projectName = "shop";
    //数据库   ip及端口   
    private static String dbIpAndPort = "ip:port";
    //数据库   名称
    private static String dbName = "saas_shop";
    //数据库   登录用户名
    private static String dbUserName = "dbUserName";
    //数据库   登录用户密码
    private static String dbPassword = "dbPassword!";
    //微服务   数据库 驱动名称
    private static String dbDriverName = "com.mysql.jdbc.Driver";
    //微服务   数据库 驱动连接的URL
    private static String dbUrl = "jdbc:mysql://"+dbIpAndPort+"/"+dbName+"?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";

    //微服务   输出文件根路径
    private static String outputDir = "F:\\aaa\\";
    //微服务   路径基准
    private static String projectDir = "saas-shop";


     

    public static void main(String[] args) {
        String apiPackageName="com.saas.api."+projectName;
        String controllerAndServicePackageName="com.saas."+projectName;
        //模块
        String module="order";
        //待生成模板数据的表
        String tableNames="order_refund_fee";
        genEntityAndMapper(apiPackageName, module, tableNames); //生成模型和Mappler
        genService(controllerAndServicePackageName, module, tableNames);//生成service
        genPageParameter(apiPackageName, module, tableNames); //分页参数
        genAddParameter(apiPackageName, module, tableNames);//生成增加参数
        genUpdateParameter(apiPackageName, module, tableNames);//生成修改参数
        genNewContrller(controllerAndServicePackageName, module, tableNames);//生成Contrller
    }


    private static void genNewContrller(String packageName, String module, String... tableNames) {
        String newOutDir = outputDir + "saas-micro-service\\"+projectDir+"\\src\\main\\java";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(dbUserName)
                .setPassword(dbPassword)
                .setDriverName(dbDriverName);
        //类型转换
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                if (t.contains("tinyint")) {
                    return DbColumnType.BYTE;
                }
                if (t.contains("datetime")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("timestamp")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("date")) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });

        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);//覆盖
        gc.setActiveRecord(true);// 开启 activeRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setSwagger2(true);
        gc.setOpen(false);
        gc.setAuthor("ROOT");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");

        //        自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        // tableFillList.add(new TableFill("writer", FieldFill.INSERT_UPDATE));

        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setTablePrefix("tb_", "sys_")
                .setEntityLombokModel(false)
                // .setDbColumnUnderline(true) 改为如下 2 个配置
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
//                .setEntityTableFieldAnnotationEnable(enableTableFieldAnnotation)
                //test_id -> id, test_type -> type
                .setFieldPrefix(fieldPrefix)
                //修改替换成你需要的表名，多个表名传数组
                .setTableFillList(tableFillList)
                .setCapitalMode(true)
                .setEntityBuilderModel(true)
                .setEntitySerialVersionUID(true)
                .setEntityColumnConstant(true)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setInclude(tableNames)
                .setSuperServiceClass("com."+projectName+".core.supper.SuperService")
                .setSuperServiceImplClass("com."+projectName+".core.supper.SuperServiceImpl");

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setEntity(null);
        templateConfig.setMapper(null);
        templateConfig.setXml(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setService(null);

        // 包配置
        PackageConfig packageInfo = new PackageConfig();
        packageInfo.setParent("com.saas");
        packageInfo.setEntity("api."+projectName+".entity." + module);
        packageInfo.setMapper("api."+projectName+".mappers." + module);
        packageInfo.setService(projectName+".service." + module);
        packageInfo.setServiceImpl(projectName+".service.impl." + module);
        packageInfo.setController(projectName+".controller");

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/controller.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return newOutDir + "/" + packageName.replace(".", "/") + "/" + "controller/" + module + "/" + tableInfo.getEntityName() + "Controller" + StringPool.DOT_JAVA;
            }
        });
        cfg.setFileOutConfigList(focList);

        new AutoGenerator()
                .setGlobalConfig(gc)
                .setCfg(cfg)
                .setDataSource(dataSourceConfig)
                .setPackageInfo(packageInfo)
                .setTemplate(templateConfig)
                .setTemplateEngine(new VelocityTemplateEngine())
                .setStrategy(strategyConfig)
                .execute();
    }


    private static void genService(String packageName, String module, String... tableNames) {
        String newOutDir = outputDir + "saas-micro-service\\"+projectDir+"\\src\\main\\java";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(dbUserName)
                .setPassword(dbPassword)
                .setDriverName(dbDriverName);
        //类型转换
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                if (t.contains("tinyint")) {
                    return DbColumnType.BYTE;
                }
                if (t.contains("datetime")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("timestamp")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("date")) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });

        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(newOutDir);
        gc.setFileOverride(true);//覆盖
        gc.setActiveRecord(true);// 开启 activeRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setSwagger2(true);
        gc.setOpen(false);
        gc.setAuthor("ROOT");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");

        //        自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        // tableFillList.add(new TableFill("writer", FieldFill.INSERT_UPDATE));

        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setTablePrefix("tb_", "sys_")
                .setEntityLombokModel(false)
                // .setDbColumnUnderline(true) 改为如下 2 个配置
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
//                .setEntityTableFieldAnnotationEnable(enableTableFieldAnnotation)
                //test_id -> id, test_type -> type
                .setFieldPrefix(fieldPrefix)
                //修改替换成你需要的表名，多个表名传数组
                .setTableFillList(tableFillList)
                .setCapitalMode(true)
                .setEntityBuilderModel(true)
                .setEntitySerialVersionUID(true)
                .setEntityColumnConstant(true)
                .setEntityLombokModel(true)
                .setInclude(tableNames)
                .setSuperServiceClass("com.saas.common.supper.SuperService")
                .setSuperServiceImplClass("com.saas.common.supper.SuperServiceImpl");

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setEntity(null);
        templateConfig.setMapper(null);
        templateConfig.setXml(null);

        // 包配置

        PackageConfig packageInfo = new PackageConfig();
        packageInfo.setParent("com.saas");
        packageInfo.setEntity("api."+projectName+".entity." + module);
        packageInfo.setMapper("api."+projectName+".mappers." + module);
        packageInfo.setService(projectName+".service." + module);
        packageInfo.setServiceImpl(projectName+".service." + module + ".impl");

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();
      /*  focList.add(new FileOutConfig("/templates/importListener.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return newOutDir + "/" + packageName.replace(".", "/") + "/" + "excel" + "/" + module + "/" + tableInfo.getEntityName() + "ImportListener" + StringPool.DOT_JAVA;
            }
        });*/
        cfg.setFileOutConfigList(focList);

        new AutoGenerator()
                .setGlobalConfig(gc)
                .setCfg(cfg)
                .setDataSource(dataSourceConfig)
                .setPackageInfo(packageInfo)
                .setTemplate(templateConfig)
                .setTemplateEngine(new VelocityTemplateEngine())
                .setStrategy(strategyConfig)
                .execute();
    }

    private static void genEntityAndMapper(String packageName, String module, String... tableNames) {
        String newOutDir = outputDir + "saas-service-api\\"+projectDir+"-api\\src\\main\\java";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(dbUserName)
                .setPassword(dbPassword)
                .setDriverName(dbDriverName);
        //类型转换
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                if (t.contains("tinyint")) {
                    return DbColumnType.INTEGER;
                }
                if (t.contains("sgroupint")) {
                    return DbColumnType.INTEGER;
                }
                if (t.contains("datetime")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("timestamp")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("date")) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });

        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(newOutDir);
        gc.setFileOverride(true);//覆盖
        gc.setActiveRecord(true);// 开启 activeRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(false);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setSwagger2(true);
        gc.setOpen(false);
        gc.setAuthor("ROOT");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");

        //        自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        // tableFillList.add(new TableFill("writer", FieldFill.INSERT_UPDATE));

        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setTablePrefix("tb_", "sys_")
                .setEntityLombokModel(false)
                // .setDbColumnUnderline(true) 改为如下 2 个配置
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
//                .setEntityTableFieldAnnotationEnable(enableTableFieldAnnotation)
                //test_id -> id, test_type -> type
                .setFieldPrefix(fieldPrefix)
                //修改替换成你需要的表名，多个表名传数组
                .setTableFillList(tableFillList)
                .setCapitalMode(true)
                .setEntityBuilderModel(true)
                .setEntitySerialVersionUID(true)
                .setLogicDeleteFieldName("delete_flag")
                .setEntityColumnConstant(true)
                .setEntityLombokModel(true)
                .setInclude(tableNames)
                .setSuperEntityClass("com.saas.common.supper.SuperEntity")
                .setSuperMapperClass("com.saas.common.supper.SuperMapper");

        //排除的模板
        /*Set<String> excludeTemplateSet = new HashSet<>();
        excludeTemplateSet.add("serviceImpl.java.ftl");
        excludeTemplateSet.add("service.java.ftl");
        excludeTemplateSet.add("controller.java.ftl");*/

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();
       /* focList.add(new FileOutConfig("/templates/entityExcel.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return newOutDir + "/" + packageName.replace(".", "/") + "/" + "excel" + "/" + module + "/" + tableInfo.getEntityName() + "Excel" + StringPool.DOT_JAVA;
            }
        });*/

        focList.add(new FileOutConfig("/templates/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return newOutDir + "/" + packageName.replace(".", "/") + "/" + "entity" + "/" + module + "/" + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });

        cfg.setFileOutConfigList(focList);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);

        new AutoGenerator().setGlobalConfig(gc)
                .setCfg(cfg)
                .setDataSource(dataSourceConfig)
                .setTemplate(templateConfig)
                .setTemplateEngine(new VelocityTemplateEngine())
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setEntity("entity" + StringPool.DOT + module)
                                .setMapper("mappers" + StringPool.DOT + module)
                                .setXml("mappers" + StringPool.DOT + module)
                ).execute();
    }

    /**
     * 生成Page parameter
     *
     * @param packageName
     * @param tableNames
     */
    private static void genPageParameter(String packageName, String module, String... tableNames) {
        String newOutDir = outputDir + "saas-service-api\\"+projectDir+"-api\\src\\main\\java";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(dbUserName)
                .setPassword(dbPassword)
                .setDriverName(dbDriverName);
        //类型转换
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                if (t.contains("tinyint")) {
                    return DbColumnType.INTEGER;
                }
                if (t.contains("sgroupint")) {
                    return DbColumnType.INTEGER;
                }
                if (t.contains("datetime")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("timestamp")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("date")) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });

        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(newOutDir);
        gc.setFileOverride(true);//覆盖
        gc.setActiveRecord(true);// 开启 activeRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(false);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setSwagger2(true);
        gc.setOpen(false);
        gc.setAuthor("ROOT");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");

        //        自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        // tableFillList.add(new TableFill("writer", FieldFill.INSERT_UPDATE));

        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setTablePrefix("tb_", "sys_")
                .setEntityLombokModel(false)
                // .setDbColumnUnderline(true) 改为如下 2 个配置
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
//                .setEntityTableFieldAnnotationEnable(enableTableFieldAnnotation)
                //test_id -> id, test_type -> type
                .setFieldPrefix(fieldPrefix)
                //修改替换成你需要的表名，多个表名传数组
                .setTableFillList(tableFillList)
                .setCapitalMode(true)
                .setEntityBuilderModel(true)
                .setEntitySerialVersionUID(true)
                .setEntityColumnConstant(true)
                .setEntityLombokModel(true)
                .setInclude(tableNames)
                .setSuperEntityClass("com.saas.common.supper.SuperEntity")
                .setSuperMapperClass("com.saas.common.supper.SuperMapper");

        //排除的模板
        /*Set<String> excludeTemplateSet = new HashSet<>();
        excludeTemplateSet.add("serviceImpl.java.ftl");
        excludeTemplateSet.add("service.java.ftl");
        excludeTemplateSet.add("controller.java.ftl");*/

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/entityPageParameter.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return newOutDir + "/" + packageName.replace(".", "/") + "/" + "params" + "/" + module + "/" + tableInfo.getEntityName() + "PageParam" + StringPool.DOT_JAVA;
            }
        });

        cfg.setFileOutConfigList(focList);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setEntity(null);
        templateConfig.setMapper(null);
        templateConfig.setXml(null);
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);

        new AutoGenerator().setGlobalConfig(gc)
                .setCfg(cfg)
                .setDataSource(dataSourceConfig)
                .setTemplate(templateConfig)
                .setTemplateEngine(new VelocityTemplateEngine())
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setEntity("entity" + StringPool.DOT + module)
                                .setMapper("mappers" + StringPool.DOT + module)
                                .setXml("mappers" + StringPool.DOT + module)
                ).execute();
    }

    /**
     * 生成AddParameter
     *
     * @param packageName
     * @param tableNames
     */
    private static void genAddParameter(String packageName, String module, String... tableNames) {
        String newOutDir = outputDir + "saas-service-api\\"+projectDir+"-api\\src\\main\\java";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(dbUserName)
                .setPassword(dbPassword)
                .setDriverName(dbDriverName);
        //类型转换
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                if (t.contains("tinyint")) {
                    return DbColumnType.INTEGER;
                }
                if (t.contains("sgroupint")) {
                    return DbColumnType.INTEGER;
                }
                if (t.contains("datetime")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("timestamp")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("date")) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });

        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(newOutDir);
        gc.setFileOverride(true);//覆盖
        gc.setActiveRecord(true);// 开启 activeRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(false);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setSwagger2(true);
        gc.setOpen(false);
        gc.setAuthor("ROOT");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");

        //        自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        // tableFillList.add(new TableFill("writer", FieldFill.INSERT_UPDATE));

        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setTablePrefix("tb_", "sys_")
                .setEntityLombokModel(false)
                // .setDbColumnUnderline(true) 改为如下 2 个配置
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
//                .setEntityTableFieldAnnotationEnable(enableTableFieldAnnotation)
                //test_id -> id, test_type -> type
                .setFieldPrefix(fieldPrefix)
                //修改替换成你需要的表名，多个表名传数组
                .setTableFillList(tableFillList)
                .setCapitalMode(true)
                .setEntityBuilderModel(true)
                .setEntitySerialVersionUID(true)
                .setEntityColumnConstant(true)
                .setEntityLombokModel(true)
                .setInclude(tableNames)
                .setSuperEntityClass("com.saas.common.supper.SuperEntity")
                .setSuperMapperClass("com.saas.common.supper.SuperMapper");

        //排除的模板
        /*Set<String> excludeTemplateSet = new HashSet<>();
        excludeTemplateSet.add("serviceImpl.java.ftl");
        excludeTemplateSet.add("service.java.ftl");
        excludeTemplateSet.add("controller.java.ftl");*/

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/entityAddParameter.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return newOutDir + "/" + packageName.replace(".", "/") + "/" + "params/" + module + "/" + tableInfo.getEntityName() + "AddParam" + StringPool.DOT_JAVA;
            }
        });

        cfg.setFileOutConfigList(focList);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setEntity(null);
        templateConfig.setMapper(null);
        templateConfig.setXml(null);
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);


        new AutoGenerator().setGlobalConfig(gc)
                .setCfg(cfg)
                .setDataSource(dataSourceConfig)
                .setTemplate(templateConfig)
                .setTemplateEngine(new VelocityTemplateEngine())
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setEntity("entity" + StringPool.DOT + module)
                                .setMapper("mappers" + StringPool.DOT + module)
                                .setXml("mappers" + StringPool.DOT + module)
                ).execute();
    }


    /**
     * 生成UpdateParameter
     *
     * @param packageName
     * @param tableNames
     */
    private static void genUpdateParameter(String packageName, String module, String... tableNames) {
        String newOutDir = outputDir + "saas-service-api\\"+projectDir+"-api\\src\\main\\java";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(dbUserName)
                .setPassword(dbPassword)
                .setDriverName(dbDriverName);
        //类型转换
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                if (t.contains("tinyint")) {
                    return DbColumnType.INTEGER;
                }
                if (t.contains("sgroupint")) {
                    return DbColumnType.INTEGER;
                }
                if (t.contains("datetime")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("timestamp")) {
                    return DbColumnType.DATE;
                }
                if (t.contains("date")) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });

        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(newOutDir);
        gc.setFileOverride(true);//覆盖
        gc.setActiveRecord(true);// 开启 activeRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(false);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setSwagger2(true);
        gc.setOpen(false);
        gc.setAuthor("ROOT");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");

        //        自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        // tableFillList.add(new TableFill("writer", FieldFill.INSERT_UPDATE));

        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setTablePrefix("tb_", "sys_")
                .setEntityLombokModel(false)
                // .setDbColumnUnderline(true) 改为如下 2 个配置
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
//                .setEntityTableFieldAnnotationEnable(enableTableFieldAnnotation)
                //test_id -> id, test_type -> type
                .setFieldPrefix(fieldPrefix)
                //修改替换成你需要的表名，多个表名传数组
                .setTableFillList(tableFillList)
                .setCapitalMode(true)
                .setEntityBuilderModel(true)
                .setEntitySerialVersionUID(true)
                .setEntityColumnConstant(true)
                .setEntityLombokModel(true)
                .setInclude(tableNames)
                .setSuperEntityClass("com.saas.common.supper.SuperEntity")
                .setSuperMapperClass("com.saas.common.supper.SuperMapper");

        //排除的模板
        /*Set<String> excludeTemplateSet = new HashSet<>();
        excludeTemplateSet.add("serviceImpl.java.ftl");
        excludeTemplateSet.add("service.java.ftl");
        excludeTemplateSet.add("controller.java.ftl");*/

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/entityUpdateParameter.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return newOutDir + "/" + packageName.replace(".", "/") + "/" + "params/" + module + "/" + tableInfo.getEntityName() + "UpdateParam" + StringPool.DOT_JAVA;
            }
        });

        cfg.setFileOutConfigList(focList);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setEntity(null);
        templateConfig.setMapper(null);
        templateConfig.setXml(null);
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);


        new AutoGenerator().setGlobalConfig(gc)
                .setCfg(cfg)
                .setDataSource(dataSourceConfig)
                .setTemplate(templateConfig)
                .setTemplateEngine(new VelocityTemplateEngine())
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setEntity("entity" + StringPool.DOT + module)
                                .setMapper("mappers" + StringPool.DOT + module)
                                .setXml("mappers" + StringPool.DOT + module)
                ).execute();
    }
}
