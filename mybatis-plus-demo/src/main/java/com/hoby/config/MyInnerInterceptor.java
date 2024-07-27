package com.hoby.config;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.sun.istack.internal.NotNull;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
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
        // 默认情况下，直接加到最外层查询的 where 后面
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        if (select.toString().contains("ROWNUM")) {
            // oracle 分页的情况，需要找到最内层的查询，然后加到 where 后面
            plainSelect = getInnerSelect(plainSelect);
        }
        // 给查询添加条件
        GreaterThanEquals greaterThanEquals = getGreaterThanEquals();
        Expression where = plainSelect.getWhere();
        plainSelect.setWhere(where == null ? greaterThanEquals : new AndExpression(where, greaterThanEquals));
    }

    private static PlainSelect getInnerSelect(PlainSelect select) {
        if (select.getFromItem() instanceof SubSelect) {
            PlainSelect fromSelect = (PlainSelect) ((SubSelect) select.getFromItem()).getSelectBody();
            return getInnerSelect(fromSelect);
        }
        return select;
    }

    @NotNull
    private static GreaterThanEquals getGreaterThanEquals() {
        GreaterThanEquals greaterThanEquals = new GreaterThanEquals();
        greaterThanEquals.setLeftExpression(new Column("created_time"));
        greaterThanEquals.setRightExpression(new DateValue("'2024-07-26'"));
        return greaterThanEquals;
    }

}
