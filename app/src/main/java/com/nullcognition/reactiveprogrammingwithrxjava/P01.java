package com.nullcognition.reactiveprogrammingwithrxjava;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by mms on 11/19/16.
 * infinite streams
 * creation
 * nevr, empty,
 * interruption,
 * cache
 * unsub and takeuntil
 */

public class P01 {

  public static Observable<Integer> get() {
    return Observable.range(0, 100);
  }

  public static void infiniteS() {
    // don't do this, don't create new threads in here either
    Observable.create(new Observable.OnSubscribe<Object>() {
      @Override public void call(Subscriber<? super Object> subscriber) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
          subscriber.onNext(i);
        }
        subscriber.onCompleted();
      }
    });
  }

  public static void nevEv() {
    Observable.create(subscriber -> {
      subscriber.onCompleted();
      // or
    });
    Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(Subscriber<? super Void> subscriber) {
      }
    });
  }

  public static void takeUnt() {
    get().takeUntil(integer -> integer.equals(5));
  }

  public static void cache() {
    Observable cached = get().map(integer -> {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return integer;
    }).cache();
    Subscription s = cached.subscribe(MainActivity::p); // values will be cached
    Subscription cachedSub = cached.subscribe(MainActivity::p); // get the cached values
  }

  public static void callable() {
    Observable.fromCallable(() -> 9);
  }
}
