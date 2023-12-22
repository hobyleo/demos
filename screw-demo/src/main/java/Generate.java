import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 使用 Screw 生成数据库文档
 *
 * @author hoby
 * @since 2023-12-22
 */
public class Generate {

    public static void main(String[] args) {
        // 输出目录
        String outputDir = "C:\\Users\\hoby\\Documents";
        // 文档版本
        String version = "1.0.0";
        // 文档描述
        String description = "db_doc_by_screw";

        // 数据库连接信息
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 需要忽略的表名
        List<String> ignoreTableName = Arrays.asList("undo_log");
        // 需要忽略的表前缀
        List<String> ignoreTablePrefix = Arrays.asList("test_");
        // 需要忽略的表后缀
        List<String> ignoreTableSuffix = Arrays.asList("_test");

        buildDBDoc(outputDir, version, description, dataSource, ignoreTableName, ignoreTablePrefix, ignoreTableSuffix);
    }

    /**
     * 生成数据库文档
     *
     * @param outputDir 输出目录
     * @param version 文档版本号
     * @param description 文档描述
     * @param dataSource 数据源
     * @param ignoreTableName 忽略的表名
     * @param ignoreTablePrefix 忽略的表前缀
     * @param ignoreTableSuffix 忽略的表后缀
     */
    public static void buildDBDoc(String outputDir,
                                  String version, String description,
                                  DataSource dataSource,
                                  List<String> ignoreTableName,
                                  List<String> ignoreTablePrefix,
                                  List<String> ignoreTableSuffix) {

        EngineConfig engineConfig = EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir(outputDir)
                // 打开目录
                .openOutputDir(false)
                // 文件类型
                .fileType(EngineFileType.HTML)
                .produceType(EngineTemplateType.freemarker).build();

        // 生成文档配置，包括自定义版本号、描述等等
        Configuration configuration = Configuration.builder()
                .version(version)
                .description(description)
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(getProduceConfig(ignoreTableName, ignoreTablePrefix, ignoreTableSuffix))
                .build();

        // 执行生成
        new DocumentationExecute(configuration).execute();
    }

    /**
     * 配置想要生成以及忽略的数据表
     */
    private static ProcessConfig getProduceConfig(List<String> ignoreTableName,
                                                  List<String> ignoreTablePrefix,
                                                  List<String> ignoreTableSuffix) {
        return ProcessConfig.builder()
                // 根据名称指定表生成
                .designatedTableName(Collections.emptyList())
                // 根据表前缀生成
                .designatedTablePrefix(Collections.emptyList())
                // 根据表后缀生成
                .designatedTableSuffix(Collections.emptyList())
                // 忽略表
                .ignoreTableName(ignoreTableName)
                // 忽略表前缀
                .ignoreTablePrefix(ignoreTablePrefix)
                // 忽略表后缀
                .ignoreTableSuffix(ignoreTableSuffix)
                .build();
    }
}
