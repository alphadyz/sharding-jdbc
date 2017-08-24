package com.dangdang.ddframe.rdb.sharding.parsing.parser.clause;

import com.dangdang.ddframe.rdb.sharding.parsing.lexer.LexerEngine;
import com.dangdang.ddframe.rdb.sharding.parsing.lexer.token.DefaultKeyword;
import com.dangdang.ddframe.rdb.sharding.parsing.lexer.token.Keyword;
import com.dangdang.ddframe.rdb.sharding.parsing.lexer.token.Symbol;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.exception.SQLParsingUnsupportedException;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.sql.dml.insert.InsertStatement;
import lombok.RequiredArgsConstructor;

/**
 * INTO从句解析器.
 *
 * @author zhangliang
 */
@RequiredArgsConstructor
public class IntoClauseParser implements SQLClauseParser {
    
    private final LexerEngine lexerEngine;
    
    private final TableClauseParser tableClauseParser;
    
    /**
     * 解析SET.
     *
     * @param insertStatement DML语句对象
     */
    public void parse(final InsertStatement insertStatement) {
        if (lexerEngine.equalAny(getUnsupportedKeywordsBeforeInto())) {
            throw new SQLParsingUnsupportedException(lexerEngine.getCurrentToken().getType());
        }
        lexerEngine.skipUntil(DefaultKeyword.INTO);
        lexerEngine.nextToken();
        tableClauseParser.parseSingleTable(insertStatement);
        skipBetweenTableAndValues(insertStatement);
    }
    
    protected Keyword[] getUnsupportedKeywordsBeforeInto() {
        return new Keyword[0];
    }
    
    private void skipBetweenTableAndValues(final InsertStatement insertStatement) {
        while (lexerEngine.skipIfEqual(getSkippedKeywordsBetweenTableAndValues())) {
            lexerEngine.nextToken();
            if (lexerEngine.equalAny(Symbol.LEFT_PAREN)) {
                lexerEngine.skipParentheses(insertStatement);
            }
        }
    }
    
    protected Keyword[] getSkippedKeywordsBetweenTableAndValues() {
        return new Keyword[0];
    }
}