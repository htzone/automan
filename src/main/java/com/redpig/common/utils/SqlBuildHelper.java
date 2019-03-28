package com.redpig.common.utils;

import com.redpig.common.entity.BaseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hetao on 2018/6/25.
 */
public class SqlBuildHelper {
    public static final String DB_ORACLE = "oracle";
    public static final String DB_MYSQL = "mysql";

    public SqlBuildHelper() {
    }

    public static String createTable(Class<? extends BaseEntity> clazz, String dbType) {
        return "oracle".equals(dbType)?makeTableForOracle(clazz):("mysql".equals(dbType)?makeTableForMysql(clazz):"请指定数据库类型");
    }

    private static String makeTableForOracle(Class<? extends BaseEntity> clazz) {
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("create table ");
            String e = Utils.toDBName(clazz.getSimpleName());
            sb.append(e);
            sb.append(" (");
            List fields = Utils.getFields(clazz);
            int i = 0;
            Iterator var5 = fields.iterator();

            while(true) {
                while(true) {
                    while(true) {
                        Field field;
                        do {
                            do {
                                if(!var5.hasNext()) {
                                    sb.append(")\n");
                                    sb.append("tablespace BIGDATA\n");
                                    sb.append(" storage\n");
                                    sb.append("(\n");
                                    sb.append("initial 2M\n");
                                    sb.append("next 2M\n");
                                    sb.append("minextents 1\n");
                                    sb.append("maxextents unlimited\n");
                                    sb.append("pctincrease 0\n");
                                    sb.append(" );\n");
                                    sb.append("alter table ").append(e).append("\n");
                                    sb.append("add constraint ").append(e).append("_pk primary key (ID);");
                                    return sb.toString();
                                }

                                field = (Field)var5.next();
                            } while(Modifier.isStatic(field.getModifiers()));
                        } while(Modifier.isFinal(field.getModifiers()));

                        String typeName = field.getType().getName();
                        if(i > 0) {
                            sb.append(", \n");
                        }

                        if(!Integer.TYPE.getName().equals(typeName) && !Integer.class.getName().equals(typeName) && !Long.TYPE.getName().equals(typeName) && !Long.class.getName().equals(typeName) && !Short.TYPE.getName().equals(typeName) && !Short.class.getName().equals(typeName) && !Byte.TYPE.getName().equals(typeName) && !Byte.class.getName().equals(typeName) && !Boolean.TYPE.getName().equals(typeName) && !Boolean.class.getName().equals(typeName)) {
                            if(!Float.TYPE.getName().equals(typeName) && !Float.class.getName().equals(typeName) && !Double.TYPE.getName().equals(typeName) && !Double.class.getName().equals(typeName) && !BigDecimal.class.getName().equals(typeName)) {
                                if(String.class.getName().equals(typeName)) {
                                    sb.append(Utils.toDBName(field.getName())).append(" varchar2(255)");
                                    ++i;
                                } else if(Date.class.getName().equals(typeName)) {
                                    sb.append(Utils.toDBName(field.getName())).append(" date");
                                    ++i;
                                }
                            } else {
                                sb.append(Utils.toDBName(field.getName())).append(" number(16,4)");
                                ++i;
                            }
                        } else {
                            sb.append(Utils.toDBName(field.getName())).append(" number(16)");
                            ++i;
                        }
                    }
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            return sb.toString();
        }
    }

    private static String makeTableForMysql(Class<? extends BaseEntity> clazz) {
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("create table `");
            String e = Utils.toDBName(clazz.getSimpleName());
            sb.append(e);
            sb.append("` (");
            List fields = Utils.getFields(clazz);
            int i = 0;
            Iterator var5 = fields.iterator();

            while(true) {
                while(true) {
                    while(true) {
                        Field field;
                        do {
                            do {
                                if(!var5.hasNext()) {
                                    sb.append(",PRIMARY KEY (`id`)) ENGINE=InnoDB;");
                                    return sb.toString();
                                }

                                field = (Field)var5.next();
                            } while(Modifier.isStatic(field.getModifiers()));
                        } while(Modifier.isFinal(field.getModifiers()));

                        String typeName = field.getType().getName();
                        if(i > 0) {
                            sb.append(", \n");
                        }

                        if(!Integer.TYPE.getName().equals(typeName) && !Integer.class.getName().equals(typeName) && !Long.TYPE.getName().equals(typeName) && !Long.class.getName().equals(typeName) && !Short.TYPE.getName().equals(typeName) && !Short.class.getName().equals(typeName) && !Byte.TYPE.getName().equals(typeName) && !Byte.class.getName().equals(typeName) && !Boolean.TYPE.getName().equals(typeName) && !Boolean.class.getName().equals(typeName)) {
                            if(!Float.TYPE.getName().equals(typeName) && !Float.class.getName().equals(typeName) && !Double.TYPE.getName().equals(typeName) && !Double.class.getName().equals(typeName) && !BigDecimal.class.getName().equals(typeName)) {
                                if(String.class.getName().equals(typeName)) {
                                    sb.append("`").append(Utils.toDBName(field.getName())).append("` varchar(255) NULL");
                                    ++i;
                                } else if(Date.class.getName().equals(typeName)) {
                                    sb.append("`").append(Utils.toDBName(field.getName())).append("` datetime NULL");
                                    ++i;
                                }
                            } else {
                                sb.append("`").append(Utils.toDBName(field.getName())).append("` decimal(16,4) NULL");
                                ++i;
                            }
                        } else {
                            sb.append("`").append(Utils.toDBName(field.getName())).append("` bigint(16) NULL");
                            ++i;
                        }
                    }
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            return sb.toString();
        }
    }
}
