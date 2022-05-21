package dev.lucalewin.planer.validation;

import java.util.regex.Pattern;

public class DomainValidator extends Validator<String> {

    private static final Pattern URL_BASE_PATTERN = Pattern.compile("^([a-zA-Z0-9][a-zA-Z0-9\\-_]{0,62}\\.)+([a-zA-Z]{2,})$");

    @Override
    public boolean isValid(String url) {
        return URL_BASE_PATTERN.matcher(url).find();
    }
}
