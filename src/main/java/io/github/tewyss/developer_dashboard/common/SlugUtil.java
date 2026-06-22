package io.github.tewyss.developer_dashboard.common;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtil {

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern NON_LATIN  = Pattern.compile("[^\\w-]");
    private static final Pattern DASHES     = Pattern.compile("-{2,}");
    private static final Pattern EDGE_DASH   = Pattern.compile("^-|-$");

    private SlugUtil() {
        // utility class, no instances
    }

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        String noWhitespace = WHITESPACE.matcher(input.trim()).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        slug = DASHES.matcher(slug).replaceAll("-");
        slug = EDGE_DASH.matcher(slug).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
