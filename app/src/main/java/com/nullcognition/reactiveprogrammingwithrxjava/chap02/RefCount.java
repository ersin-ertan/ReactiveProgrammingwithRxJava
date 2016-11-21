package com.nullcognition.reactiveprogrammingwithrxjava.chap02;

import com.nullcognition.reactiveprogrammingwithrxjava.MainActivity;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by mms on 11/21/16.
 */

public class RefCount {

  public RefCount() {

  }

  void wastefulSubscription() {
    Observable<Integer> observable = Observable.create(subscriber -> {
      MainActivity.p("starting connection");
      final Stream stream = Stream.create();
      subscriber.add(Subscriptions.create(() -> {
        MainActivity.p("dis connection");
        stream.shutdown();
      }));
      stream.get();
    });

    Subscription s1 = observable.subscribe();
    Subscription s2 = observable.subscribe();
    Subscription s3 = observable.subscribe();
    // thus each subscription will establish a new connection, and disconnect individually(non atomic)

    // but we can use the publish().refCount() pair

    Observable lazy = observable.publish().refCount();

    Subscription s4 = observable.subscribe();
    Subscription s5 = observable.subscribe();
    Subscription s6 = observable.subscribe();

    // two main points occur here: upon the first subscription we establish the connection
    // once and only once through out the other subscriptions
    // and will disconnect after the first unsubscription

    // refcount counts the active subscribers on the observable and has two important points:
    // when the subs go from zero to one and then shares the same subscription source(observable)
    // among oll the other subscribers, and when the subscribers go from 1 to zero, which unsubs
    // from the source

    observable.share(); // is the alias but with the feature that unsub, the subscription will
    // initiation a reconnection as if there were no caching at all.
  }
  // publish() - returns a connected observable which waits until connect is called before
  // emitting items from the observer it was subscribed from. Honours backpressure and does not
  // run on a default scheduler
}

class Stream {
  static Stream create() {
    return new Stream();
  }

  void shutdown() {
  }

  Integer get() {
    return 1;
  }
}
