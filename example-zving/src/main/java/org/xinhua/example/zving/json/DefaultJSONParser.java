
package org.xinhua.example.zving.json;


import java.util.Map;

import static org.xinhua.example.zving.json.JSONScanner.EOI;


public class DefaultJSONParser extends AbstractJSONParser {
	static {
		int features = 0;
		features |= Feature.AutoCloseSource.getMask();
		features |= Feature.InternFieldNames.getMask();
		// features |= Feature.UseBigDecimal.getMask();
		features |= Feature.AllowUnQuotedFieldNames.getMask();
		features |= Feature.AllowSingleQuotes.getMask();
		features |= Feature.AllowArbitraryCommas.getMask();
		features |= Feature.SortFeidFastMatch.getMask();
		features |= Feature.IgnoreNotMatch.getMask();
		DEFAULT_PARSER_FEATURE = features;
	}
	public static final int DEFAULT_PARSER_FEATURE;

	protected final JSONLexer lexer;
	protected final Object input;
	protected final SymbolTable symbolTable;

	public DefaultJSONParser(String input) {
		this(input, ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE);
	}

	public DefaultJSONParser(final String input, final ParserConfig config, int features) {
		this(input, new JSONScanner(input, features), config);
	}

	public DefaultJSONParser(final Object input, final JSONLexer lexer, final ParserConfig config) {
		this.input = input;
		this.lexer = lexer;
		symbolTable = config.getSymbolTable();

		lexer.nextToken(JSONToken.LBRACE); // prime the pump
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	@Override
	public JSONLexer getLexer() {
		return lexer;
	}

	public String getInput() {
		if (input instanceof char[]) {
			return new String((char[]) input);
		}
		return input.toString();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final void parseObject(final Map object) {
		JSONScanner lexer = (JSONScanner) this.lexer;
		if (lexer.token() != JSONToken.LBRACE) {
			throw new JSONException("syntax error, expect {, actual " + lexer.token());
		}

		for (;;) {
			lexer.skipWhitespace();
			char ch = lexer.getCurrent();
			if (isEnabled(Feature.AllowArbitraryCommas)) {
				while (ch == ',') {
					lexer.incrementBufferPosition();
					lexer.skipWhitespace();
					ch = lexer.getCurrent();
				}
			}

			String key;
			if (ch == '"') {
				key = lexer.scanSymbol(symbolTable, '"');
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':') {
					throw new JSONException("expect ':' at " + lexer.pos() + ", name " + key);
				}
			} else if (ch == '}') {
				lexer.incrementBufferPosition();
				lexer.resetStringPosition();
				lexer.nextToken();
				return;
			} else if (ch == '\'') {
				if (!isEnabled(Feature.AllowSingleQuotes)) {
					throw new JSONException("syntax error");
				}

				key = lexer.scanSymbol(symbolTable, '\'');
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':') {
					throw new JSONException("expect ':' at " + lexer.pos());
				}
			} else if (ch == EOI) {
				throw new JSONException("syntax error");
			} else if (ch == ',') {
				throw new JSONException("syntax error");
			} else {
				if (!isEnabled(Feature.AllowUnQuotedFieldNames)) {
					throw new JSONException("syntax error");
				}

				key = lexer.scanSymbolUnQuoted(symbolTable);
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':') {
					throw new JSONException("expect ':' at " + lexer.pos() + ", actual " + ch);
				}
			}

			lexer.incrementBufferPosition();
			lexer.skipWhitespace();
			ch = lexer.getCurrent();

			lexer.resetStringPosition();

			Object value;
			if (ch == '"') {
				lexer.scanString();
				String strValue = lexer.stringVal();
				value = strValue;

				if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
					JSONScanner iso8601Lexer = new JSONScanner(strValue);
					if (iso8601Lexer.scanISO8601DateIfMatch()) {
						value = iso8601Lexer.getCalendar().getTime();
					}
				}

				object.put(key, value);
			} else if (ch >= '0' && ch <= '9' || ch == '-') {
				lexer.scanNumber();
				if (lexer.token() == JSONToken.LITERAL_INT) {
					value = lexer.integerValue();
				} else {
					if (isEnabled(Feature.UseBigDecimal)) {
						value = lexer.decimalValue();
					} else {
						value = lexer.doubleValue();
					}
				}

				object.put(key, value);
			} else if (ch == '[') { // 减少潜套，兼容android
				lexer.nextToken();
				JSONArray list = new JSONArray();
				parseArray(list);
				value = list;
				object.put(key, value);

				if (lexer.token() == JSONToken.RBRACE) {
					lexer.nextToken();
					return;
				} else if (lexer.token() == JSONToken.COMMA) {
					continue;
				} else {
					throw new JSONException("syntax error");
				}
			} else if (ch == '{') { // 减少潜套，兼容android
				lexer.nextToken();
				JSONObject obj = new JSONObject();
				parseObject(obj);
				object.put(key, JSON.tryReverse(obj));

				if (lexer.token() == JSONToken.RBRACE) {
					lexer.nextToken();
					return;
				} else if (lexer.token() == JSONToken.COMMA) {
					continue;
				} else {
					throw new JSONException("syntax error");
				}
			} else {
				lexer.nextToken();
				value = parse();
				object.put(key, value);

				if (lexer.token() == JSONToken.RBRACE) {
					lexer.nextToken();
					return;
				} else if (lexer.token() == JSONToken.COMMA) {
					continue;
				} else {
					throw new JSONException("syntax error, position at " + lexer.pos() + ", name " + key);
				}
			}

			lexer.skipWhitespace();
			ch = lexer.getCurrent();
			if (ch == ',') {
				lexer.incrementBufferPosition();
				continue;
			} else if (ch == '}') {
				lexer.incrementBufferPosition();
				lexer.resetStringPosition();
				lexer.nextToken();
				return;
			} else {
				throw new JSONException("syntax error, position at " + lexer.pos() + ", name " + key);
			}

		}
	}

}
