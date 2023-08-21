package com.hba.hbacore.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public abstract class AbstractRepository {

  protected static final String PAGE_INDEX = "PageIndex";
  protected static final String PAGE_SIZE = "PageSize";
  private static final String RS = "rs";
  private static final String TOTAL_ROWS = "TotalRows";

  @Autowired private JdbcTemplate jdbcTemplate;

  protected static <T> T coalesce(ResultSet rs, T result) throws SQLException {
    if (rs.wasNull()) return null;
    return result;
  }

  protected static Instant getValueAsInstant(ResultSet rs, String alias) throws SQLException {
    LocalDateTime result = rs.getObject(alias, LocalDateTime.class);
    return result != null ? result.toInstant(ZoneOffset.UTC) : null;
  }

  protected static <T> T coalesce(ResultSet rs, T result, T defaultValue) throws SQLException {
    if (rs.wasNull()) return defaultValue;
    return result;
  }

  protected static LocalDate getValueAsLocalDate(ResultSet rs, String alias) throws SQLException {
    return rs.getDate(alias).toLocalDate();
  }

  protected static LocalDateTime getValueAsLocalDateTime(ResultSet rs, String alias)
      throws SQLException {
    return rs.getTimestamp(alias).toLocalDateTime();
  }

  protected void execProc(String spName) {
    verifyProcedureOrFunctionNameValid(spName);
    SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName(spName);
    call.execute();
  }

  protected void execProc(String spName, Map<String, ?> params) {
    verifyProcedureOrFunctionNameValid(spName);
    SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName(spName);
    call.execute(params);
  }

  protected ResultMap execProc(String spName, RowMapper<?> rowMapper, Map<String, ?> params) {
    verifyProcedureOrFunctionNameValid(spName);
    SimpleJdbcCall call =
        new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName(spName)
            .returningResultSet(RS, rowMapper);
    return new ResultMap(call.execute(params));
  }

  protected ResultMap execProc(String spName, RowMapper<?> rowMapper) {
    verifyProcedureOrFunctionNameValid(spName);
    SimpleJdbcCall call =
        new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName(spName)
            .returningResultSet(RS, rowMapper);
    return new ResultMap(call.execute());
  }

  protected <T> T execFunc(String fName, Map<String, ?> params, Class<T> resultClass) {
    verifyProcedureOrFunctionNameValid(fName);
    final SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName(fName);

    if (resultClass.equals(LocalDate.class)) {
      return resultClass.cast(call.executeFunction(java.sql.Date.class, params).toLocalDate());
    }

    return call.executeFunction(resultClass, params);
  }

  private void verifyProcedureOrFunctionNameValid(String name) {
    if (!StringUtils.hasText(name)) {
      throw new IllegalArgumentException(
          String.format("Procedure or function name '%s' must not be blank.", name));
    }
  }

  protected static class ResultMap {
    private final Map<String, ?> outputs;

    public ResultMap(Map<String, ?> outputs) {
      this.outputs = outputs;
    }

    public Object getResultSet() {
      return outputs.get(RS);
    }

    public Object getParam(final String param) {
      return outputs.get(param);
    }

    public int getTotalRows() {
      return (int) getParam(TOTAL_ROWS);
    }

    public <T> Page<T> getAsPage(Pageable pageable) {

      List<T> content = (List<T>) getResultSet();
      int totalRows = getTotalRows();

      return new PageImpl<>(content, pageable, totalRows);
    }
  }
}