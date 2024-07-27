package com.hoby.config;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.sun.istack.internal.NotNull;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.sql.Connection;

/**
 * @author liaozh
 * @since 2024-07-27
 */
public class MyInnerInterceptor extends JsqlParserSupport implements InnerInterceptor {

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType commandType = ms.getSqlCommandType();

        if (commandType == SqlCommandType.SELECT || commandType == SqlCommandType.UPDATE || commandType == SqlCommandType.DELETE) {
            mpSh.mPBoundSql().sql(parserMulti(mpSh.mPBoundSql().sql(), null));
        }
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        EqualsTo equalsTo = getEqualsTo();
        Expression where = plainSelect.getWhere();
        plainSelect.setWhere(where == null ? equalsTo : new AndExpression(where, equalsTo));
    }

    @NotNull
    private static EqualsTo getEqualsTo() {
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column("is_active"));
        equalsTo.setRightExpression(new LongValue(1));
        return equalsTo;
    }

}
