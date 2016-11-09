package my.beelzik.mobile.scopemvptest.mvp.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.annotations.Beta;
import rx.exceptions.Exceptions;
import rx.internal.operators.BackpressureUtils;
import rx.subjects.Subject;


/**
 * Created by Andrey on 06.11.2016.
 */

public class CommandSubject<T> extends Subject<T, T> {


    /**
     * Creates a size-bounded replay subject.
     * <p>
     * In this setting, the {@code ReplaySubject} holds at most {@code size} items in its internal mBuffer and
     * discards the oldest item.
     * <p>
     * When observers subscribe to a terminated {@code ReplaySubject}, they are guaranteed to see at most
     * {@code size} {@code onNext} events followed by a termination event.
     * <p>
     * If an observer subscribes while the {@code ReplaySubject} is active, it will observe all items in the
     * mBuffer at that point in time and each item observed afterwards, even if the mBuffer evicts items due to
     * the size constraint in the mean time. In other words, once an Observer subscribes, it will receive items
     * without gaps in the sequence.
     *
     * @param <T>  the type of items observed and emitted by the Subject
     * @param size the maximum number of buffered items
     * @return the created subject
     */
    public static <T> CommandSubject<T> createWithSize(int size) {
        ReplayBuffer<T> buffer = new ReplaySizeBoundBuffer<T>(size);
        ReplayState<T> state = new ReplayState<T>(buffer);
        return new CommandSubject<T>(state);
    }

    /**
     * The state storing the history and the references.
     */
    final ReplayState<T> state;

    CommandSubject(ReplayState<T> state) {
        super(state);
        this.state = state;
    }

    public void clear() {
        state.buffer.clear();
    }

    public void remove(T value) {
        state.buffer.remove(value);
    }

    @Override
    public void onNext(T t) {
        state.onNext(t);
    }

    @Override
    public void onError(final Throwable e) {
        state.onError(e);
    }

    @Override
    public void onCompleted() {
        state.onCompleted();
    }

    /**
     * @return Returns the number of subscribers.
     */
    /* Support test. */int subscriberCount() {
        return state.get().length;
    }

    @Override
    public boolean hasObservers() {
        return state.get().length != 0;
    }

    /**
     * Check if the Subject has terminated with an exception.
     *
     * @return true if the subject has received a throwable through {@code onError}.
     */
    @Beta
    public boolean hasThrowable() {
        return state.isTerminated() && state.buffer.error() != null;
    }

    /**
     * Check if the Subject has terminated normally.
     *
     * @return true if the subject completed normally via {@code onCompleted}
     */
    @Beta
    public boolean hasCompleted() {
        return state.isTerminated() && state.buffer.error() == null;
    }

    /**
     * Returns the Throwable that terminated the Subject.
     *
     * @return the Throwable that terminated the Subject or {@code null} if the
     * subject hasn't terminated yet or it terminated normally.
     */
    @Beta
    public Throwable getThrowable() {
        if (state.isTerminated()) {
            return state.buffer.error();
        }
        return null;
    }

    /**
     * Returns the current number of items (non-terminal events) available for replay.
     *
     * @return the number of items available
     */
    @Beta
    public int size() {
        return state.buffer.size();
    }

    /**
     * @return true if the Subject holds at least one non-terminal event available for replay
     */
    @Beta
    public boolean hasAnyValue() {
        return !state.buffer.isEmpty();
    }

    @Beta
    public boolean hasValue() {
        return hasAnyValue();
    }

    /**
     * Returns a snapshot of the currently buffered non-terminal events into
     * the provided {@code a} array or creates a new array if it has not enough capacity.
     *
     * @param a the array to fill in
     * @return the array {@code a} if it had enough capacity or a new array containing the available values
     */
    @Beta
    public T[] getValues(T[] a) {
        return state.buffer.toArray(a);
    }

    /**
     * An empty array to trigger getValues() to return a new array.
     */
    private static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * Returns a snapshot of the currently buffered non-terminal events.
     * <p>The operation is threadsafe.
     *
     * @return a snapshot of the currently buffered non-terminal events.
     * @since (If this graduates from being an Experimental class method, replace this parenthetical with the release number)
     */
    @SuppressWarnings("unchecked")
    @Beta
    public Object[] getValues() {
        T[] r = getValues((T[]) EMPTY_ARRAY);
        if (r == EMPTY_ARRAY) {
            return new Object[0]; // don't leak the default empty array.
        }
        return r;
    }

    @Beta
    public T getValue() {
        return state.buffer.last();
    }

    /**
     * Holds onto the array of Subscriber-wrapping ReplayProducers and
     * the mBuffer that holds values to be replayed; it manages
     * subscription and signal dispatching.
     *
     * @param <T> the value type
     */
    static final class ReplayState<T>
            extends AtomicReference<ReplayProducer<T>[]>
            implements OnSubscribe<T>, Observer<T> {

        /** */
        private static final long serialVersionUID = 5952362471246910544L;

        final ReplayBuffer<T> buffer;

        @SuppressWarnings("rawtypes")
        static final ReplayProducer[] EMPTY = new ReplayProducer[0];
        @SuppressWarnings("rawtypes")
        static final ReplayProducer[] TERMINATED = new ReplayProducer[0];

        @SuppressWarnings("unchecked")
        public ReplayState(ReplayBuffer<T> buffer) {
            this.buffer = buffer;
            lazySet(EMPTY);
        }

        @Override
        public void call(Subscriber<? super T> t) {
            ReplayProducer<T> rp = new ReplayProducer<T>(t, this);
            t.add(rp);
            t.setProducer(rp);

            if (add(rp)) {
                if (rp.isUnsubscribed()) {
                    remove(rp);
                    return;
                }
            }
            buffer.drain(rp);
        }

        boolean add(ReplayProducer<T> rp) {
            for (; ; ) {
                ReplayProducer<T>[] a = get();
                if (a == TERMINATED) {
                    return false;
                }

                int n = a.length;

                @SuppressWarnings("unchecked")
                ReplayProducer<T>[] b = new ReplayProducer[n + 1];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = rp;

                if (compareAndSet(a, b)) {
                    return true;
                }
            }
        }

        @SuppressWarnings("unchecked")
        void remove(ReplayProducer<T> rp) {
            for (; ; ) {
                ReplayProducer<T>[] a = get();
                if (a == TERMINATED || a == EMPTY) {
                    return;
                }

                int n = a.length;

                int j = -1;
                for (int i = 0; i < n; i++) {
                    if (a[i] == rp) {
                        j = i;
                        break;
                    }
                }

                if (j < 0) {
                    return;
                }

                ReplayProducer<T>[] b;
                if (n == 1) {
                    b = EMPTY;
                } else {
                    b = new ReplayProducer[n - 1];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, n - j - 1);
                }
                if (compareAndSet(a, b)) {
                    return;
                }
            }
        }

        @Override
        public void onNext(T t) {
            ReplayBuffer<T> b = buffer;

            b.next(t);
            for (ReplayProducer<T> rp : get()) {
                if (rp.caughtUp) {
                    rp.actual.onNext(t);
                } else {
                    if (b.drain(rp)) {
                        rp.caughtUp = true;
                        rp.node = null;
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onError(Throwable e) {
            ReplayBuffer<T> b = buffer;

            b.error(e);
            List<Throwable> errors = null;
            for (ReplayProducer<T> rp : getAndSet(TERMINATED)) {
                try {
                    if (rp.caughtUp) {
                        rp.actual.onError(e);
                    } else {
                        if (b.drain(rp)) {
                            rp.caughtUp = true;
                            rp.node = null;
                        }
                    }
                } catch (Throwable ex) {
                    if (errors == null) {
                        errors = new ArrayList<Throwable>();
                    }
                    errors.add(ex);
                }
            }

            Exceptions.throwIfAny(errors);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onCompleted() {
            ReplayBuffer<T> b = buffer;

            b.complete();
            for (ReplayProducer<T> rp : getAndSet(TERMINATED)) {
                if (rp.caughtUp) {
                    rp.actual.onCompleted();
                } else {
                    if (b.drain(rp)) {
                        rp.caughtUp = true;
                        rp.node = null;
                    }
                }
            }
        }


        boolean isTerminated() {
            return get() == TERMINATED;
        }
    }

    /**
     * The base interface for buffering signals to be replayed to individual
     * Subscribers.
     *
     * @param <T> the value type
     */
    interface ReplayBuffer<T> {

        void next(T t);

        void error(Throwable e);

        void complete();

        boolean drain(ReplayProducer<T> rp);

        boolean isComplete();

        Throwable error();

        T last();

        int size();

        boolean isEmpty();

        T[] toArray(T[] a);

        void clear();

        void remove(T value);
    }

    static final class ReplaySizeBoundBuffer<T> implements ReplayBuffer<T> {

        final int limit;

        volatile ReplaySizeBoundBuffer.Node<T> head;

        ReplaySizeBoundBuffer.Node<T> tail;

        int size;

        volatile boolean done;
        Throwable error;

        public ReplaySizeBoundBuffer(int limit) {
            this.limit = limit;
            ReplaySizeBoundBuffer.Node<T> n = new ReplaySizeBoundBuffer.Node<T>(null);
            this.tail = n;
            this.head = n;
        }

        @Override
        public void next(T value) {
            ReplaySizeBoundBuffer.Node<T> n = new ReplaySizeBoundBuffer.Node<T>(value);
            tail.set(n);
            tail = n;
            int s = size;
            if (s == limit) {
                head = head.get();
            } else {
                size = s + 1;
            }
        }

        @Override
        public void error(Throwable ex) {
            error = ex;
            done = true;
        }

        @Override
        public void complete() {
            done = true;
        }

        @Override
        public boolean drain(ReplayProducer<T> rp) {
            if (rp.getAndIncrement() != 0) {
                return false;
            }

            final Subscriber<? super T> a = rp.actual;

            int missed = 1;

            for (; ; ) {

                long r = rp.requested.get();
                long e = 0L;

                @SuppressWarnings("unchecked")
                ReplaySizeBoundBuffer.Node<T> node = (ReplaySizeBoundBuffer.Node<T>) rp.node;
                if (node == null) {
                    node = head;
                }

                while (e != r) {
                    if (a.isUnsubscribed()) {
                        rp.node = null;
                        return false;
                    }

                    boolean d = done;
                    ReplaySizeBoundBuffer.Node<T> next = node.get();
                    boolean empty = next == null;

                    if (d && empty) {
                        rp.node = null;
                        Throwable ex = error;
                        if (ex != null) {
                            a.onError(ex);
                        } else {
                            a.onCompleted();
                        }
                        return false;
                    }

                    if (empty) {
                        break;
                    }

                    a.onNext(next.value);

                    e++;
                    node = next;
                }

                if (e == r) {
                    if (a.isUnsubscribed()) {
                        rp.node = null;
                        return false;
                    }

                    boolean d = done;
                    boolean empty = node.get() == null;

                    if (d && empty) {
                        rp.node = null;
                        Throwable ex = error;
                        if (ex != null) {
                            a.onError(ex);
                        } else {
                            a.onCompleted();
                        }
                        return false;
                    }
                }

                if (e != 0L) {
                    if (r != Long.MAX_VALUE) {
                        BackpressureUtils.produced(rp.requested, e);
                    }
                }

                rp.node = node;

                missed = rp.addAndGet(-missed);
                if (missed == 0) {
                    return r == Long.MAX_VALUE;
                }
            }
        }

        static final class Node<T> extends AtomicReference<ReplaySizeBoundBuffer.Node<T>> {

            /** */
            private static final long serialVersionUID = 3713592843205853725L;

            final T value;

            public Node(T value) {
                this.value = value;
            }
        }

        @Override
        public boolean isComplete() {
            return done;
        }

        @Override
        public Throwable error() {
            return error;
        }

        @Override
        public T last() {
            ReplaySizeBoundBuffer.Node<T> h = head;
            ReplaySizeBoundBuffer.Node<T> n;
            while ((n = h.get()) != null) {
                h = n;
            }
            return h.value;
        }

        @Override
        public int size() {
            int s = 0;
            ReplaySizeBoundBuffer.Node<T> n = head.get();
            while (n != null && s != Integer.MAX_VALUE) {
                n = n.get();
                s++;
            }
            return s;
        }

        @Override
        public boolean isEmpty() {
            return head.get() == null;
        }

        @Override
        public T[] toArray(T[] a) {
            List<T> list = new ArrayList<T>();

            ReplaySizeBoundBuffer.Node<T> n = head.get();
            while (n != null) {
                list.add(n.value);
                n = n.get();
            }
            return list.toArray(a);
        }

        @Override
        public void clear() {
            Log.d(TAG, "clear: ");
            ReplaySizeBoundBuffer.Node<T> n = new ReplaySizeBoundBuffer.Node<T>(null);
            this.tail = n;
            this.head = n;

            this.size = 0;
        }

        private static final String TAG = "SignInActivity";

        @Override
        public void remove(T value) {
            Log.d(TAG, "remove: ");
            if (value == null) {
                return;
            }

            if (value.equals(head.value)) {
                head = head.get() == null ? new ReplaySizeBoundBuffer.Node<T>(null) : head.get();
            } else {

                ReplaySizeBoundBuffer.Node<T> previous = head;
                ReplaySizeBoundBuffer.Node<T> current = head.get();


                while (current != null) {
                    if (value.equals(current.value)) {
                        ReplaySizeBoundBuffer.Node<T> next = current.get();
                        previous.set(next);
                        this.size--;

                        if (value.equals(tail.value)) {
                            tail = previous;
                        }
                        return;
                    } else {
                        current = current.get();
                    }

                }
            }


        }
    }


    /**
     * A producer and subscription implementation that tracks the current
     * replay position of a particular subscriber.
     * <p>
     * The this holds the current work-in-progress indicator used by serializing
     * replays.
     *
     * @param <T> the value type
     */
    static final class ReplayProducer<T>
            extends AtomicInteger
            implements Producer, Subscription {

        /** */
        private static final long serialVersionUID = -5006209596735204567L;

        /**
         * The wrapped Subscriber instance.
         */
        final Subscriber<? super T> actual;

        /**
         * Holds the current requested amount.
         */
        final AtomicLong requested;

        /**
         * Holds the back-reference to the replay state object.
         */
        final ReplayState<T> state;

        /**
         * Indicates this Subscriber runs unbounded and the <b>source</b>-triggered
         * mBuffer.drain() has emitted all available values.
         * <p>
         * This field has to be read and written from the source emitter's thread only.
         */
        boolean caughtUp;

        /**
         * Unbounded mBuffer.drain() uses this field to remember the absolute index of
         * values replayed to this Subscriber.
         */
        int index;

        /**
         * Unbounded mBuffer.drain() uses this index within its current node to indicate
         * how many items were replayed from that particular node so far.
         */
        int tailIndex;

        /**
         * Stores the current replay node of the mBuffer to be used by mBuffer.drain().
         */
        Object node;

        public ReplayProducer(Subscriber<? super T> actual, ReplayState<T> state) {
            this.actual = actual;
            this.requested = new AtomicLong();
            this.state = state;
        }

        @Override
        public void unsubscribe() {
            state.remove(this);
        }

        @Override
        public boolean isUnsubscribed() {
            return actual.isUnsubscribed();
        }

        @Override
        public void request(long n) {
            if (n > 0L) {
                BackpressureUtils.getAndAddRequest(requested, n);
                state.buffer.drain(this);
            } else if (n < 0L) {
                throw new IllegalArgumentException("n >= required but it was " + n);
            }
        }

    }
}

