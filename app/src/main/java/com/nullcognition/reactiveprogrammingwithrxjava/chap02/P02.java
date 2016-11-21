package com.nullcognition.reactiveprogrammingwithrxjava.chap02;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by mms on 11/19/16.
 */

public class P02 {

  void main() {
    Observable.never();
    Observable.empty();
    Observable.timer(100, TimeUnit.SECONDS);
    Observable.interval(100, TimeUnit.SECONDS);
    Observable.range(1, 99);
    Observable.create(subscriber -> {
      if (!subscriber.isUnsubscribed()) {
        subscriber.onNext(4);
        subscriber.onCompleted();
      }
    });
    Subscription subscription = Observable.range(1, 100).cache().subscribe();
    Observable.never();
    subscription.isUnsubscribed();
    subscription.unsubscribe();
    Observable.empty().doOnUnsubscribe(() -> {
    }).unsubscribeOn(Schedulers.io());
  }
}
