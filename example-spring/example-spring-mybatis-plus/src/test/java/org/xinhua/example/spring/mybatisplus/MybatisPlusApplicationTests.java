package org.xinhua.example.spring.mybatisplus;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xinhua.example.spring.mybatisplus.model.po.BaseEntity;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

@Configurable
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class MybatisPlusApplicationTests {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${user.dir}")
    private String projectDir;

    @Test
    public void generatorTest() {
        String resourcesDir = "src/main/resources";
        String javaDir = "src/main/java";
        String parent = "org.xinhua.example.spring.mybatisplus";
        String commonColumns = "id,extend_info,create_at,update_at,create_by,update_by,deleted,version";
        String tables = "mg_app,mg_video,mg_video_num,mg_user";

        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("lilong")
                            .outputDir(projectDir + File.separator + javaDir)
                            .disableOpenDir()
                            .enableSwagger()
                            .dateType(DateType.TIME_PACK)//LocalDateTime
                            .commentDate("yyyy-MM-dd HH:mm");
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent(parent)
                            .controller("controller")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .entity("model.po")
                            .pathInfo(Map.of(OutputFile.xml, projectDir + File.separator + resourcesDir + File.separator + "mapper"));
                })
                .strategyConfig(builder -> {
                    builder.enableSkipView()
                            .disableSqlFilter()
                            .addInclude(Arrays.asList(tables.split(",")));
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder()
                            //.enableFileOverride()
                            .enableLombok()
                            .enableChainModel()
                            .enableTableFieldAnnotation()
                            .disableSerialVersionUID()
                            .superClass(BaseEntity.class)
                            .addSuperEntityColumns(commonColumns)
                            .idType(IdType.ASSIGN_ID)
                            .logicDeleteColumnName("deleted")
                            .logicDeletePropertyName("deleted")
                            .versionColumnName("version")
                            .versionPropertyName("version")
                            .addTableFills(new Column("create_at", FieldFill.INSERT))
                            .addTableFills(new Column("create_by", FieldFill.INSERT))
                            .addTableFills(new Column("update_at", FieldFill.INSERT_UPDATE))
                            .addTableFills(new Column("update_by", FieldFill.INSERT_UPDATE));
                })
                .strategyConfig(builder -> {
                    builder.controllerBuilder()
                            //.enableFileOverride()
                            .enableRestStyle()
                            .enableHyphenStyle()
                            .formatFileName("%sController");
                })
                .strategyConfig(builder -> {
                    builder.serviceBuilder()
                            //.enableFileOverride()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");
                })
                .strategyConfig(builder -> {
                    builder.mapperBuilder()
                            //.enableFileOverride()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("%sMapper");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                // 执行
                .execute();

    }

}
