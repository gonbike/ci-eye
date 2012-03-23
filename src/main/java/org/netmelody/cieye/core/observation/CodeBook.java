package org.netmelody.cieye.core.observation;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializer;

public final class CodeBook {

    private final SimpleDateFormat dateFormat;
    private final ImmutableMap<Class<?>, JsonDeserializer<?>> deserialisers;
    private final Function<String, String> munger;

    public CodeBook() {
        this(new SimpleDateFormat());
    }

    public CodeBook(SimpleDateFormat dateFormat) {
        this(dateFormat, Functions.<String>identity(), ImmutableMap.<Class<?>, JsonDeserializer<?>>of());
    }

    private CodeBook(SimpleDateFormat dateFormat, Function<String, String> munger, ImmutableMap<Class<?>, JsonDeserializer<?>> deserialisers) {
        this.dateFormat = dateFormat;
        this.deserialisers = deserialisers;
        this.munger = munger;
    }

    public <T> CodeBook withJsonDeserializerFor(Class<T> type, JsonDeserializer<T> deserialiser) {
        return new CodeBook(this.dateFormat, this.munger, extend(this.deserialisers, type, deserialiser));
    }

    public CodeBook withRawContentMunger(Function<String, String> munger) {
        return new CodeBook(this.dateFormat, Functions.compose(munger, this.munger), this.deserialisers);
    }

    public SimpleDateFormat dateFormat() {
        return dateFormat;
    }

    public Function<String, String> contentMunger() {
        return munger;
    }

    public Map<Class<?>, JsonDeserializer<?>> deserialisers() {
        return this.deserialisers;
    }

    private static <X, Y> ImmutableMap<X, Y> extend(Map<X, Y> map, X key, Y value) {
        return ImmutableMap.<X, Y>builder().putAll(map).put(key, value).build();
    }
}