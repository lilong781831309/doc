
package org.xinhua.example.zving.json;


public enum Feature {
	/**
	 * 
	 */
	AutoCloseSource,
	/**
	 * 
	 */
	AllowComment,
	/**
	 * 
	 */
	AllowUnQuotedFieldNames,
	/**
	 * 
	 */
	AllowSingleQuotes,
	/**
	 * 
	 */
	InternFieldNames,
	/**
	 * 
	 */
	AllowISO8601DateFormat,

	/**
	 * {"a":1,,,"b":2}
	 */
	AllowArbitraryCommas,

	/**
     * 
     */
	UseBigDecimal,

	/**
	 * @since 1.1.2
	 */
	IgnoreNotMatch,

	/**
     * 
     */
	SortFeidFastMatch,

	/**
     * 
     */
	DisableASM;

	private Feature() {
		mask = 1 << ordinal();
	}

	private final int mask;

	public final int getMask() {
		return mask;
	}

	public static boolean isEnabled(int features, Feature feature) {
		return (features & feature.getMask()) != 0;
	}
}
