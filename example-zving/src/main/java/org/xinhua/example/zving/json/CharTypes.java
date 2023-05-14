
package org.xinhua.example.zving.json;


public final class CharTypes {

	public final static boolean[] firstIdentifierFlags = new boolean[256];

	static {
		for (char c = 0; c < firstIdentifierFlags.length; ++c) {
			if (c >= 'A' && c <= 'Z') {
				firstIdentifierFlags[c] = true;
			} else if (c >= 'a' && c <= 'z') {
				firstIdentifierFlags[c] = true;
			} else if (c == '_') {
				firstIdentifierFlags[c] = true;
			}
		}
	}

	public final static boolean[] identifierFlags = new boolean[256];

	static {
		for (char c = 0; c < identifierFlags.length; ++c) {
			if (c >= 'A' && c <= 'Z') {
				identifierFlags[c] = true;
			} else if (c >= 'a' && c <= 'z') {
				identifierFlags[c] = true;
			} else if (c == '_') {
				identifierFlags[c] = true;
			} else if (c >= '0' && c <= '9') {
				identifierFlags[c] = true;
			}
		}
	}
	public final static boolean[] specicalFlags_doubleQuotes = new boolean['\\' + 1];

	public final static boolean[] specicalFlags_singleQuotes = new boolean['\\' + 1];

	public final static char[] replaceChars = new char['\\' + 1];

	static {
		specicalFlags_doubleQuotes['\b'] = true;
		specicalFlags_doubleQuotes['\n'] = true;
		specicalFlags_doubleQuotes['\f'] = true;
		specicalFlags_doubleQuotes['\r'] = true;
		specicalFlags_doubleQuotes['\"'] = true;
		specicalFlags_doubleQuotes['\\'] = true;

		specicalFlags_singleQuotes['\b'] = true;
		specicalFlags_singleQuotes['\n'] = true;
		specicalFlags_singleQuotes['\f'] = true;
		specicalFlags_singleQuotes['\r'] = true;
		specicalFlags_singleQuotes['\''] = true;
		specicalFlags_singleQuotes['\\'] = true;

		replaceChars['\b'] = 'b';
		replaceChars['\n'] = 'n';
		replaceChars['\f'] = 'f';
		replaceChars['\r'] = 'r';
		replaceChars['\"'] = '"';
		replaceChars['\''] = '\'';
		replaceChars['\\'] = '\\';
		replaceChars['\t'] = 't';
	}

}
