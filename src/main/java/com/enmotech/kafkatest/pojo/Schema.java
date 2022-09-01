package com.enmotech.kafkatest.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * com.enmotech.kafkatest.controller
 *
 * @author syf
 * @create 2022-08-22-10:14
 * @Description kafka-test
 */

@Getter
@Setter
public class Schema {
    @JSONField(ordinal = 1)
    private String type;
    //数组长度就是数据库数据字段数
    @JSONField(ordinal = 2)
    private Field[] fields;
    @JSONField(ordinal = 3)
    private boolean optional;
    @JSONField(ordinal = 4)
    private String name;

    public Schema(String type, Field[] fields, boolean optional, String name) {
        this.type = type;
        this.fields = fields;
        this.optional = optional;
        this.name = name;
    }

    public Schema() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }
}
