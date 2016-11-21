package com.nullcognition.reactiveprogrammingwithrxjava.chap02;

import com.nullcognition.reactiveprogrammingwithrxjava.MainActivity;
import rx.Observable;
import rx.Subscription;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * Created by mms on 11/20/16.
 */

public class P03 {

  AsyncSubject<Integer> as = AsyncSubject.create();
  BehaviorSubject<Integer> bs = BehaviorSubject.create(2);
  ReplaySubject<Integer> rs = ReplaySubject.create(10);

  public P03() {
    callback();
    PublishSubject<Integer> ps = pubSub();
    Subscription a = ps.subscribe(MainActivity::p);
    Subscription b = ps.subscribe(MainActivity::p);
    ps.onNext(3);
    MainActivity.p("both pub sub should have gotten 3");

    as.onNext(1);
    as.onNext(2);
    as.onCompleted();
    MainActivity.p("async sub should be 2");
    as.subscribe(MainActivity::p);

    bs.onNext(1);
    bs.onNext(2);
    MainActivity.p("behaviour sub should be 2, 3");
    bs.subscribe(MainActivity::p);
    bs.onNext(3);

    rs.onNext(1);
    rs.onNext(2);
    rs.onNext(3);
    MainActivity.p("replay sub should be {1,2,3}");
    rs.subscribe(MainActivity::p);
  }

  private PublishSubject<Integer> pubSub() {
    return PublishSubject.create();
  }

  private Observable callback() {
    return Observable.fromCallable(() -> 9);
  }
}
