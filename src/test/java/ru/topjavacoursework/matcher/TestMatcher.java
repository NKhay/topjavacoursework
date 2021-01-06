package ru.topjavacoursework.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import ru.topjavacoursework.util.JsonUtil;

abstract public class TestMatcher<T> extends BaseMatcher<String> {
    protected T expected;

    public TestMatcher(T expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object actual) {
        return compare(expected, (String) actual);
    }

    protected abstract boolean compare(T expected, String actual);

    @Override
    public void describeTo(Description description) {
        description.appendText(JsonUtil.writeValue(expected));
    }
}
