package freed.uc2rest;

@FunctionalInterface
public interface ApiServiceCallback<T> {
    void onResponse(T response);
    default void onFailure(Throwable cause) {}
}
