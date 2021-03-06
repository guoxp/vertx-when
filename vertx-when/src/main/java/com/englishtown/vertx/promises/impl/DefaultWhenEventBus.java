package com.englishtown.vertx.promises.impl;

import com.englishtown.promises.Promise;
import com.englishtown.promises.When;
import com.englishtown.vertx.promises.PromiseAdapter;
import com.englishtown.vertx.promises.WhenEventBus;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import javax.inject.Inject;

/**
 * Default implementation of {@link WhenEventBus}
 */
public class DefaultWhenEventBus implements WhenEventBus {

    private final EventBus eventBus;
    private final PromiseAdapter adapter;

    /**
     * Takes the event bus off the provided vert.x instance
     *
     * @param vertx the vertx instance with the event bus to wrap
     */
    @Inject
    public DefaultWhenEventBus(Vertx vertx, PromiseAdapter adapter) {
        this(vertx.eventBus(), adapter);
    }

    /**
     * Takes the event bus off the provided vert.x instance
     *
     * @param vertx the vertx instance with the event bus to wrap
     */
    public DefaultWhenEventBus(Vertx vertx, When when) {
        this(vertx.eventBus(), when);
    }

    /**
     * Directly provide the event bus instance to use
     *
     * @param eventBus the event bus instance to wrap
     */
    public DefaultWhenEventBus(EventBus eventBus, PromiseAdapter adapter) {
        this.eventBus = eventBus;
        this.adapter = adapter;
    }

    /**
     * Directly provide the event bus instance to use
     *
     * @param eventBus the event bus instance to wrap
     */
    public DefaultWhenEventBus(EventBus eventBus, When when) {
        this(eventBus, new DefaultPromiseAdapter(when));
    }

    /**
     * Returns the underlying event bus being wrapped over
     *
     * @return
     */
    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Close the EventBus and release all resources.
     *
     * @return A promise for the event bus close completion
     */
    @Override
    public Promise<Void> close() {
        return adapter.toPromise(eventBus::close);
    }

    /**
     * Send a message
     *
     * @param address The address to send it to
     * @param message The message, may be {@code null}
     * @return A promise for a reply
     */
    @Override
    public <T> Promise<Message<T>> send(String address, Object message) {
        return adapter.toPromise(handler -> eventBus.send(address, message, handler));
    }

    /**
     * Send a message
     *
     * @param address The address to send it to
     * @param message The message, may be {@code null}
     * @param options
     * @return A promise for a reply
     */
    @Override
    public <T> Promise<Message<T>> send(String address, Object message, DeliveryOptions options) {
        return adapter.toPromise(handler -> eventBus.send(address, message, options, handler));
    }

}
