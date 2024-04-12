package com.hoby.config;

import cn.hutool.core.date.DatePattern;
import com.hoby.jackson.BigDecimalDeSerializer;
import com.hoby.jackson.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.TimeZone;

/**
 * jackson序列化配置
 *
 * @author hoby
 * @since 2023-08-20
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {

        // set locale and time zone
        builder.locale(Locale.CHINA);
        builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        // LocalDateTime、LocalDate、LocalTime
        builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER));
        builder.serializerByType(LocalDate.class, new LocalDateSerializer(DatePattern.NORM_DATE_FORMATTER));
        builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DatePattern.NORM_TIME_FORMATTER));
        builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER));
        builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DatePattern.NORM_DATE_FORMATTER));
        builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DatePattern.NORM_TIME_FORMATTER));
        // BigDecimal
        builder.serializerByType(BigDecimal.class, new BigDecimalSerializer());
        builder.deserializerByType(BigDecimal.class, new BigDecimalDeSerializer());

        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
        // Include.Include.ALWAYS 默认
        // Include.NON_DEFAULT 属性为默认值不序列化
        // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的
        // Include.NON_NULL 属性为NULL 不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 允许出现特殊字符和转义符
//        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        // 允许出现单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

}
