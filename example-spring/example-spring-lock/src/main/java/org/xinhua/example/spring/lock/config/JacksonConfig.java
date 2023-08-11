package org.xinhua.example.spring.lock.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.createXmlMapper(false).build();
    }

    @Order
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.modulesToInstall(javaTimeModule())
                .simpleDateFormat(DATETIME_PATTERN)
                .featuresToEnable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToDisable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                .serializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private JavaTimeModule javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(new LocalDateSerializer(DATETIME_FORMATTER))
                .addSerializer(new LocalTimeSerializer(DATETIME_FORMATTER))
                .addSerializer(new LocalDateTimeSerializer(DATETIME_FORMATTER))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DATETIME_FORMATTER))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DATETIME_FORMATTER))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMATTER));
        return module;
    }

}
