// Copyright 2017 Yahoo Inc.
// Licensed under the terms of the Apache license. Please see LICENSE.md file distributed with this work for terms.
package com.yahoo.bard.webservice.sql.aggregation;

import com.yahoo.bard.webservice.druid.model.aggregation.Aggregation;
import com.yahoo.bard.webservice.sql.ApiToFieldMapper;
import com.yahoo.bard.webservice.util.Utils;

import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;

import java.util.Set;

/**
 * All the default aggregation types supported for use with a sql backend.
 */
public enum DefaultSqlAggregationType implements SqlAggregationType {
    SUM(SqlStdOperatorTable.SUM, "longSum", "doubleSum"),
    MIN(SqlStdOperatorTable.MIN, "longMin", "doubleMin"),
    MAX(SqlStdOperatorTable.MAX, "longMax", "doubleMax");
    // todo avg

    private final Set<String> validDruidAggregations;
    private final SqlAggFunction sqlAggFunction;

    /**
     * Construct an DefaultSqlAggregationType with a keyword to look for in a
     * druid aggregation types, i.e. {"longSum", "doubleMin"}.
     *
     * @param sqlAggFunction  The aggregation function that should be performed.
     * @param aliases  The druid aggregation type.
     */
    DefaultSqlAggregationType(SqlAggFunction sqlAggFunction, String... aliases) {
        this.sqlAggFunction = sqlAggFunction;
        this.validDruidAggregations = Utils.asLinkedHashSet(aliases);
    }

    @Override
    public Set<String> getSupportedDruidAggregations() {
        return validDruidAggregations;
    }

    @Override
    public SqlAggregation getSqlAggregation(Aggregation aggregation, ApiToFieldMapper apiToFieldMapper) {
        return new SqlAggregation(apiToFieldMapper.apply(aggregation.getFieldName()), sqlAggFunction);
    }
}
